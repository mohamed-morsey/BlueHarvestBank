package io.blueharvest.bank.dto;

import io.blueharvest.bank.model.Customer;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * DTO for {@link Customer}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-04
 **/
public class CustomerDto {
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String address;

    @NotBlank
    private String postcode;

    public CustomerDto() {
        id = 0L;
    }

    public CustomerDto(long id) {
        this(id, EMPTY, EMPTY, EMPTY, EMPTY);
    }

    public CustomerDto(long id, String name, String surname, String address, String postcode) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CustomerDto)) {
            return false;
        }

        CustomerDto customer = (CustomerDto) o;

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
                '}';
    }
}