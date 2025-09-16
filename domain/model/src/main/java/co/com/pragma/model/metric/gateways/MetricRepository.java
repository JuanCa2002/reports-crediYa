package co.com.pragma.model.metric.gateways;

import co.com.pragma.model.metric.Metric;
import reactor.core.publisher.Mono;

public interface MetricRepository {

    Mono<Metric> getReport();
    Mono<Void> updateMetric(Metric metric);
}
