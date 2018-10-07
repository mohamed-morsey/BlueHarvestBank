package io.blueharvest.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * A transaction performed on an account
 *
 * @author Mohamed Morsey
 * Date: 2018-10-06
 **/
@Entity
public class Transaction {
    private Long id;
    private Double value;
    private Date transactionTime;
    private Account account;

    public Transaction() {
        this(0L, 0.0D);
    }

    public Transaction(Long id, Double value) {
        this.id = id;
        this.value = value;
        this.transactionTime = new Date();
    }

    public Transaction(Long id, Double value, Account account) {
        this(id, value);
        this.account = account;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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

        return id.equals(transaction.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value=" + value +
                ", transactionTime=" + transactionTime +
                ", account=" + account +
                '}';
    }
}
