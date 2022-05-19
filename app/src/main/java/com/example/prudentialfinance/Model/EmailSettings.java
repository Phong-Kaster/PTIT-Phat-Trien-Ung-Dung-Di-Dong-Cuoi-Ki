package com.example.prudentialfinance.Model;

public class EmailSettings {
    private String host;
    private String port;
    private String encryption;
    private boolean auth;
    private String username;
    private String password;
    private String from;

    public EmailSettings() {
    }

    public EmailSettings(String host, String port, String encryption, boolean auth, String username, String password, String from) {
        this.host = host;
        this.port = port;
        this.encryption = encryption;
        this.auth = auth;
        this.username = username;
        this.password = password;
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getEncryption() {
        return encryption;
    }

    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    public boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
