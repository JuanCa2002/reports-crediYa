package co.com.pragma.consumer.auth;

import co.com.pragma.consumer.auth.dto.ValidateResponseDTO;
import co.com.pragma.consumer.auth.exception.InvalidTokenUnauthorizedException;
import co.com.pragma.consumer.auth.mapper.AuthenticationMapper;
import co.com.pragma.model.authentication.Authentication;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.time.LocalDate;

class AuthenticationRestConsumerTest {

    private static MockWebServer mockBackEnd;

    private AuthenticationRestConsumer authenticationRestConsumer;

    private AuthenticationMapper mapper;

    @BeforeAll
    static void setUpServer() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void init() {
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(mockBackEnd.url("/").toString());

        mapper = Mockito.mock(AuthenticationMapper.class);

        authenticationRestConsumer = new AuthenticationRestConsumer(builder, mapper);

        // setea el campo privado "baseUrl"
        ReflectionTestUtils.setField(authenticationRestConsumer, "baseUrl", mockBackEnd.url("/").toString());
    }

    @Test
    @DisplayName("validateToken returns Authentication when token is valid")
    void validateToken_success() {
        // given
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"userName\":\"JUAN\",\"role\":\"ADMINISTRADOR\",\"expirationDate\":\"2025-08-14\"}")
        );

        ValidateResponseDTO dto = new ValidateResponseDTO();

        Authentication auth = new Authentication("JUAN", "ADMINISTRADOR", "2025-08-14");

        Mockito.when(mapper.toDomain(Mockito.any(ValidateResponseDTO.class)))
                .thenReturn(auth);

        // when
        var response = authenticationRestConsumer.validateToken("myToken");

        // then
        StepVerifier.create(response)
                .expectNext(auth)
                .verifyComplete();
    }

    @Test
    @DisplayName("validateToken throws InvalidTokenUnauthorizedException on 401")
    void validateToken_invalidToken() {
        // given
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(401)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"messages\":[\"Invalid token\"]}")
        );

        // when
        var response = authenticationRestConsumer.validateToken("badToken");

        // then
        StepVerifier.create(response)
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidTokenUnauthorizedException &&
                                throwable.getMessage().equals("Invalid token"))
                .verify();
    }

    @Test
    @DisplayName("validateToken throws RuntimeException on 500")
    void validateToken_serverError() {
        // given
        mockBackEnd.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"messages\":[\"Internal error\"]}")
        );

        // when
        var response = authenticationRestConsumer.validateToken("anyToken");

        // then
        StepVerifier.create(response)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Internal error"))
                .verify();
    }
}
