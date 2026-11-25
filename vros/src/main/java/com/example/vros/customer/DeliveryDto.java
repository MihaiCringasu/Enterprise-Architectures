package com.example.vros.customer;

import java.time.LocalDateTime;

public record DeliveryDto(
        Long id,
        Long customerId,
        Double quantity,
        String notes,
        LocalDateTime timestamp
) {}
