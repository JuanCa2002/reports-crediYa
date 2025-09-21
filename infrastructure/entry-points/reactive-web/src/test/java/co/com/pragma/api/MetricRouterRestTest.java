package co.com.pragma.api;

import co.com.pragma.api.config.BasePath;
import co.com.pragma.api.metric.MetricHandler;
import co.com.pragma.api.metric.MetricRouterRest;
import co.com.pragma.api.metric.config.MetricPath;
import co.com.pragma.api.metric.dto.MetricResponseDTO;
import co.com.pragma.api.metric.mapper.MetricMapper;
import co.com.pragma.model.metric.Metric;
import co.com.pragma.usecase.metric.MetricUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MetricRouterRest.class, MetricHandler.class})
@EnableConfigurationProperties({MetricPath.class, BasePath.class})
@TestPropertySource(properties = "routes.base-path=/api/v1")
@TestPropertySource(properties = "routes.paths.reports=/reportes")
@Import(SecurityTestConfig.class)
@WebFluxTest
class MetricRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MetricUseCase metricUseCase;

    @MockitoBean
    private MetricMapper mapper;

    private final String reports = "/reportes";
    private final String mainPath = "/api/v1";

    private final Metric domain = Metric.builder()
            .id("my-id")
            .proposalApprovedQuantity(1L)
            .totalApprovedAmount(10000.0)
            .build();

    private final MetricResponseDTO response = MetricResponseDTO.builder()
            .proposalApprovedQuantity(1L)
            .totalApprovedAmount(10000.0)
            .build();

    @Autowired
    private MetricPath metricPath;

    @Autowired
    private BasePath basePath;

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals("/reportes", metricPath.getReports());
        assertEquals("/api/v1", basePath.getBasePath());
    }

    @Test
    void listenGetReport() {
        when(mapper.toResponse(Mockito.any(Metric.class))).thenReturn(response);
        when(metricUseCase.getReport()).thenReturn(Mono.just(domain));

        webTestClient.get()
                .uri(mainPath + reports)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MetricResponseDTO.class)
                .value(report -> {
                    Assertions.assertThat(report.getProposalApprovedQuantity())
                            .isEqualTo(response.getProposalApprovedQuantity());
                    Assertions.assertThat(report.getTotalApprovedAmount())
                            .isEqualTo(response.getTotalApprovedAmount());
                });
    }
}
