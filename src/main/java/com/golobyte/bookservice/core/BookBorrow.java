package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.core.mapping.BookMapper;
import com.golobyte.bookservice.data.BookRepository;
import com.golobyte.bookservice.data.entity.BookEo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class BookBorrow {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<Book> borrow(int numberOfBooks) {

        List<BookEo> booksEoFound = bookRepository.borrow(numberOfBooks);

        if (booksEoFound.size() < numberOfBooks) {
            throw new IllegalStateException("Number of books is not available");
        }

        List<BookEo> booksForBorrow = booksEoFound.stream()
                .peek(booksEo -> {
                    booksEo.setBorrowed(true);
                }).toList();

        List<BookEo> borrowedBooks = bookRepository.saveAll(booksForBorrow);

        return borrowedBooks
                .stream()
                .map(bookMapper::map)
                .toList();

    }
}
