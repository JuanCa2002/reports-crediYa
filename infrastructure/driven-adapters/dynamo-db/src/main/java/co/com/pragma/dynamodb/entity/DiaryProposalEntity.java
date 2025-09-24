package co.com.pragma.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigInteger;

@DynamoDbBean
public class DiaryProposalEntity {

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

    public DiaryProposalEntity(BigInteger id, Double amount, String limitDate, String creationDate, Double baseSalary, Double monthlyFee, String email, String proposalType, String state, Double interestRate) {
        this.id = id;
        this.amount = amount;
        this.limitDate = limitDate;
        this.creationDate = creationDate;
        this.baseSalary = baseSalary;
        this.monthlyFee = monthlyFee;
        this.email = email;
        this.proposalType = proposalType;
        this.state = state;
        this.interestRate = interestRate;
    }

    public DiaryProposalEntity() {
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @DynamoDbAttribute("amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @DynamoDbAttribute("limitDate")
    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    @DynamoDbAttribute("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @DynamoDbAttribute("baseSalary")
    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    @DynamoDbAttribute("monthlyFee")
    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbAttribute("proposalType")
    public String getProposalType() {
        return proposalType;
    }

    public void setProposalType(String proposalType) {
        this.proposalType = proposalType;
    }

    @DynamoDbAttribute("state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @DynamoDbAttribute("interestRate")
    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }
}

