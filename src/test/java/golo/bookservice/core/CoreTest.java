package golo.bookservice.core;

import feign.FeignException;
import golo.bookservice.api.dto.Book;
import golo.bookservice.api.dto.Charge;
import golo.bookservice.data.BookRepository;
import golo.bookservice.data.ChargeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
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
    @Autowired private BookRepository bookRepository;
    @Autowired private ChargeRepository chargeRepository;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        chargeRepository.deleteAll();
    }
    @Test
    @Transactional
    void importBooks() throws Exception {
        MultipartFile file =  getFileFromResource("/books/books.csv");
        Charge charge = core.importBooks(file);

        assertThat(charge.getImported()).isEqualTo(20);
        assertThat(charge.getTotal()).isEqualTo(20);
        assertThat(charge.getTimestamp()).isNotNull();

        assertThat(core.getAvailableQuantity()).isEqualTo(20);

        List<Book> books = core.lendOut(2);
        assertThat(books).hasSize(2);

        assertThat(core.getAvailableQuantity()).isEqualTo(18);

        assertThatThrownBy(() ->core.lendOut(19)).isInstanceOf(IllegalStateException.class);

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