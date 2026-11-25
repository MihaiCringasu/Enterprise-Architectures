package com.example.vros.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByAvailableTrueAndCapacityGreaterThanEqual(Double minCapacity);

    @Query("SELECT v FROM Vehicle v JOIN FETCH v.depot WHERE v.depot.id = :depotId")
    List<Vehicle> findAllByDepotIdWithDepot(@Param("depotId") Long depotId);

    @Modifying
    @Transactional
    @Query("UPDATE Vehicle v SET v.available = :status WHERE v.id = :vehicleId")
    int updateAvailabilityStatus(@Param("vehicleId") Long vehicleId, @Param("status") boolean status);
}
