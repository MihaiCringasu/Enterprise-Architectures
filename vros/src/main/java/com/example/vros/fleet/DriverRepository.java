package com.example.vros.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    @Query("SELECT d FROM Driver d WHERE d.vehicle IS NULL")
    List<Driver> findUnassignedDrivers();
}
