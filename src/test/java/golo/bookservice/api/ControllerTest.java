package golo.bookservice.api;

import feign.RequestInterceptor;
import golo.bookservice.api.dto.Charge;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:golo-book-service-db"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ControllerTest.TestConfig.class)
class ControllerTest {

    @LocalServerPort
    long port;

    //    @Value("${spring.security.user.name:golo}")
    private static String username = "golo";

    //    @Value("${spring.security.user.name:password:golo}")
    private static String password = "golo";

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RequestInterceptor requestInterceptor() {
            return template -> {
                // use given test token for testing purposes
                template.header(getAuthHeader());
            };
        }
    }

    @Test
    void controllerTest() throws IOException {
        MultiValueMap<String, HttpEntity<?>> file = getResourceFileAsMultiValueMap();

        // POST
        Charge charge = RestClient.create()
                .post()
                .uri("http://localhost:" + port + "/books/import")
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

    private static String getAuthHeader() {
        // Construct Basic Authentication header
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }
}