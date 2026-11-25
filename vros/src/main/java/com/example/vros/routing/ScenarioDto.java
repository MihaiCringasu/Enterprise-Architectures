package com.example.vros.routing;

import java.util.Set;

public record ScenarioDto(
        Long id,
        String name,
        Set<Long> vehicleIds,
        Set<Long> deliveryIds
) {}
