package com.eventpass.seats.dto;

import com.eventpass.seats.model.SeatStatus;
import com.eventpass.seats.model.SeatZone;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponseDTO {
    private Long id;
    private String seatCode;
    private String rowNumber;
    private Integer number;
    private SeatZone zone;
    private SeatStatus status;
    private Double price;
}
