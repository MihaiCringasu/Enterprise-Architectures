package com.example.lab3.library.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class HighBorrowingListener {

    @EventListener
    public void handleHighBorrowing(HighBorrowingActivityEvent event) {
        System.out.println("[EVENT ALERT] Membrul " + event.getMember().getName() +
                " a depășit pragul de împrumuturi! Total cărți împrumutate acum: " +
                event.getMember().getBorrowedBooksCount());
    }
}
