package co.com.pragma.model.report.gateways;

import co.com.pragma.model.metric.Metric;
import reactor.core.publisher.Mono;

public interface ReportRepository {

    Mono<Void> sendReport(Metric metric);
}
