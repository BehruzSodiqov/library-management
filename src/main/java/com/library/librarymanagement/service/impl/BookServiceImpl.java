package com.library.librarymanagement.service.impl;

import com.library.librarymanagement.exception.BadRequestException;
import com.library.librarymanagement.exception.ResourceNotFoundException;
import com.library.librarymanagement.model.Book;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BadRequestException("A book with ISBN " + book.getIsbn() + " already exists");
        }
        // Keep availableCopies in sync with totalCopies on creation
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
    }

    @Override
    public Book updateBook(Long id, Book updated) {
        Book existing = getBookById(id);

        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setGenre(updated.getGenre());
        existing.setPublishedYear(updated.getPublishedYear());

        // Adjust availableCopies proportionally if totalCopies changes
        if (updated.getTotalCopies() != null) {
            int borrowed = existing.getTotalCopies() - existing.getAvailableCopies();
            existing.setTotalCopies(updated.getTotalCopies());
            existing.setAvailableCopies(Math.max(0, updated.getTotalCopies() - borrowed));
        }

        return bookRepository.save(existing);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
