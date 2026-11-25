package com.example.vros.customer;

public record CustomerDto(
        Long id,
        String name,
        String email,
        String phone,
        String address,
        String coordinates
) {}
