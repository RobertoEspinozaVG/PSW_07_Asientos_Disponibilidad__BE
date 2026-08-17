package com.eventpass.seats.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilitySummaryDTO {
    private long totalSeats;
    private long availableSeats;
    private long occupiedSeats;
    private long blockedSeats;
}
