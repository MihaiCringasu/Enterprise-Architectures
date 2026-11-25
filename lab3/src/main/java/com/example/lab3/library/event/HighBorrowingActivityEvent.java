package com.example.lab3.library.event;

import com.example.lab3.library.model.Member;
import org.springframework.context.ApplicationEvent;

public class HighBorrowingActivityEvent extends ApplicationEvent {
    private final Member member;

    public HighBorrowingActivityEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
