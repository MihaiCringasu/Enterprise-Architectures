package com.example.vros.routing;

import com.example.vros.fleet.Vehicle;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "total_distance", nullable = false)
    private Double totalDistance;

    @Column(name = "total_load", nullable = false)
    private Double totalLoad;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stopOrder ASC")
    private List<RouteStop> stops = new ArrayList<>();

    public Route() {}

    public Route(Scenario scenario, Vehicle vehicle, Double totalDistance, Double totalLoad) {
        this.scenario = scenario;
        this.vehicle = vehicle;
        this.totalDistance = totalDistance;
        this.totalLoad = totalLoad;
    }

    public Long getId() { return id; }
    public Scenario getScenario() { return scenario; }
    public Vehicle getVehicle() { return vehicle; }
    public Double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(Double totalDistance) { this.totalDistance = totalDistance; }
    public Double getTotalLoad() { return totalLoad; }
    public void setTotalLoad(Double totalLoad) { this.totalLoad = totalLoad; }
    public List<RouteStop> getStops() { return stops; }
}
