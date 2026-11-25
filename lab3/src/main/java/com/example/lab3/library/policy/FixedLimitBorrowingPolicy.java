package com.example.lab3.library.policy;

import com.example.lab3.library.model.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "library.borrowing.policy", havingValue = "fixed", matchIfMissing = true)
public class FixedLimitBorrowingPolicy implements BorrowingPolicy {

    private static final int FIXED_LIMIT = 2;

    @Override
    public boolean canBorrow(Member member) {
        return member.getBorrowedBooksCount() < FIXED_LIMIT;
    }

    @Override
    public int getMaxLimit(Member member) {
        return FIXED_LIMIT;
    }
}
