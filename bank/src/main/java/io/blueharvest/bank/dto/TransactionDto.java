package io.blueharvest.bank.dto;

import io.blueharvest.bank.model.Transaction;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * DTO for {@link Transaction}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
public class TransactionDto {
    private long id;
    private double amount;
    private long accountId;

    @NotNull
    private Date transactionTime;

    public TransactionDto() {
        this(0L, 0.0D);
    }

    public TransactionDto(long id, Double amount) {
        this.id = id;
        this.amount = amount;
        this.transactionTime = new Date();
    }

    public TransactionDto(long id, double amount, long accountId) {
        this(id, amount);
        this.accountId = accountId;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TransactionDto)) {
            return false;
        }

        TransactionDto transaction = (TransactionDto) o;

        return id == transaction.id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", transactionTime=" + transactionTime +
                ", accountId=" + accountId +
                '}';
    }
}
