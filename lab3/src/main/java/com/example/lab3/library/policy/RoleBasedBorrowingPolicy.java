package com.example.lab3.library.policy;

import com.example.lab3.library.model.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "library.borrowing.policy", havingValue = "role-based")
public class RoleBasedBorrowingPolicy implements BorrowingPolicy {

    @Override
    public boolean canBorrow(Member member) {
        return member.getBorrowedBooksCount() < getMaxLimit(member);
    }

    @Override
    public int getMaxLimit(Member member) {
        return switch (member.getRole()) {
            case STUDENT -> 3;
            case REGULAR -> 2;
            case PREMIUM -> 5;
        };
    }
}
