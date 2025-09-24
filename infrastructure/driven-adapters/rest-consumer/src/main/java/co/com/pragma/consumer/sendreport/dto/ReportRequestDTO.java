package co.com.pragma.consumer.sendreport.dto;

import co.com.pragma.model.diaryproposal.DiaryProposal;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReportRequestDTO {

    private String email;
    private Double totalAmount;
    private Long approvedProposals;
    private List<DiaryProposal> diaryProposals;

}
