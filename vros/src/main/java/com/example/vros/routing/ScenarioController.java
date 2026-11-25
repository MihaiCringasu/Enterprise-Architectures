package com.example.vros.routing;

import com.example.vros.customer.Delivery;
import com.example.vros.customer.DeliveryRepository;
import com.example.vros.fleet.Vehicle;
import com.example.vros.fleet.VehicleRepository;
import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioRepository scenarioRepository;
    private final VehicleRepository vehicleRepository;
    private final DeliveryRepository deliveryRepository;

    public ScenarioController(ScenarioRepository scenarioRepository,
                              VehicleRepository vehicleRepository,
                              DeliveryRepository deliveryRepository) {
        this.scenarioRepository = scenarioRepository;
        this.vehicleRepository = vehicleRepository;
        this.deliveryRepository = deliveryRepository;
    }

    @GetMapping
    public List<ScenarioDto> getAll() {
        return scenarioRepository.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ScenarioDto getById(@PathVariable Long id) {
        return scenarioRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<ScenarioDto> create(@RequestBody ScenarioDto dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Numele scenariului este obligatoriu");
        }
        Scenario scenario = new Scenario(dto.name());
        populateRelations(scenario, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(scenarioRepository.save(scenario)));
    }

    @PutMapping("/{id}")
    public ScenarioDto update(@PathVariable Long id, @RequestBody ScenarioDto dto) {
        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException("Numele scenariului este obligatoriu");
        }
        Scenario scenario = scenarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scenario not found with id: " + id));
        scenario.setName(dto.name());
        populateRelations(scenario, dto);
        return toDto(scenarioRepository.save(scenario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!scenarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Scenario not found with id: " + id);
        }
        scenarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void populateRelations(Scenario scenario, ScenarioDto dto) {
        if (dto.vehicleIds() != null) {
            Set<Vehicle> vehicles = new HashSet<>(vehicleRepository.findAllById(dto.vehicleIds()));
            scenario.setVehicles(vehicles);
        }
        if (dto.deliveryIds() != null) {
            Set<Delivery> deliveries = new HashSet<>(deliveryRepository.findAllById(dto.deliveryIds()));
            scenario.setDeliveries(deliveries);
        }
    }

    private ScenarioDto toDto(Scenario s) {
        Set<Long> vIds = s.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toSet());
        Set<Long> dIds = s.getDeliveries().stream().map(Delivery::getId).collect(Collectors.toSet());
        return new ScenarioDto(s.getId(), s.getName(), vIds, dIds);
    }
}
