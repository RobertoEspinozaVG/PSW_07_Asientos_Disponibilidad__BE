package com.eventpass.seats.exception;

public class SeatAlreadyReservedException extends RuntimeException {
    public SeatAlreadyReservedException(String seatCode) {
        super("El asiento '" + seatCode + "' ya se encuentra reservado u ocupado y no está disponible.");
    }
}
