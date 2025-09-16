package co.com.pragma.sqs.sender.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MetricResponseDTO {
    private Double amount;
}
