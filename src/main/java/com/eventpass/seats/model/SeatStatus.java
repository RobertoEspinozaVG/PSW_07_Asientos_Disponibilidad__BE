package com.eventpass.seats.model;

public enum SeatStatus {
    AVAILABLE,   // Asiento libre para selección
    OCCUPIED,    // Asiento reservado/pagado (no seleccionable)
    BLOCKED      // Asiento bloqueado por mantenimiento o contingencia
}
