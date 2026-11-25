package com.example.lab3.library.policy;

import com.example.lab3.library.model.Member;

public interface BorrowingPolicy {
    boolean canBorrow(Member member);
    int getMaxLimit(Member member);
}
