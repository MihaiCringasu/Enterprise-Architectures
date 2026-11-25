package com.example.lab3.library.aspect;

import com.example.lab3.library.exception.BorrowingException;
import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.policy.BorrowingPolicy;
import com.example.lab3.library.repository.BookRepository;
import com.example.lab3.library.repository.MemberRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LibraryAspects {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowingPolicy borrowingPolicy;

    public LibraryAspects(MemberRepository memberRepository,
                          BookRepository bookRepository,
                          BorrowingPolicy borrowingPolicy) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.borrowingPolicy = borrowingPolicy;
    }

    @Before("execution(* com.example.lab3.library.service.BorrowingService.borrowBook(..)) && args(memberId, bookId)")
    public void validateBeforeBorrow(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BorrowingException("Validare eșuată: Membrul cu ID " + memberId + " nu există."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BorrowingException("Validare eșuată: Cartea cu ID " + bookId + " nu există."));

        if (!book.isAvailable()) {
            throw new BorrowingException("Validare eșuată: Cartea '" + book.getTitle() + "' este deja împrumutată.");
        }

        if (!borrowingPolicy.canBorrow(member)) {
            throw new BorrowingException("Validare eșuată: Membrul " + member.getName() +
                    " a atins limita maximă de " + borrowingPolicy.getMaxLimit(member) + " cărți.");
        }
    }

    @Around("execution(* com.example.lab3.library.service.BorrowingService.*(..)) && args(memberId, bookId)")
    public Object logExecutionDetails(ProceedingJoinPoint joinPoint, Long memberId, Long bookId) throws Throwable {
        long startTime = System.nanoTime();

        String methodName = joinPoint.getSignature().getName();
        String memberName = memberRepository.findById(memberId).map(Member::getName).orElse("Necunoscut");
        String bookTitle = bookRepository.findById(bookId).map(Book::getTitle).orElse("Necunoscută");

        Object result = joinPoint.proceed();

        long durationMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("[AOP AUDIT] Metodă: %s | Membru: %s | Carte: '%s' | Timp execuție: %d ms%n",
                methodName, memberName, bookTitle, durationMs);

        return result;
    }

    @AfterThrowing(
            pointcut = "execution(* com.example.lab3.library.service.BorrowingService.*(..))",
            throwing = "ex"
    )
    public void logBorrowingException(JoinPoint joinPoint, Exception ex) {
        String failedOperation = joinPoint.getSignature().getName();
        System.err.printf("[AOP ERROR] Operația '%s' a eșuat! Motiv: %s%n", failedOperation, ex.getMessage());
    }
}
