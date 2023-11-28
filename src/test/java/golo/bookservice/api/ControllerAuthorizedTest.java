package golo.bookservice.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

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
    void controllerAuthorized() {
        // Make the request with Basic Authentication header
        Integer integer = RestClient.create().get()
                .uri("http://localhost:" + port + "/books/available-quantity")
                .header("Authorization", getAuthHeader())
                .retrieve().body(Integer.class);

        Assertions.assertNotNull(integer);
    }



    private String getAuthHeader() {
        // Construct Basic Authentication header
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        return "Basic " + new String(encodedAuth);
    }
}