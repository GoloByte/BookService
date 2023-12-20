package com.golobyte.bookservice.core;

import com.golobyte.bookservice.api.dto.Author;
import com.golobyte.bookservice.api.dto.Book;
import com.golobyte.bookservice.api.dto.Charge;
import com.golobyte.bookservice.core.mapping.ChargeMapper;
import com.golobyte.bookservice.data.BookRepository;
import com.golobyte.bookservice.data.ChargeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:golo-book-service-db"})
@SpringBootTest
class CoreTest {
    @Autowired private Core core;
    @Autowired
    private ChargeMapper chargeMapper;
    @Autowired private BookRepository bookRepository;
    @Autowired private ChargeRepository chargeRepository;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        chargeRepository.deleteAll();
    }

    @Test
    @Transactional
    void getCharges() {
        assertThat(core.getCharges()).hasSize(0);
    }

    @Test
    @Transactional
    void importBooks() throws Exception {
        assertThat(core.getCharges()).hasSize(0);

        // GIVEN: source of 5 books
        String booksSource = "/books/5books.csv";
        MultipartFile file = getFileFromResource(booksSource);
        // WHEN: import of the book source
        Charge charge = core.importBooks(file);

        // THEN: number of imported books should be 5
        assertThat(charge.getImportedCount()).isEqualTo(5);
        assertThat(charge.getImportedOn()).isNotNull();
        assertThat(core.getNumberOfAvailableBooks()).isEqualTo(5);

        // WHEN: borrow 2 books
        List<Book> books = core.borrow(2);
        // THEN: number of borrowed books should be 2, books are marked as borrowed
        assertThat(books).hasSize(2);
        assertThat(books.getFirst().getName()).isNotEmpty();
        assertThat(books.getFirst().isBorrowed()).isTrue();
        // THEN: number of available books should be 3
        assertThat(core.getNumberOfAvailableBooks()).isEqualTo(3);

        // WHEN: borrow 4 books, exception should be thrown
        assertThatThrownBy(() -> core.borrow(4)).isInstanceOf(IllegalStateException.class);

        Charge chargeLoaded = core.getCharges().getFirst();
        assertThat(chargeLoaded.getBooks()).hasSize(5);

        List<Author> authors = core.getAuthors();
        assertThat(authors).hasSize(3);

    }

    private static MultipartFile getFileFromResource(String path) throws IOException {
        File file = ResourceUtils.getFile(new ClassPathResource(path).getURL());

        byte[] fileBytes;
        try (FileInputStream fis = new FileInputStream(file)) {
            fileBytes = new byte[fis.available()];
            fis.read(fileBytes);
        }

        return new MockMultipartFile("file",
                file.getName(), "text/plain", fileBytes);
    }
}