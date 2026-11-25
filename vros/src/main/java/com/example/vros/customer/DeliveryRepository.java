package com.example.vros.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByCustomerId(Long customerId);
    @Query("SELECT SUM(d.quantity) FROM Delivery d WHERE d.customer.id = :customerId")
    Double getTotalQuantityByCustomer(@Param("customerId") Long customerId);
}
