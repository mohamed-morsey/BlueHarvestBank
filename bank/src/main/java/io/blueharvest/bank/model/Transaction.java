package io.blueharvest.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * A transaction performed on an account
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
@Entity
public class Transaction {
    private long id;

    @NotNull
    private Double amount;

    @NotNull
    private Date transactionTime;

    @NotNull
    private Account account;

    public Transaction() {
        this.id = 0L;
        this.amount = 0.0D;
        this.account = new Account();
        this.transactionTime = new Date();
    }

    public Transaction(long id, Double amount, Account account) {
        this.id = id;
        this.amount = amount;
        this.account = account;
        this.transactionTime = new Date();
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    @ManyToOne
    @JoinColumn(name = "transaction_account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Transaction)) {
            return false;
        }

        Transaction transaction = (Transaction) o;

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
                ", account=" + account +
                '}';
    }
}
