package co.com.pragma.api.metric.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetricResponseDTO {

    @Schema(description = "Proposal Approved Quantity until now")
    private Long proposalApprovedQuantity;

    @Schema(description = "Total Approved Amount until now")
    private Double totalApprovedAmount;
}
