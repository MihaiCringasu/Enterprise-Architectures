package com.example.vros.fleet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle save(Vehicle vehicle) { return vehicleRepository.save(vehicle); }

    public List<Vehicle> findAvailableWithCapacity(Double minCapacity) {
        return vehicleRepository.findByAvailableTrueAndCapacityGreaterThanEqual(minCapacity);
    }

    @Transactional
    public void setVehicleAvailability(Long vehicleId, boolean available) {
        vehicleRepository.updateAvailabilityStatus(vehicleId, available);
    }
}
