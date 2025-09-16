package co.com.pragma.api.metric;

import co.com.pragma.api.metric.dto.MetricResponseDTO;
import co.com.pragma.api.metric.mapper.MetricMapper;
import co.com.pragma.usecase.metric.MetricUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Tag(name = "Reports", description = "Reports management APIs")
@Slf4j
@Component
@RequiredArgsConstructor
public class MetricHandler {

    private final MetricUseCase metricUseCase;
    private final MetricMapper mapper;

    @Operation(
            operationId = "getReport",
            summary = "Get report with the metrics",
            description = "Get report and returns its metrics",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Report",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MetricResponseDTO.class)
                            )
                    )
            }
    )
    public Mono<ServerResponse> listenGetReport(ServerRequest serverRequest) {
        return metricUseCase.getReport()
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok().bodyValue(response));
    }
}
