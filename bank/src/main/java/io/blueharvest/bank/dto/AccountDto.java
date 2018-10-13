package io.blueharvest.bank.dto;

import io.blueharvest.bank.model.Account;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * DTO for {@link Account}
 *
 * @author Mohamed Morsey
 * Date: 2018-10-05
 **/
public class AccountDto {
    private long id;

    @NotNull
    private Double credit;

    @NotNull
    private Date establishDate;

    private long customerId;

    public AccountDto() {
        this(0L, 0.0D);
    }

    public AccountDto(long id) {
        this(id, 0.0D);
    }

    public AccountDto(long id, Double credit) {
        this.id = id;
        this.credit = credit;
        this.establishDate = new Date();
    }

    public AccountDto(long id, Double credit, long customerId) {
        this(id, credit);
        this.customerId = customerId;
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

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AccountDto)) {
            return false;
        }

        AccountDto account = (AccountDto) o;

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
                ", customerId=" + customerId +
                '}';
    }
}
