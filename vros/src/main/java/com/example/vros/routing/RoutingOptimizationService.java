package com.example.vros.routing;

import com.example.vros.customer.Delivery;
import com.example.vros.fleet.Vehicle;
import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoutingOptimizationService {

    private final ScenarioRepository scenarioRepository;
    private final RouteRepository routeRepository;
    private final LiveNotificationService notificationService;

    public RoutingOptimizationService(ScenarioRepository scenarioRepository,
                                      RouteRepository routeRepository,
                                      LiveNotificationService notificationService) {
        this.scenarioRepository = scenarioRepository;
        this.routeRepository = routeRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public List<RouteDto> optimizeScenario(Long scenarioId) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found: " + scenarioId));

        notificationService.broadcast("status", "Pornire optimizare VRP pentru scenariul: " + scenario.getName());

        routeRepository.deleteByScenarioId(scenarioId);

        List<Vehicle> availableVehicles = scenario.getVehicles().stream()
                .filter(v -> Boolean.TRUE.equals(v.getAvailable()))
                .sorted(Comparator.comparingDouble(Vehicle::getCapacity).reversed())
                .toList();

        List<Delivery> remainingDeliveries = new ArrayList<>(scenario.getDeliveries());
        List<RouteDto> resultDtos = new ArrayList<>();

        for (Vehicle vehicle : availableVehicles) {
            if (remainingDeliveries.isEmpty()) break;

            double[] currentCoords = parseCoords(vehicle.getDepot().getCoordinates());
            double currentLoad = 0.0;
            double totalDistance = 0.0;
            int order = 1;

            Route route = new Route(scenario, vehicle, 0.0, 0.0);

            while (!remainingDeliveries.isEmpty()) {
                Delivery nearest = null;
                double minDist = Double.MAX_VALUE;

                for (Delivery d : remainingDeliveries) {
                    if (currentLoad + d.getQuantity() <= vehicle.getCapacity()) {
                        double[] destCoords = parseCoords(d.getCustomer().getCoordinates());
                        double dist = haversineKm(currentCoords[0], currentCoords[1], destCoords[0], destCoords[1]);
                        if (dist < minDist) {
                            minDist = dist;
                            nearest = d;
                        }
                    }
                }

                if (nearest == null) break;

                remainingDeliveries.remove(nearest);
                currentLoad += nearest.getQuantity();
                totalDistance += minDist;
                currentCoords = parseCoords(nearest.getCustomer().getCoordinates());

                route.getStops().add(new RouteStop(route, nearest, order++));
            }

            if (!route.getStops().isEmpty()) {
                double[] depotCoords = parseCoords(vehicle.getDepot().getCoordinates());
                totalDistance += haversineKm(currentCoords[0], currentCoords[1], depotCoords[0], depotCoords[1]);

                route.setTotalDistance(Math.round(totalDistance * 100.0) / 100.0);
                route.setTotalLoad(currentLoad);

                Route savedRoute = routeRepository.save(route);
                RouteDto dto = toDto(savedRoute);
                resultDtos.add(dto);

                notificationService.broadcast("route_computed", dto);
            }
        }

        notificationService.broadcast("status", "Optimizare finalizată! Rute generate: " + resultDtos.size()
                + " | Livrări nealocate: " + remainingDeliveries.size());

        return resultDtos;
    }

    public List<RouteDto> getRoutesForScenario(Long scenarioId) {
        return routeRepository.findByScenarioId(scenarioId).stream().map(this::toDto).toList();
    }

    private RouteDto toDto(Route r) {
        List<RouteDto.StopDto> stopDtos = r.getStops().stream()
                .map(s -> new RouteDto.StopDto(
                        s.getStopOrder(),
                        s.getDelivery().getId(),
                        s.getDelivery().getCustomer().getName(),
                        s.getDelivery().getCustomer().getAddress(),
                        s.getDelivery().getCustomer().getCoordinates(),
                        s.getDelivery().getQuantity()
                )).toList();

        return new RouteDto(
                r.getId(),
                r.getScenario().getId(),
                r.getVehicle().getName(),
                r.getVehicle().getCapacity(),
                r.getTotalLoad(),
                r.getTotalDistance(),
                stopDtos
        );
    }

    private double[] parseCoords(String coords) {
        String[] parts = coords.split(",");
        return new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())};
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return r * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
