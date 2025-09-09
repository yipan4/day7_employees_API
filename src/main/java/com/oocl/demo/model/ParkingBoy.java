package com.oocl.demo.model;

import java.util.UUID;

public class ParkingBoy {
    private final String name;
    private final UUID id;
    private final ParkingLot parkingLot;

    public ParkingBoy(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.parkingLot = new ParkingLot();
    }

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.id;
    }

    public boolean park(Car car) {
        return parkingLot.park(car);
    }
}
