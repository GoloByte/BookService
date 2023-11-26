package golo.bookservice.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:mem:golo-book-service-db"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerUnauthorizedTest {

    @LocalServerPort
    long port;

    @Test
    void controllerUnauthorized() {
        assertThatThrownBy(() -> RestClient.create().get()
                .uri("http://localhost:" + port + "/books/available-quantity")
                .retrieve()
                .body(Integer.class))
                .isInstanceOf(HttpClientErrorException.Unauthorized.class);
    }
}