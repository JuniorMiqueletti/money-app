package com.juniormiqueletti.moneyapp.dto;

public class Attached {

    private String name;
    private String Url;

    public Attached(String name, String url) {
        this.name = name;
        Url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return Url;
    }
}
