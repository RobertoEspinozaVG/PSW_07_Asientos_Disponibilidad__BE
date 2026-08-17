package com.eventpass.seats.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveSeatRequest {

    @NotBlank(message = "El codigo del asiento es obligatorio")
    private String seatCode;

    @NotBlank(message = "El ID de usuario es obligatorio")
    private String userId;
}
