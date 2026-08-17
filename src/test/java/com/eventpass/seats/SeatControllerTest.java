package com.eventpass.seats;

import com.eventpass.seats.dto.ReserveSeatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("CP-01: Validar consulta exitosa de todos los asientos y mapa de disponibilidad")
    void testGetAllSeats() throws Exception {
        mockMvc.perform(get("/api/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].seatCode", notNullValue()))
                .andExpect(jsonPath("$[0].status", notNullValue()));
    }

    @Test
    @DisplayName("CP-02: Validar fallo (409 Conflict) al intentar reservar un asiento ya ocupado")
    void testReserveOccupiedSeat_ShouldReturn409() throws Exception {
        // VIP-A3 es un asiento sembrado como OCCUPIED por defecto
        ReserveSeatRequest request = ReserveSeatRequest.builder()
                .seatCode("VIP-A3")
                .userId("USER-123")
                .build();

        mockMvc.perform(post("/api/seats/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.error", is("Conflict / Seat Unavailable")))
                .andExpect(jsonPath("$.message", containsString("ya se encuentra reservado u ocupado")));
    }

    @Test
    @DisplayName("CP-03: Validar reserva exitosa de un asiento disponible")
    void testReserveAvailableSeat_ShouldReturn200() throws Exception {
        // VIP-A1 es un asiento disponible
        ReserveSeatRequest request = ReserveSeatRequest.builder()
                .seatCode("VIP-A1")
                .userId("USER-456")
                .build();

        mockMvc.perform(post("/api/seats/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatCode", is("VIP-A1")))
                .andExpect(jsonPath("$.status", is("OCCUPIED")));
    }

    @Test
    @DisplayName("CP-04: Validar consulta de resumen de contadores de disponibilidad")
    void testGetSummary() throws Exception {
        mockMvc.perform(get("/api/seats/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeats", greaterThan(0)))
                .andExpect(jsonPath("$.availableSeats", greaterThan(0)))
                .andExpect(jsonPath("$.occupiedSeats", greaterThan(0)));
    }
}
