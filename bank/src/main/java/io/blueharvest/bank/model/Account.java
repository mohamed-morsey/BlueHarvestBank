package io.blueharvest.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * A bank account belonging to a specific customer
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
@Entity
public class Account {
    private Long id;

    private Long balance;

    private Customer customer;

    public Account() {
    }

    public Account(Long id) {
        this(id, 0L);
    }

    public Account(Long id, Long balance) {
        this.id = id;
        this.balance = balance;
    }

    public Account(Long id, Long balance, Customer customer) {
        this.id = id;
        this.balance = balance;
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

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @ManyToOne
    @JoinColumn(name = "account_customer_id")
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
                ", balance=" + balance +
                ", customer=" + customer +
                '}';
    }
}
