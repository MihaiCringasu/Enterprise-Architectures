package com.example.vros.fleet;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FleetFacade {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final ApplicationEventPublisher eventPublisher;

    public FleetFacade(VehicleRepository vehicleRepository,
                       DriverRepository driverRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<VehicleDto> getAvailableVehicles() {
        return vehicleRepository.findAll().stream()
                .filter(v -> Boolean.TRUE.equals(v.getAvailable()))
                .map(v -> new VehicleDto(v.getId(), v.getName(), v.getCapacity(), v.getAvailable()))
                .toList();
    }

    @Transactional
    public void assignDriverToVehicle(Long vehicleId, Long driverId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + vehicleId));
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new IllegalArgumentException("Driver not found: " + driverId));

        vehicle.setDriver(driver);
        vehicleRepository.save(vehicle);
        eventPublisher.publishEvent(new VehicleAssignedEvent(vehicleId, driverId));
    }
}
