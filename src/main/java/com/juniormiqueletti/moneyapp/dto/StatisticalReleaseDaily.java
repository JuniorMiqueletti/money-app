package com.juniormiqueletti.moneyapp.dto;

import com.juniormiqueletti.moneyapp.model.ReleaseType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StatisticalReleaseDaily {

    private ReleaseType type;
    private LocalDate day;
    private BigDecimal total;

    public StatisticalReleaseDaily(
        ReleaseType type,
        LocalDate day,
        BigDecimal total
    ) {
        this.type = type;
        this.day = day;
        this.total = total;
    }

    public ReleaseType getType() {
        return type;
    }

    public void setType(ReleaseType type) {
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
