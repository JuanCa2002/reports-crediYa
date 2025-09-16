package co.com.pragma.usecase.metric;

import co.com.pragma.model.metric.Metric;
import co.com.pragma.model.metric.gateways.MetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MetricUseCaseTest {

    @InjectMocks
    MetricUseCase metricUseCase;

    @Mock
    MetricRepository repository;

    private final Metric metric = Metric.builder()
            .id("someId")
            .proposalApprovedQuantity(0L)
            .totalApprovedAmount(0.0)
            .build();

    @Test
    void shouldGetReport() {
       when(repository.getReport())
               .thenReturn(Mono.just(metric));

       var result = metricUseCase.getReport();

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getId().equals(metric.getId())
                )
                .verifyComplete();

    }
}
