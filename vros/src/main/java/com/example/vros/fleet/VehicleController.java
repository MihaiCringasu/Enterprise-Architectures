package com.example.vros.fleet;

import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final DepotRepository depotRepository;

    public VehicleController(VehicleRepository vehicleRepository, DepotRepository depotRepository) {
        this.vehicleRepository = vehicleRepository;
        this.depotRepository = depotRepository;
    }

    @GetMapping
    public List<VehicleDto> getAll() {
        return vehicleRepository.findAll().stream()
                .map(v -> new VehicleDto(v.getId(), v.getName(), v.getCapacity(), v.getAvailable()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getById(
            @PathVariable Long id,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        VehicleDto dto = new VehicleDto(v.getId(), v.getName(), v.getCapacity(), v.getAvailable());
        String eTag = "\"" + Objects.hash(dto.id(), dto.name(), dto.capacity(), dto.available()) + "\"";

        if (eTag.equals(ifNoneMatch)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).eTag(eTag).build();
        }

        return ResponseEntity.ok().eTag(eTag).body(dto);
    }

    @PostMapping
    public ResponseEntity<VehicleDto> create(@RequestBody VehicleDto dto) {
        validate(dto);
        Depot defaultDepot = depotRepository.findAll().stream().findFirst()
                .orElseGet(() -> depotRepository.save(new Depot("Default Depot", "47.1585,27.6014")));

        Vehicle saved = vehicleRepository.save(new Vehicle(
                dto.name(), dto.capacity(), defaultDepot, dto.available() != null ? dto.available() : true));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new VehicleDto(saved.getId(), saved.getName(), saved.getCapacity(), saved.getAvailable()));
    }

    @PutMapping("/{id}")
    public VehicleDto update(@PathVariable Long id, @RequestBody VehicleDto dto) {
        validate(dto);
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        v.setName(dto.name());
        v.setCapacity(dto.capacity());
        if (dto.available() != null) v.setAvailable(dto.available());
        Vehicle saved = vehicleRepository.save(v);
        return new VehicleDto(saved.getId(), saved.getName(), saved.getCapacity(), saved.getAvailable());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validate(VehicleDto dto) {
        if (dto.name() == null || dto.name().isBlank()) throw new IllegalArgumentException("Numele vehiculului este obligatoriu");
        if (dto.capacity() == null || dto.capacity() <= 0) throw new IllegalArgumentException("Capacitatea trebuie să fie pozitivă");
    }
}
