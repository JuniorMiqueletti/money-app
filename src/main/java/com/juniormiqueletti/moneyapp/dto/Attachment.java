package com.juniormiqueletti.moneyapp.dto;

public class Attachment {

    private String name;
    private String Url;

    public Attachment(String name, String url) {
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
