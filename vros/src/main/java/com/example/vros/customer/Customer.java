package com.example.vros.customer;

import com.example.vros.shared.Person;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 100)
    private String coordinates;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    public Customer() {}

    public Customer(String name, String email, String phone, String address, String coordinates) {
        super(name, email, phone);
        this.address = address;
        this.coordinates = coordinates;
    }

    public Long getId() { return id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }
    public List<Delivery> getDeliveries() { return deliveries; }
}
