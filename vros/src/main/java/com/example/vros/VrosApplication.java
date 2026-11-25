package com.example.vros;

import com.example.vros.customer.*;
import com.example.vros.fleet.*;
import com.example.vros.security.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VrosApplication {

    public static void main(String[] args) {
        SpringApplication.run(VrosApplication.class, args);
    }

    @Bean
    public CommandLineRunner testVrosPersistence(DepotService depotService,
                                                 DriverService driverService,
                                                 VehicleService vehicleService,
                                                 CustomerService customerService,
                                                 DeliveryService deliveryService,
                                                 AppUserRepository userRepository,
                                                 PasswordHashService passwordHashService) {
        return args -> {
            System.out.println("=== TESTARE LAB 4 & INIȚIALIZARE DATE VROS ===");

            Depot depot = depotService.save(new Depot("Central Hub", "47.1585,27.6014"));
            Driver d1 = driverService.save(new Driver("Ion Popescu", "ion@vros.ro", "0711111111"));
            Driver d2 = driverService.save(new Driver("Vasile Radu", "vasile@vros.ro", "0722222222"));

            Vehicle v1 = new Vehicle("Ford Transit", 1200.0, depot, true);
            v1.setDriver(d1);
            vehicleService.save(v1);

            Vehicle v2 = new Vehicle("Mercedes Sprinter", 2500.0, depot, true);
            vehicleService.save(v2);

            Customer c1 = customerService.save(new Customer("Kaufland Pacurari", "contact@kaufland.ro", "0733333333", "Sos. Pacurari 92", "47.1740,27.5480"));
            deliveryService.save(new Delivery(c1, 450.0, "Livrare dimineața"));
            deliveryService.save(new Delivery(c1, 300.0, "Marfă perisabilă"));

            System.out.println("Șoferi neasignați (JPQL): " + driverService.findUnassignedDrivers().size());
            System.out.println("Vehicule disponibile >= 1500kg (Derived): " + vehicleService.findAvailableWithCapacity(1500.0).size());
            System.out.println("Cantitate totală comandată de client (JPQL SUM): " + deliveryService.getTotalQuantityForCustomer(c1.getId()));

            vehicleService.setVehicleAvailability(v1.getId(), false);
            System.out.println("Disponibilitate actualizată prin @Modifying query pentru vehiculul: " + v1.getName());

            userRepository.save(new AppUser("admin", passwordHashService.hashPassword("admin123"), Role.ADMIN, null));
            userRepository.save(new AppUser("dispatcher", passwordHashService.hashPassword("disp123"), Role.DISPATCHER, null));
            userRepository.save(new AppUser("ion.popescu", passwordHashService.hashPassword("driver123"), Role.DRIVER, d1));
            System.out.println("Conturi create: admin / admin123, dispatcher / disp123, ion.popescu / driver123");

            System.out.println("=== INIȚIALIZARE FINALIZATĂ CU SUCCES ===");
        };
    }
}
