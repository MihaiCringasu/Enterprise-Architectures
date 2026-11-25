package com.example.vros.customer;

import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;
    private final CustomerRepository customerRepository;

    public DeliveryController(DeliveryRepository deliveryRepository, CustomerRepository customerRepository) {
        this.deliveryRepository = deliveryRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<DeliveryDto> getAll() {
        return deliveryRepository.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public DeliveryDto getById(@PathVariable Long id) {
        return deliveryRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<DeliveryDto> create(@RequestBody DeliveryDto dto) {
        validate(dto);
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.customerId()));
        Delivery saved = deliveryRepository.save(new Delivery(customer, dto.quantity(), dto.notes()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public DeliveryDto update(@PathVariable Long id, @RequestBody DeliveryDto dto) {
        validate(dto);
        Delivery existing = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with id: " + id));
        Customer customer = customerRepository.findById(dto.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.customerId()));
        existing.setCustomer(customer);
        existing.setQuantity(dto.quantity());
        existing.setNotes(dto.notes());
        return toDto(deliveryRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!deliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery not found with id: " + id);
        }
        deliveryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validate(DeliveryDto dto) {
        if (dto.customerId() == null) throw new IllegalArgumentException("ID-ul clientului este obligatoriu");
        if (dto.quantity() == null || dto.quantity() <= 0) throw new IllegalArgumentException("Cantitatea trebuie să fie pozitivă");
    }

    private DeliveryDto toDto(Delivery d) {
        return new DeliveryDto(d.getId(), d.getCustomer().getId(), d.getQuantity(), d.getNotes(), d.getTimestamp());
    }
}
