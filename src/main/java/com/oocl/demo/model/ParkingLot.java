package com.oocl.demo.model;

import com.oocl.demo.model.Car;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private int capacity = 10;
    private final List<Car> parkedCars = new ArrayList<>();

    public ParkingLot() {};

    public ParkingLot(int capacity) {
        this.capacity = capacity;
    }

    public boolean park(Car car) {
        if (parkedCars.size() >= capacity) {
            return false;
        }
        parkedCars.add(car);
        return true;
    }
}
