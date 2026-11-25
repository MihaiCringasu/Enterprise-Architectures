package com.example.vros.routing;

import com.example.vros.customer.Delivery;
import jakarta.persistence.*;

@Entity
@Table(name = "route_stops")
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(optional = false)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    public RouteStop() {}

    public RouteStop(Route route, Delivery delivery, Integer stopOrder) {
        this.route = route;
        this.delivery = delivery;
        this.stopOrder = stopOrder;
    }

    public Long getId() { return id; }
    public Route getRoute() { return route; }
    public Delivery getDelivery() { return delivery; }
    public Integer getStopOrder() { return stopOrder; }
}
