package co.com.pragma.api.config;

import co.com.pragma.api.SecurityTestConfig;
import co.com.pragma.api.metric.MetricHandler;
import co.com.pragma.api.metric.MetricRouterRest;
import co.com.pragma.api.metric.config.MetricPath;
import co.com.pragma.api.metric.dto.MetricResponseDTO;
import co.com.pragma.api.metric.mapper.MetricMapper;
import co.com.pragma.model.metric.Metric;
import co.com.pragma.usecase.metric.MetricUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MetricRouterRest.class, MetricHandler.class, MetricPath.class, BasePath.class})
@TestPropertySource(properties = "routes.base-path=/api/v1")
@TestPropertySource(properties = "routes.paths.reports=/reportes")
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, SecurityTestConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MetricUseCase metricUseCase;

    @MockitoBean
    private MetricMapper metricMapper;

    private final Metric domain = Metric.builder()
            .id("my-id")
            .proposalApprovedQuantity(1L)
            .totalApprovedAmount(10000.0)
            .build();

    private final MetricResponseDTO response = MetricResponseDTO.builder()
            .proposalApprovedQuantity(1L)
            .totalApprovedAmount(10000.0)
            .build();

    @BeforeEach
    void setUp() {
        when(metricMapper.toResponse(Mockito.any(Metric.class))).thenReturn(response);
        when(metricUseCase.getReport()).thenReturn(Mono.just(domain));
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.get()
                .uri("/api/v1/reportes")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin")
                .expectBody(MetricResponseDTO.class);
    }

}