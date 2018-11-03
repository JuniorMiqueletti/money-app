package com.juniormiqueletti.moneyapp.controller.dto;

import com.juniormiqueletti.moneyapp.model.Category;

import java.math.BigDecimal;

public class StatisticalReleaseCategory {

    private Category category;
    private BigDecimal total;

    public StatisticalReleaseCategory(
        Category category,
        BigDecimal total
    ) {
        this.category = category;
        this.total = total;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
