package com.eventpass.seats.repository;

import com.eventpass.seats.model.Seat;
import com.eventpass.seats.model.SeatStatus;
import com.eventpass.seats.model.SeatZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatCode(String seatCode);

    List<Seat> findByZone(SeatZone zone);

    List<Seat> findByStatus(SeatStatus status);

    List<Seat> findByZoneAndStatus(SeatZone zone, SeatStatus status);

    long countByStatus(SeatStatus status);

    long countByZoneAndStatus(SeatZone zone, SeatStatus status);
}
