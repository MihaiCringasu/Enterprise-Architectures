package com.example.lab3.library.repository;

import com.example.lab3.library.model.Member;
import com.example.lab3.library.model.MemberRole;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository {
    private final Map<Long, Member> members = new HashMap<>();

    @PostConstruct
    public void init() {
        members.put(1L, new Member(1L, "Andrei Popescu", MemberRole.STUDENT));
        members.put(2L, new Member(2L, "Elena Ionescu", MemberRole.REGULAR));
        members.put(3L, new Member(3L, "Mihai Georgescu", MemberRole.PREMIUM));
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(members.get(id));
    }
}
