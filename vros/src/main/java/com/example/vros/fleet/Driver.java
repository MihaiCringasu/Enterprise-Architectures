package com.example.vros.fleet;

import com.example.vros.shared.Person;
import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "driver")
    private Vehicle vehicle;

    public Driver() {}

    public Driver(String name, String email, String phone) {
        super(name, email, phone);
    }

    public Long getId() { return id; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
