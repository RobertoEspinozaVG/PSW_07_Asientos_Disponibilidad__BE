package com.eventpass.seats.exception;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(String seatCode) {
        super("El asiento con codigo '" + seatCode + "' no fue encontrado en el sistema.");
    }
}
