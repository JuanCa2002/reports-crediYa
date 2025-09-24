package co.com.pragma.consumer.sendreport;

import co.com.pragma.consumer.sendreport.dto.ReportRequestDTO;
import co.com.pragma.consumer.sendreport.mapper.ReportMapper;
import co.com.pragma.model.diaryproposal.DiaryProposal;
import co.com.pragma.model.diaryproposal.gateways.DiaryProposalRepository;
import co.com.pragma.model.metric.Metric;
import co.com.pragma.model.metric.gateways.MetricRepository;
import co.com.pragma.model.report.gateways.ReportRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class SendReportConsumer implements ReportRepository {

    private final ReportMapper mapper;
    private final WebClient client;
    private final MetricRepository metricRepository;
    private final DiaryProposalRepository diaryProposalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${sender.email}")
    private String senderEmail;


    public SendReportConsumer(ReportMapper mapper,
                              @Qualifier("lambdaWebClient") WebClient client, MetricRepository metricRepository,
                              DiaryProposalRepository diaryProposalRepository) {
        this.mapper = mapper;
        this.client = client;
        this.metricRepository = metricRepository;
        this.diaryProposalRepository = diaryProposalRepository;
    }

    @Scheduled(cron = "0 0 2 * * ?", zone = "America/Bogota")
    @SchedulerLock(name = "sendDailyReport", lockAtLeastFor = "PT5M", lockAtMostFor = "PT14M")
    public void sendEmailReport() {
        log.info("[SendReportConsumer] sending daily report...");

        metricRepository.getReport()
                .flatMap(metric -> diaryProposalRepository.getAll()
                        .flatMap(proposals -> sendReport(metric, proposals))
                )
                .then()
                .subscribe(
                        unused -> {},
                        error -> log.error("Error sending report", error)
                );
    }


    @Override
    @CircuitBreaker(name = "sendReport")
    public Mono<Void> sendReport(Metric metric, List<DiaryProposal> proposals) {
        log.info("[SendReportConsumer] mapping found metrics");
        ReportRequestDTO request = mapper.toRequest(metric);
        request.setDiaryProposals(proposals);
        log.info("[SendReportConsumer] setting target email to {}", senderEmail);
        request.setEmail(senderEmail);
        try {
            String bodyAsJson = objectMapper.writeValueAsString(request);

            return client.post()
                .attribute("signedBody", bodyAsJson)
                .bodyValue(bodyAsJson)
                .retrieve()
                .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("[SendReportConsumer] email send with success message {}", response))
                    .doOnError(error -> log.error("[SendReportConsumer] error while sending email to {}", senderEmail, error))
                    .then(Mono.empty());


        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error serializing request to JSON", e));
        }
    }
}
