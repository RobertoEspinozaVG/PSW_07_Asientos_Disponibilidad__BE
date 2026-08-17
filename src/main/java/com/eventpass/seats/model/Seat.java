package com.eventpass.seats.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String seatCode; // Ej: "VIP-A1", "GEN-C5"

    @Column(nullable = false)
    private String rowNumber; // Ej: "A", "B", "C"

    @Column(nullable = false)
    private Integer number; // Ej: 1, 2, 3

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatZone zone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Column(nullable = false)
    private Double price;
}
