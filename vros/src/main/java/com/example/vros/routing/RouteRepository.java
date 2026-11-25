package com.example.vros.routing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByScenarioId(Long scenarioId);
    void deleteByScenarioId(Long scenarioId);
}
