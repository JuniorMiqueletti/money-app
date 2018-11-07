package com.juniormiqueletti.moneyapp.controller.dto;

import com.juniormiqueletti.moneyapp.model.Person;
import com.juniormiqueletti.moneyapp.model.ReleaseType;

import java.math.BigDecimal;

public class StatisticalReleasePerson {

    private ReleaseType type;
    private Person person;
    private BigDecimal total;

    public StatisticalReleasePerson(
        ReleaseType type,
        Person person,
        BigDecimal total
    ) {
        this.type = type;
        this.person = person;
        this.total = total;
    }

    public ReleaseType getType() {
        return type;
    }

    public void setType(ReleaseType type) {
        this.type = type;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
