package com.example.vros.fleet;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver save(Driver driver) { return driverRepository.save(driver); }
    public List<Driver> findUnassignedDrivers() { return driverRepository.findUnassignedDrivers(); }
}
