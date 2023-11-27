package golo.bookservice.core;

import golo.bookservice.api.dto.Charge;
import golo.bookservice.core.mapping.ChargeMapper;
import golo.bookservice.data.BookRepository;
import golo.bookservice.data.ChargeRepository;
import golo.bookservice.data.entity.BookEo;
import golo.bookservice.data.entity.ChargeEo;
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
public class Importer {

    private final BookRepository bookRepository;
    private final ChargeRepository chargeRepository;
    private final ChargeMapper chargeMapper;

    public Charge importBooks(InputStream inputStream) {
        log.info("import started ...");
        Instant importTimeStamp = Instant.now();
        Set<String> bookNames = new HashSet<>();
        long total = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean skipColumnHead = true;
            while ((line = br.readLine()) != null) {
                if (skipColumnHead) {
                    skipColumnHead = false;
                    continue;
                }
                try (Scanner rowScanner = new Scanner(line)) {
                    rowScanner.useDelimiter(";");
                    int column = 0;
                    while (rowScanner.hasNext()) {
                        column++;
                        if (column == 1) {
                            String bookName = rowScanner.next();
                            total++;

                            boolean contains = bookNames.contains(bookName);
                            if (!contains) {
                                if (!this.bookRepository.isBookExists(bookName)) {
                                    bookNames.add(bookName);
                                }
                            }
                        } else {
                            rowScanner.next();
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        ChargeEo chargeEo = new ChargeEo();
        chargeEo.setTimestamp(importTimeStamp);
        final ChargeEo chargeEoSaved = chargeRepository.save(chargeEo);

        List<String> strings = new ArrayList<>(bookNames);
        List<BookEo> bookEoList = strings.stream().map(s -> BookEo.builder()
                        .name(s)
                        .chargeEo(chargeEoSaved)
                        .build())
                .toList();
        List<BookEo> bookEoListSaved = bookRepository.saveAll(bookEoList);

        chargeEoSaved.setTotal(total);
        chargeEoSaved.setImported(bookEoListSaved.size());
        chargeRepository.save(chargeEoSaved);


        return chargeMapper.map(chargeEoSaved);
    }
}