package com.bankingsim.dto;

import com.bankingsim.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {
    private Long transactionId;
    private String type;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
    private String sourceAccountNumber;
    private String targetAccountNumber;

    public static TransactionResponse from(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.transactionId = t.getId();
        r.type = t.getType().name();
        r.amount = t.getAmount();
        r.status = t.getStatus();
        r.timestamp = t.getTimestamp();
        r.sourceAccountNumber = t.getSourceAccount() != null ? t.getSourceAccount().getAccountNumber() : null;
        r.targetAccountNumber = t.getTargetAccount() != null ? t.getTargetAccount().getAccountNumber() : null;
        return r;
    }

    public Long getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
}
