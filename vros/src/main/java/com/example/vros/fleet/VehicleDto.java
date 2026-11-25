package com.example.vros.fleet;

public record VehicleDto(
        Long id,
        String name,
        Double capacity,
        Boolean available
) {}
