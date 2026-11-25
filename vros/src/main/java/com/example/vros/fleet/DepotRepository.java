package com.example.vros.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DepotRepository extends JpaRepository<Depot, Long> {
    Optional<Depot> findByName(String name);
}
