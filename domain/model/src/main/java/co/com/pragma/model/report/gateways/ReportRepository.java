package co.com.pragma.model.report.gateways;

import co.com.pragma.model.diaryproposal.DiaryProposal;
import co.com.pragma.model.metric.Metric;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReportRepository {

    Mono<Void> sendReport(Metric metric, List<DiaryProposal> proposals);
}
