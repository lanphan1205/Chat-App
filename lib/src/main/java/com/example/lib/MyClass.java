package com.example.lib;

public class MyClass {
    static void startEngine(Vehicle vehicle) {
        vehicle.startEngine();
    }
    public static void main(String[] args) {
//        Car car = new Car();
//        startEngine(car);

//        Car car = (Car) new Vehicle();

        // Down Casting
        String drive = (String) new Car().drive();
        System.out.println(drive);
    }

}

class Vehicle {
    Vehicle() {

    }
    void startEngine() {
        System.out.println("Make some sound ..");
    }
}

class Car extends Vehicle{
    Car() {
        super();
    }
    @Override
    void startEngine() {
        System.out.println("Brmm brmm ..");
    }

    Object drive() {
        return "Drive very fast";
    }
}