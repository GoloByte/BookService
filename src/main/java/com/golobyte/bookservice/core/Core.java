package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.core.mapping.ChargeMapper;
import com.golobyte.bookservice.data.BookRepository;
import com.golobyte.bookservice.data.ChargeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class Core {
    private final Borrow borrow;
    private final Importer importer;
    private final ChargeRepository chargeRepository;
    private final BookRepository bookRepository;
    private final ChargeMapper chargeMapper;

    @Transactional()
    public Charge importBooks(MultipartFile file) {
        try {
            return importer.importBooks(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    @Transactional()
    public List<Charge> getCharges() {
        return chargeRepository.findAll()
                .stream()
                .map(chargeMapper::map)
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
}
