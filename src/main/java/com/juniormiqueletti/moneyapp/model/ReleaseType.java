package com.juniormiqueletti.moneyapp.model;

public enum ReleaseType {

	RECIPE("Recipe"),
    EXPENSE("Expense");

	private String description;

    ReleaseType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
