package co.com.pragma.model.diaryproposal;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DiaryProposal {

    private BigInteger id;
    private Double amount;
    private String limitDate;
    private String creationDate;
    private Double baseSalary;
    private Double monthlyFee;
    private String email;
    private String proposalType;
    private String state;
    private Double interestRate;
}
