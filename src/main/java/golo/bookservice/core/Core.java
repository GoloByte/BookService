package golo.bookservice.core;

import golo.bookservice.api.dto.Book;
import golo.bookservice.api.dto.Charge;
import golo.bookservice.core.mapping.ChargeMapper;
import golo.bookservice.data.BookRepository;
import golo.bookservice.data.ChargeRepository;
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
    private final Lend lend;
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
    public long getAvailableQuantity() {
        return bookRepository.getAvailableQuantity();
    }

    @Transactional()
    public List<Book> lendOut(int quantity) {
        return lend.lendOut(quantity);
    }
}
