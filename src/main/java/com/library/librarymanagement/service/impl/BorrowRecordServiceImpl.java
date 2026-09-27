package com.library.librarymanagement.service.impl;

import com.library.librarymanagement.exception.BadRequestException;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.model.BorrowStatus;
import com.library.librarymanagement.model.Member;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.BorrowRecordRepository;
import com.library.librarymanagement.repository.MemberRepository;
import com.library.librarymanagement.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public BorrowRecord borrowBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + bookId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id " + memberId));

        if (book.getAvailableCopies() == null || book.getAvailableCopies() <= 0) {
            throw new BadRequestException("No available copies of \"" + book.getTitle() + "\" to borrow");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = BorrowRecord.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS))
                .status(BorrowStatus.BORROWED)
                .build();

        return borrowRecordRepository.save(record);
    }

    @Override
    @Transactional
    public BorrowRecord returnBook(Long bookId, Long memberId) {
        BorrowRecord record = borrowRecordRepository
                .findByBookIdAndMemberIdAndStatus(bookId, memberId, BorrowStatus.BORROWED)
                .orElseThrow(() -> new BadRequestException(
                        "No active borrow record found for this book and member"));

        record.setReturnDate(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);
        borrowRecordRepository.save(record);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return record;
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    @Override
    public List<BorrowRecord> getRecordsByMember(Long memberId) {
        return borrowRecordRepository.findByMemberId(memberId);
    }

    @Override
    public List<BorrowRecord> getRecordsByBook(Long bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }
}
