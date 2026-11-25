package com.example.lab3.library.model;

public class Member {
    private Long id;
    private String name;
    private MemberRole role;
    private int borrowedBooksCount;

    public Member(Long id, String name, MemberRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.borrowedBooksCount = 0;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public MemberRole getRole() { return role; }
    public int getBorrowedBooksCount() { return borrowedBooksCount; }
    public void incrementBorrowedCount() { this.borrowedBooksCount++; }
    public void decrementBorrowedCount() { this.borrowedBooksCount--; }
}
