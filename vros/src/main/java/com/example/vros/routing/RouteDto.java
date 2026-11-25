package com.example.vros.routing;

import java.util.List;

public record RouteDto(
        Long routeId,
        Long scenarioId,
        String vehicleName,
        Double vehicleCapacity,
        Double totalLoad,
        Double totalDistanceKm,
        List<StopDto> stops
) {
    public record StopDto(int order, Long deliveryId, String customerName, String address, String coordinates, Double quantity) {}
}
