package com.example.vros.routing;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
public class RoutingController {

    private final RoutingOptimizationService optimizationService;
    private final LiveNotificationService notificationService;

    public RoutingController(RoutingOptimizationService optimizationService,
                             LiveNotificationService notificationService) {
        this.optimizationService = optimizationService;
        this.notificationService = notificationService;
    }

    @PostMapping("/api/scenarios/{id}/optimize")
    public List<RouteDto> optimize(@PathVariable Long id) {
        return optimizationService.optimizeScenario(id);
    }

    @GetMapping("/api/scenarios/{id}/routes")
    public List<RouteDto> getRoutes(@PathVariable Long id) {
        return optimizationService.getRoutesForScenario(id);
    }

    @GetMapping(value = "/api/routes/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamRoutes() {
        return notificationService.subscribe();
    }
}
