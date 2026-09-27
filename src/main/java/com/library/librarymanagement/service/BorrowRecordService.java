package com.library.librarymanagement.service;

import com.library.librarymanagement.model.BorrowRecord;

import java.util.List;

public interface BorrowRecordService {
    BorrowRecord borrowBook(Long bookId, Long memberId);
    BorrowRecord returnBook(Long bookId, Long memberId);
    List<BorrowRecord> getAllBorrowRecords();
    List<BorrowRecord> getRecordsByMember(Long memberId);
    List<BorrowRecord> getRecordsByBook(Long bookId);
}
