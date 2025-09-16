package co.com.pragma.api.metric;

import co.com.pragma.api.dto.errors.ErrorResponse;
import co.com.pragma.api.metric.dto.MetricResponseDTO;
import co.com.pragma.api.metric.mapper.MetricMapper;
import co.com.pragma.usecase.metric.MetricUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reports", description = "Reports management APIs")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = "Validation data error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected server error",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
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
        log.info("[MetricHandler] Getting report metrics");
        return metricUseCase.getReport()
                .map(mapper::toResponse)
                .flatMap(response -> ServerResponse.ok().bodyValue(response))
                .doOnSuccess(r -> log.info("[MetricHandler] Returning report successfully {}", r.statusCode()))
                .doOnError(error -> log.error("[MetricHandler] Error while getting the report", error));
    }
}
