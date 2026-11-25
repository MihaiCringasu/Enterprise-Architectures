package com.example.vros.customer;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    public CustomerFacade(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(c -> new CustomerDto(c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress(), c.getCoordinates()))
                .toList();
    }
}
