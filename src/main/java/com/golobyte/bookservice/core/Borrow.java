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
public class Borrow {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<Book> borrow(int quantity) {

        List<BookEo> booksEoFound = bookRepository.borrow(quantity);

        if (booksEoFound.size() < quantity) {
            throw new IllegalStateException("Quantity is not available");
        }

        List<BookEo> booksEoToUpdate = booksEoFound.stream()
                .peek(booksEo -> {
                    booksEo.setBorrowed(true);
                }).toList();

        List<BookEo> booksUpdated = bookRepository.saveAll(booksEoToUpdate);

        return booksUpdated
                .stream()
                .map(bookMapper::map)
                .toList();

    }
}
