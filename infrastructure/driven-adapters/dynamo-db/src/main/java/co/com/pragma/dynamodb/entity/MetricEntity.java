package co.com.pragma.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class MetricEntity {

    private String id;
    private Long proposalApprovedQuantity;
    private Double totalApprovedAmount;

    public MetricEntity() {
    }

    public MetricEntity(Long proposalApprovedQuantity, Double totalApprovedAmount) {
        this.proposalApprovedQuantity = proposalApprovedQuantity;
        this.totalApprovedAmount = totalApprovedAmount;
    }

    @DynamoDbAttribute("proposalApprovedQuantity")
    public Long getProposalApprovedQuantity() {
        return proposalApprovedQuantity;
    }

    public void setProposalApprovedQuantity(Long proposalApprovedQuantity) {
        this.proposalApprovedQuantity = proposalApprovedQuantity;
    }

    @DynamoDbAttribute("totalApprovedAmount")
    public Double getTotalApprovedAmount() {
        return totalApprovedAmount;
    }

    public void setTotalApprovedAmount(Double totalApprovedAmount) {
        this.totalApprovedAmount = totalApprovedAmount;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
