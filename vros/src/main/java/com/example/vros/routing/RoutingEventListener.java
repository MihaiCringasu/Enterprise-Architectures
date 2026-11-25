package com.example.vros.routing;

import com.example.vros.fleet.VehicleAssignedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RoutingEventListener {

    private final LiveNotificationService notificationService;

    public RoutingEventListener(LiveNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onVehicleAssigned(VehicleAssignedEvent event) {
        notificationService.broadcast("fleet_event",
                "Șoferul " + event.driverId() + " a fost asignat vehiculului " + event.vehicleId());
    }
}
