package io.blueharvest.bank.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.Set;

/**
 * A bank account belonging to a specific customer
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Entity
public class Account {
    private Long id;
    private Long credit;
    private Customer customer;
    private Set<Transaction> transactions;

    public Account() {
    }

    public Account(Long id) {
        this(id, 0L);
    }

    public Account(Long id, Long credit) {
        this.id = id;
        this.credit = credit;
    }

    public Account(Long id, Long credit, Customer customer) {
        this.id = id;
        this.credit = credit;
        this.customer = customer;
    }

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    @ManyToOne
    @JoinColumn(name = "account_customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> accounts) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)){
            return false;
        }

        Account account = (Account) o;

        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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
