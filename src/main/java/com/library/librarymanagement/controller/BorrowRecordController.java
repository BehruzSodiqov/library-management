package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.BorrowRequest;
import com.library.librarymanagement.dto.ReturnRequest;
import com.library.librarymanagement.model.BorrowRecord;
import com.library.librarymanagement.service.BorrowRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
@RequiredArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowBook(@Valid @RequestBody BorrowRequest request) {
        BorrowRecord record = borrowRecordService.borrowBook(request.getBookId(), request.getMemberId());
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @PostMapping("/return")
    public ResponseEntity<BorrowRecord> returnBook(@Valid @RequestBody ReturnRequest request) {
        BorrowRecord record = borrowRecordService.returnBook(request.getBookId(), request.getMemberId());
        return ResponseEntity.ok(record);
    }

    @GetMapping
    public ResponseEntity<List<BorrowRecord>> getAllRecords() {
        return ResponseEntity.ok(borrowRecordService.getAllBorrowRecords());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BorrowRecord>> getRecordsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowRecordService.getRecordsByMember(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BorrowRecord>> getRecordsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowRecordService.getRecordsByBook(bookId));
    }
}
