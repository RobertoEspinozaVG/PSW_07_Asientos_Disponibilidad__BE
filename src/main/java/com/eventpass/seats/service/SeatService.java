package com.eventpass.seats.service;

import com.eventpass.seats.dto.AvailabilitySummaryDTO;
import com.eventpass.seats.dto.SeatResponseDTO;
import com.eventpass.seats.exception.SeatAlreadyReservedException;
import com.eventpass.seats.exception.SeatNotFoundException;
import com.eventpass.seats.model.Seat;
import com.eventpass.seats.model.SeatStatus;
import com.eventpass.seats.model.SeatZone;
import com.eventpass.seats.repository.SeatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @PostConstruct
    public void initData() {
        if (seatRepository.count() == 0) {
            // Seed Zone VIP (Rows A & B)
            seedSeats("A", 1, 10, SeatZone.VIP, List.of(3, 7)); // Seats 3 and 7 are OCCUPIED
            seedSeats("B", 1, 10, SeatZone.VIP, List.of(2, 5, 8)); // 2,5,8 OCCUPIED

            // Seed Zone PREFERENTIAL (Rows C & D)
            seedSeats("C", 1, 12, SeatZone.PREFERENTIAL, List.of(4, 9, 11));
            seedSeats("D", 1, 12, SeatZone.PREFERENTIAL, List.of(1, 6, 12));

            // Seed Zone GENERAL (Rows E & F)
            seedSeats("E", 1, 15, SeatZone.GENERAL, List.of(5, 10, 15));
            seedSeats("F", 1, 15, SeatZone.GENERAL, List.of(2, 4, 8, 14));
        }
    }

    private void seedSeats(String row, int startNum, int endNum, SeatZone zone, List<Integer> occupiedIndices) {
        for (int i = startNum; i <= endNum; i++) {
            SeatStatus status = occupiedIndices.contains(i) ? SeatStatus.OCCUPIED : SeatStatus.AVAILABLE;
            Seat seat = Seat.builder()
                    .seatCode(zone.name().substring(0, 3) + "-" + row + i)
                    .rowNumber(row)
                    .number(i)
                    .zone(zone)
                    .status(status)
                    .price(zone.getPrice())
                    .build();
            seatRepository.save(seat);
        }
    }

    public List<SeatResponseDTO> getAllSeats(String zoneStr, String statusStr) {
        List<Seat> seats;

        if (zoneStr != null && statusStr != null) {
            SeatZone zone = SeatZone.valueOf(zoneStr.toUpperCase());
            SeatStatus status = SeatStatus.valueOf(statusStr.toUpperCase());
            seats = seatRepository.findByZoneAndStatus(zone, status);
        } else if (zoneStr != null) {
            SeatZone zone = SeatZone.valueOf(zoneStr.toUpperCase());
            seats = seatRepository.findByZone(zone);
        } else if (statusStr != null) {
            SeatStatus status = SeatStatus.valueOf(statusStr.toUpperCase());
            seats = seatRepository.findByStatus(status);
        } else {
            seats = seatRepository.findAll();
        }

        return seats.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public SeatResponseDTO getSeatByCode(String seatCode) {
        Seat seat = seatRepository.findBySeatCode(seatCode)
                .orElseThrow(() -> new SeatNotFoundException(seatCode));
        return mapToDTO(seat);
    }

    public AvailabilitySummaryDTO getAvailabilitySummary() {
        long total = seatRepository.count();
        long available = seatRepository.countByStatus(SeatStatus.AVAILABLE);
        long occupied = seatRepository.countByStatus(SeatStatus.OCCUPIED);
        long blocked = seatRepository.countByStatus(SeatStatus.BLOCKED);

        return AvailabilitySummaryDTO.builder()
                .totalSeats(total)
                .availableSeats(available)
                .occupiedSeats(occupied)
                .blockedSeats(blocked)
                .build();
    }

    @Transactional
    public SeatResponseDTO reserveSeat(String seatCode, String userId) {
        Seat seat = seatRepository.findBySeatCode(seatCode)
                .orElseThrow(() -> new SeatNotFoundException(seatCode));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new SeatAlreadyReservedException(seatCode);
        }

        seat.setStatus(SeatStatus.OCCUPIED);
        Seat updatedSeat = seatRepository.save(seat);
        return mapToDTO(updatedSeat);
    }

    private SeatResponseDTO mapToDTO(Seat seat) {
        return SeatResponseDTO.builder()
                .id(seat.getId())
                .seatCode(seat.getSeatCode())
                .rowNumber(seat.getRowNumber())
                .number(seat.getNumber())
                .zone(seat.getZone())
                .status(seat.getStatus())
                .price(seat.getPrice())
                .build();
    }
}
