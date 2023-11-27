package golo.bookservice.core;

import golo.bookservice.api.dto.Book;
import golo.bookservice.core.mapping.BookMapper;
import golo.bookservice.data.BookRepository;
import golo.bookservice.data.entity.BookEo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class Lend {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<Book> lendOut(int quantity) {
        Instant lendOn = Instant.now();

        List<BookEo> booksEoFound = bookRepository.lendOut(quantity);

        if (booksEoFound.size() < quantity) {
            throw new IllegalStateException("Quantity is not available");
        }

        List<BookEo> booksEoToUpdate = booksEoFound.stream()
                .peek(booksEo -> {
                    booksEo.setLendOut(true);
                }).toList();

        List<BookEo> booksUpdated = bookRepository.saveAll(booksEoToUpdate);

        return booksUpdated
                .stream()
                .map(bookMapper::map)
                .toList();

    }
}
