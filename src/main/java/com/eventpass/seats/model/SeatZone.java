package com.eventpass.seats.model;

public enum SeatZone {
    VIP(250.00),
    PREFERENTIAL(150.00),
    GENERAL(80.00);

    private final double price;

    SeatZone(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
