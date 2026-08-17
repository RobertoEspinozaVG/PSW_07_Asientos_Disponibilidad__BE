package com.eventpass.seats.controller;

import com.eventpass.seats.dto.AvailabilitySummaryDTO;
import com.eventpass.seats.dto.ReserveSeatRequest;
import com.eventpass.seats.dto.SeatResponseDTO;
import com.eventpass.seats.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats(
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(seatService.getAllSeats(zone, status));
    }

    @GetMapping("/{seatCode}")
    public ResponseEntity<SeatResponseDTO> getSeatByCode(@PathVariable String seatCode) {
        return ResponseEntity.ok(seatService.getSeatByCode(seatCode));
    }

    @GetMapping("/summary")
    public ResponseEntity<AvailabilitySummaryDTO> getSummary() {
        return ResponseEntity.ok(seatService.getAvailabilitySummary());
    }

    @PostMapping("/reserve")
    public ResponseEntity<SeatResponseDTO> reserveSeat(@Valid @RequestBody ReserveSeatRequest request) {
        SeatResponseDTO reserved = seatService.reserveSeat(request.getSeatCode(), request.getUserId());
        return ResponseEntity.ok(reserved);
    }
}
