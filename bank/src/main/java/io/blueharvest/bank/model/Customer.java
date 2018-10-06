package io.blueharvest.bank.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
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
    private Long id;

    private String name;
    private String address;
    private String postcode;
    private Set<Account> accounts;

    public Customer() {
    }

    public Customer(Long id) {
        this(id, EMPTY, EMPTY, EMPTY);
    }

    public Customer(Long id, String name, String address, String postcode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
    }

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postcode='" + postcode + '\'' +
                '}';
    }
}
