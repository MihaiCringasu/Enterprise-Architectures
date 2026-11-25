package com.example.lab3.library.service;

import com.example.lab3.library.event.HighBorrowingActivityEvent;
import com.example.lab3.library.exception.BorrowingException;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.policy.BorrowingPolicy;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BorrowingService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;
    private final ApplicationEventPublisher eventPublisher;
    private final int eventThreshold;

    public BorrowingService(MemberRepository memberRepository,
                            BookRepository bookRepository,
                            BorrowingPolicy borrowingPolicy,
                            ApplicationEventPublisher eventPublisher,
                            @Value("${library.borrowing.event-threshold:2}") int eventThreshold) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
        this.eventPublisher = eventPublisher;
        this.eventThreshold = eventThreshold;
        System.out.println(">>> BorrowingService inițializat cu politica: " + borrowingPolicy.getClass().getSimpleName());
    }

    public void borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        book.setAvailable(false);
        member.incrementBorrowedCount();

        if (member.getBorrowedBooksCount() > eventThreshold) {
            eventPublisher.publishEvent(new HighBorrowingActivityEvent(this, member));
        }
    }

    public void returnBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BorrowingException("Membrul nu există: " + memberId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BorrowingException("Cartea nu există: " + bookId));

        if (book.isAvailable()) {
            throw new BorrowingException("Cartea '" + book.getTitle() + "' nu este împrumutată!");
        }

        book.setAvailable(true);
        member.decrementBorrowedCount();
    }
}
