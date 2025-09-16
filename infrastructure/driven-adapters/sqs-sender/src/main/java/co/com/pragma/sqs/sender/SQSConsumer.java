package co.com.pragma.sqs.sender;

import co.com.pragma.model.metric.Metric;
import co.com.pragma.model.metric.gateways.MetricRepository;
import co.com.pragma.sqs.sender.config.SQSSenderProperties;
import co.com.pragma.sqs.sender.dto.MetricResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@EnableScheduling
@Log4j2
@RequiredArgsConstructor
public class SQSConsumer {

    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;
    private final MetricRepository metricRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 5000)
    public void pollMessages() throws ExecutionException, InterruptedException {
        ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .maxNumberOfMessages(10)
                .waitTimeSeconds(20)
                .build();

        List<Message> messages = client.receiveMessage(request).get().messages();

        for (Message message : messages) {
            log.info("Receive message {}", message.body());

            try {
                MetricResponseDTO metric = objectMapper.readValue(message.body(), MetricResponseDTO.class);
                log.info("Starting updating metrics");

                metricRepository.getReport()
                                .flatMap(currentMetric -> {
                                    log.info("Updating values from SQS");
                                    currentMetric.setProposalApprovedQuantity(currentMetric.getProposalApprovedQuantity()+1);
                                    currentMetric.setTotalApprovedAmount(currentMetric.getTotalApprovedAmount() + metric.getAmount());
                                    return Mono.just(currentMetric);
                                })
                                .flatMap(metricRepository::updateMetric)
                                .doOnSuccess(p -> {
                                    log.info("Report updating successfully");
                                    client.deleteMessage(DeleteMessageRequest.builder()
                                        .queueUrl(properties.queueUrl())
                                        .receiptHandle(message.receiptHandle())
                                        .build());
                                })
                                .doOnError(e -> log.error("Error updating report"))
                                .subscribe();
            } catch (Exception e) {
                log.error("Error while processing message {}: {}", message.body(), e.getMessage());
            }
        }
    }
}
