package golo.bookservice.api;

import golo.bookservice.api.dto.Charge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:golo-book-service-db"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerAuthorizedTest {

    @LocalServerPort
    long port;

    @Value("${spring.security.user.name:golo}")
    private String username;

    @Value("${spring.security.user.name:password}")
    private String password;

    @Test
    void controllerAuthorized() throws IOException {

//        Charge charge = RestClient.create().post().uri("http://localhost:" + port + "/books/import")
//                .header("Authorization", getAuthHeader())
//                .body( getFileFromResource("/books/books.csv") )
//                .retrieve().body(Charge.class);

        // Make the request with Basic Authentication header
        Integer integer = RestClient.create().get()
                .uri("http://localhost:" + port + "/books/available-quantity")
                .header("Authorization", getAuthHeader())
                .header("ContentType", String.valueOf(MediaType.MULTIPART_FORM_DATA))
                .retrieve().body(Integer.class);

        Assertions.assertNotNull(integer);
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

    private String getAuthHeader() {
        // Construct Basic Authentication header
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }
}