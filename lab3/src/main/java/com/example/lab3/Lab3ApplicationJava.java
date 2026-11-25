package com.example.lab3;

import com.example.lab3.library.service.BorrowingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab3Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab3Application.class, args);
    }

    @Bean
    public CommandLineRunner testLibrarySystem(BorrowingService borrowingService) {
        return args -> {
            System.out.println("=== ÎNCEPERE TESTARE LIBRARY MANAGEMENT SYSTEM ===");

            // 1. Andrei (STUDENT - limită 3 pe role-based) împrumută 3 cărți
            borrowingService.borrowBook(1L, 101L); // 1 carte
            borrowingService.borrowBook(1L, 102L); // 2 cărți
            borrowingService.borrowBook(1L, 103L); // 3 cărți -> Declanșează Event (> 2)!

            // 2. Încercare depășire limită pentru Andrei (a 4-a carte) -> Aruncă BorrowingException
            try {
                borrowingService.borrowBook(1L, 104L);
            } catch (Exception ignored) {}

            // 3. Returnare o carte și împrumutare de către Elena (REGULAR)
            borrowingService.returnBook(1L, 101L);
            borrowingService.borrowBook(2L, 101L);

            // 4. Încercare împrumut carte deja luată -> Aruncă BorrowingException
            try {
                borrowingService.borrowBook(3L, 101L);
            } catch (Exception ignored) {}

            System.out.println("=== TESTARE FINALIZATĂ ===");
        };
    }
}
