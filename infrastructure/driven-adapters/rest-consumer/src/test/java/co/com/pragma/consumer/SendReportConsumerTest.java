package co.com.pragma.consumer;


import co.com.pragma.consumer.sendreport.SendReportConsumer;
import co.com.pragma.consumer.sendreport.dto.ReportRequestDTO;
import co.com.pragma.consumer.sendreport.mapper.ReportMapper;
import co.com.pragma.model.metric.Metric;
import co.com.pragma.model.metric.gateways.MetricRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class SendReportConsumerTest {

    private static SendReportConsumer restConsumer;

    private static MockWebServer mockBackEnd;

    @Mock
    private ReportMapper mapper;

    @Mock
    private MetricRepository repository;

    @BeforeAll
    static void setUpAll() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @BeforeEach
    void setUp() {
        var webClient = WebClient.builder().baseUrl(mockBackEnd.url("/").toString()).build();
        restConsumer = new SendReportConsumer(mapper, webClient, repository);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function sendReport.")
    void validateSendReport() {

        final Metric metric = Metric.builder()
                .id("my-id")
                .totalApprovedAmount(10000.0)
                .proposalApprovedQuantity(3L)
                .build();

        final ReportRequestDTO request = ReportRequestDTO.builder()
                .email("random@email.com")
                .approvedProposals(3L)
                .totalAmount(10000.0)
                .build();

        Mockito
                .when(mapper.toRequest(Mockito.any(Metric.class)))
                .thenReturn(request);

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"amount\" : 10000.0}"));
        var response = restConsumer.sendReport(metric);

        StepVerifier.create(response)
                .verifyComplete();
    }
}