package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.dto.Author;
import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.core.mapping.AuthorMapper;
import com.golobyte.bookservice.core.mapping.BookMapper;
import com.golobyte.bookservice.core.mapping.ChargeMapper;
import com.golobyte.bookservice.data.AuthorRepository;
import com.golobyte.bookservice.data.BookRepository;
import com.golobyte.bookservice.data.ChargeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@AllArgsConstructor
public class Core {
    private final Borrow borrow;
    private final Importer importer;

    private final ChargeRepository chargeRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    private final ChargeMapper chargeMapper;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    public Charge importBooks(MultipartFile file) {
        try {
            return importer.importBooks(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Async
    public CompletableFuture<Charge> importBooksAsync(InputStream inputStream) {
        log.info("CompletableFuture.supplyAsync");
        return CompletableFuture.supplyAsync(() -> {
            log.info("CompletableFuture.supplyAsync -> importer.importBooks");
            return importer.importBooks(inputStream);
        });
    }

    @Transactional()
    public List<Charge> getCharges() {
        return chargeRepository.findAll()
                .stream()
                .map(chargeMapper::map)
                .toList();
    }

    @Transactional()
    public List<Author> getAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::map)
                .toList();
    }



    @Transactional()
    public long getNumberOfAvailableBooks() {
        return bookRepository.getNumberOfAvailableBooks();
    }

    @Transactional()
    public List<Book> borrow(int number) {
        return borrow.borrow(number);
    }

    @Transactional()
    public List<Book> getBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::map)
                .toList();
    }
}
