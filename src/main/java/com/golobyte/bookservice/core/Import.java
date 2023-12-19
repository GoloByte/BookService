package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.core.mapping.ChargeMapper;
import com.golobyte.bookservice.data.AuthorRepository;
import com.golobyte.bookservice.data.BookRepository;
import com.golobyte.bookservice.data.ChargeRepository;
import com.golobyte.bookservice.data.entity.AuthorEo;
import com.golobyte.bookservice.data.entity.BookEo;
import com.golobyte.bookservice.data.entity.ChargeEo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
@Transactional
public class Import {

    private final BookRepository bookRepository;
    private final ChargeRepository chargeRepository;
    private final AuthorRepository authorRepository;
    private final ChargeMapper chargeMapper;

    public Charge importBooks(InputStream inputStream) {
        log.info("import started ...");
        Instant importedOn = Instant.now();
//        Set<String> bookNames = new HashSet<>();
        Map<String, List<String>> books = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean skip = true;
            while ((line = br.readLine()) != null) {
                if (skip) {
                    skip = false;
                    continue;
                }
                try (Scanner rowScanner = new Scanner(line)) {
                    rowScanner.useDelimiter(";");
                    int column = 0;
                    String bookName = null;
                    while (rowScanner.hasNext()) {
                        column++;
                        if (column == 1) {
                            bookName = rowScanner.next();

                            boolean contains = books.containsKey(bookName);
                            if (!contains && !this.bookRepository.isBookExists(bookName)) {
                                books.put(bookName, new ArrayList<>());
                            }
                        } else if (column == 2) {
                            String authors = rowScanner.next();
                            String finalBookName = bookName;
                            Arrays.stream(authors.split(",")).toList().forEach(authorName -> {
                                books.get(finalBookName).add(authorName);
                            });
                        } else {
                            rowScanner.next();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        ChargeEo chargeEo = createChargeWithBooks(importedOn, books);
        // when  save the ChargeEo entity,
        // the BookEo entities within it will be saved automatically due to the cascading configuration in ChargeEo
        ChargeEo chargeEoSaved = chargeRepository.save(chargeEo);

        // return the charge mapped to the dto
        return chargeMapper.map(chargeEoSaved);
    }

    private ChargeEo createChargeWithBooks(Instant importedOn, Map<String, List<String>> books) {
        ChargeEo chargeEo = new ChargeEo();
        chargeEo.setImportedOn(importedOn);
        chargeEo.setBooks(new ArrayList<>());

        List<String> bookNames = new ArrayList<>(books.keySet());
        Map<String, AuthorEo> createdAuthors = new HashMap<>();

        // iterate the book list, create EO for each and add it to charge
        bookNames.forEach(bookName -> {
            ArrayList<AuthorEo> authors = new ArrayList<>();
            books.get(bookName).forEach(authorName -> {
                // retrieve from database
                AuthorEo authorEo = authorRepository.findByName(authorName);
                if (authorEo == null) {
                    // retrieve from map of created in this run
                    authorEo = createdAuthors.get(authorName);
                    if (authorEo == null) {
                        // create new one and put in map of created
                        createdAuthors.put(authorName, AuthorEo.builder()
                                .name(authorName)
                                .build());
                    }
                }
                authors.add(authorEo);
            });

            BookEo bookEo = BookEo.builder()
                    .name(bookName)
                    .chargeEo(chargeEo)
                    .authors(authors)
                    .build();
            // add BookEo instances to the List<BookEo> in ChargeEo.
            // This ensures the relationship is set up correctly on both sides.
            chargeEo.getBooks().add(bookEo);
        });

        chargeEo.setImportedCount(bookNames.size());
        return chargeEo;
    }
}