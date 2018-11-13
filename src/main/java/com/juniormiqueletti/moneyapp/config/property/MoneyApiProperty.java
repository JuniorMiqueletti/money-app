package com.juniormiqueletti.moneyapp.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("money-app-api")
public class MoneyApiProperty {

    private String sourceAllowed = "http://localhost:8000";

    private final Security seguranca = new Security();

    private final Mail mail = new Mail();

    public String getSourceAllowed() {
        return sourceAllowed;
    }

    public void setSourceAllowed(String sourceAllowed) {
        this.sourceAllowed = sourceAllowed;
    }

    public Security getSeguranca() {
        return seguranca;
    }

    public Mail getMail() {
        return mail;
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

    public static class Mail {

        private String host;

        private Integer port;

        private String username;

        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
