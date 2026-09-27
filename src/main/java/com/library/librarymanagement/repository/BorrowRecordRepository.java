package com.library.librarymanagement.repository;

import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByMemberId(Long memberId);

    List<BorrowRecord> findByBookId(Long bookId);

    List<BorrowRecord> findByStatus(BorrowStatus status);

    Optional<BorrowRecord> findByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, BorrowStatus status);
}
