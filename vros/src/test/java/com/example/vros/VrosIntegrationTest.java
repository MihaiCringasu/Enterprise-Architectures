package com.example.vros;

import com.example.vros.routing.RouteDto;
import com.example.vros.routing.RoutingOptimizationService;
import com.example.vros.routing.Scenario;
import com.example.vros.routing.ScenarioRepository;
import com.example.vros.customer.DeliveryRepository;
import com.example.vros.fleet.VehicleRepository;
import com.example.vros.security.AppUserRepository;
import com.example.vros.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VrosIntegrationTest {

    @Autowired private AppUserRepository userRepository;
    @Autowired private JwtService jwtService;
    @Autowired private VehicleRepository vehicleRepository;
    @Autowired private DeliveryRepository deliveryRepository;
    @Autowired private ScenarioRepository scenarioRepository;
    @Autowired private RoutingOptimizationService optimizationService;

    @Test
    void testJwtGenerationAndRouteOptimizationFlow() {
        var admin = userRepository.findByUsername("admin").orElseThrow();
        String token = jwtService.generateToken(admin);
        var claims = jwtService.validateAndExtract(token);

        assertNotNull(claims);
        assertEquals("admin", claims.username());

        Scenario scenario = new Scenario("Test Integration Scenario " + System.currentTimeMillis());
        scenario.setVehicles(new HashSet<>(vehicleRepository.findAll()));
        scenario.setDeliveries(new HashSet<>(deliveryRepository.findAll()));
        Scenario saved = scenarioRepository.save(scenario);

        List<RouteDto> routes = optimizationService.optimizeScenario(saved.getId());
        assertFalse(routes.isEmpty(), "Trebuie să se genereze cel puțin o rută optimizată");
        assertTrue(routes.get(0).totalDistanceKm() > 0, "Distanța calculată trebuie să fie pozitivă");
    }
}
