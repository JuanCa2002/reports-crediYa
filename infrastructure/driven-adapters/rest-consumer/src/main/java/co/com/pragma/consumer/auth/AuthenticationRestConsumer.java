package co.com.pragma.consumer.auth;

import co.com.pragma.consumer.auth.dto.ErrorResponse;
import co.com.pragma.consumer.auth.dto.ValidateResponseDTO;
import co.com.pragma.consumer.auth.exception.InvalidTokenUnauthorizedException;
import co.com.pragma.consumer.auth.mapper.AuthenticationMapper;
import co.com.pragma.model.authentication.Authentication;
import co.com.pragma.model.authentication.gateways.AuthenticationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationRestConsumer implements AuthenticationRepository {

    @Value("${adapter.restconsumer.clients.auth}")
    private String baseUrl;
    private final WebClient.Builder webClientBuilder;
    private final AuthenticationMapper mapper;

    @CircuitBreaker(name = "validateToken")
    @Override
    public Mono<Authentication> validateToken(String token) {
        log.info("[AuthenticationRestConsumer] Starting request to external Auth service to validate token: {}", token);

        WebClient client = webClientBuilder
                .baseUrl(baseUrl)
                .build();

        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/validate")
                        .queryParam("token", token)
                        .build())
                .bodyValue(Collections.singletonMap("token", token))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.warn("[AuthenticationRestConsumer] 4xx error occurred while validating token: {}", token);
                    return response.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> {
                                log.error("[AuthenticationRestConsumer] Invalid token: {}", error.getMessages());
                                return Mono.error(new InvalidTokenUnauthorizedException(error.getMessages().get(0)));
                            });
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("[AuthenticationRestConsumer] 5xx error occurred while validating token: {}", token);
                    return response.bodyToMono(ErrorResponse.class)
                            .flatMap(error -> {
                                log.error("[AuthenticationRestConsumer] Server error: {}", error.getMessages());
                                return Mono.error(new RuntimeException(error.getMessages().get(0)));
                            });
                })
                .bodyToMono(ValidateResponseDTO.class)
                .map(mapper::toDomain)
                .doOnSuccess(response -> log.info("[AuthenticationRestConsumer] validating token, with response {}", response))
                .doOnError(e -> log.error("[AuthenticationRestConsumer] error while validating token, with error", e));
    }
}
