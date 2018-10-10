package io.blueharvest.bank.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Represents a bank customer in the system
 *
 * @author Mohamed Morsey
 * Date: 2018-10-04
 **/
@Entity
public class Customer {
    private long id;
    private String name;
    private String surname;
    private String address;
    private String postcode;
    private Set<Account> accounts;

    public Customer() {
        id = 0L;
    }

    public Customer(long id) {
        this(id, EMPTY, EMPTY, EMPTY, EMPTY);
    }

    public Customer(long id, String name, String surname, String address, String postcode) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.postcode = postcode;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Customer)) {
            return false;
        }

        Customer customer = (Customer) o;

        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}