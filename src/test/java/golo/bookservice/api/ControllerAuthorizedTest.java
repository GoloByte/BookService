package golo.bookservice.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import java.util.Base64;

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
        // Construct Basic Authentication header
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);

        // Make the request with Basic Authentication header
        Integer integer = RestClient.create().get()
                .uri("http://localhost:" + port + "/books/available-quantity")
                .header("Authorization", authHeader)
                .retrieve().body(Integer.class);

        Assertions.assertNotNull(integer);
    }
}