package co.com.pragma.consumer.sendreport.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportRequestDTO {

    private String email;
    private Double totalAmount;
    private Long approvedProposals;

}
