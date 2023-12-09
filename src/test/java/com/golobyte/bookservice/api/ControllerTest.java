package com.golobyte.bookservice.api;

import com.golobyte.bookservice.api.dto.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:golo-book-service-db"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerTest {

    @LocalServerPort
    long port;

    @Test
    void controllerTest() throws IOException {
        MultiValueMap<String, HttpEntity<?>> file = getResourceFileAsMultiValueMap();

        // POST
        Charge charge = RestClient.create()
                .post().uri("http://localhost:" + port + "/books/import")
                .body(file)
                .retrieve()
                .body(Charge.class);
        assertThat(Objects.requireNonNull(charge).getImported()).isEqualTo(20);

        // GET
        Integer availableQuantity = RestClient.create()
                .get()
                .uri("http://localhost:" + port + "/books/available-quantity")
                .retrieve().body(Integer.class);
        assertThat(availableQuantity).isEqualTo(20);
    }

    private static MultiValueMap<String, HttpEntity<?>> getResourceFileAsMultiValueMap() throws IOException {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new FileSystemResource(ResourceUtils.getFile(new ClassPathResource("books/books.csv").getURL())));
        return multipartBodyBuilder.build();
    }
}