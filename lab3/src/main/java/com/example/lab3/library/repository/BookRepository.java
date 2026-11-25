package com.example.lab3.library.repository;

import com.example.lab3.library.model.Book;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BookRepository {
    private final Map<Long, Book> books = new HashMap<>();

    @PostConstruct
    public void init() {
        books.put(101L, new Book(101L, "Clean Code"));
        books.put(102L, new Book(102L, "Design Patterns"));
        books.put(103L, new Book(103L, "Spring in Action"));
        books.put(104L, new Book(104L, "Effective Java"));
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }
}
