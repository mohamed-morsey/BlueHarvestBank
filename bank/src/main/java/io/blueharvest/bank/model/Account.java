package io.blueharvest.bank.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

/**
 * A bank account belonging to a specific customer
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Entity
public class Account {
    private long id;
    private Double credit;
    private Date establishDate;
    private Customer customer;
    private Set<Transaction> transactions;

    public Account() {
        this(0L, 0.0D);
    }

    public Account(long id) {
        this(id, 0.0D);
    }

    public Account(long id, Double credit) {
        this.id = id;
        this.credit = credit;
        this.establishDate = new Date();
    }

    public Account(long id, Double credit, Customer customer) {
        this(id, credit);
        this.customer = customer;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    @ManyToOne
    @JoinColumn(name = "account_customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account account = (Account) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", credit=" + credit +
                ", customer=" + customer +
                '}';
    }
}
