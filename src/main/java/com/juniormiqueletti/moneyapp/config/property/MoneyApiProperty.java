package com.juniormiqueletti.moneyapp.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("money-app-api")
public class MoneyApiProperty {

    private String sourceAllowed = "http://localhost:8000";

    private final Security seguranca = new Security();

    public String getSourceAllowed() {
        return sourceAllowed;
    }

    public void setSourceAllowed(String sourceAllowed) {
        this.sourceAllowed = sourceAllowed;
    }

    public Security getSeguranca() {
        return seguranca;
    }

    public static class Security {

        private boolean enableHttps;

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }

    }
}
