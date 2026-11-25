package com.example.vros.customer;

import com.example.vros.shared.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDto> getAllJson() {
        return customerRepository.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public String getAllXml() {
        String items = customerRepository.findAll().stream()
                .map(c -> String.format("<customer><id>%d</id><name>%s</name><email>%s</email><phone>%s</phone><address>%s</address><coordinates>%s</coordinates></customer>",
                        c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getCoordinates()))
                .collect(Collectors.joining());
        return "<customers>" + items + "</customers>";
    }

    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return toDto(c);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto dto) {
        validate(dto);
        Customer saved = customerRepository.save(new Customer(
                dto.name(), dto.email(), dto.phone(), dto.address(), dto.coordinates()));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PutMapping("/{id}")
    public CustomerDto update(@PathVariable Long id, @RequestBody CustomerDto dto) {
        validate(dto);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        existing.setName(dto.name());
        existing.setEmail(dto.email());
        existing.setPhone(dto.phone());
        existing.setAddress(dto.address());
        existing.setCoordinates(dto.coordinates());
        return toDto(customerRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validate(CustomerDto dto) {
        if (dto.name() == null || dto.name().isBlank()) throw new IllegalArgumentException("Numele este obligatoriu");
        if (dto.email() == null || !dto.email().contains("@")) throw new IllegalArgumentException("Email invalid");
        if (dto.phone() == null || dto.phone().isBlank()) throw new IllegalArgumentException("Telefonul este obligatoriu");
        if (dto.address() == null || dto.address().isBlank()) throw new IllegalArgumentException("Adresa este obligatorie");
        if (dto.coordinates() == null || dto.coordinates().isBlank()) throw new IllegalArgumentException("Coordonatele sunt obligatorii");
    }

    private CustomerDto toDto(Customer c) {
        return new CustomerDto(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getCoordinates());
    }
}
