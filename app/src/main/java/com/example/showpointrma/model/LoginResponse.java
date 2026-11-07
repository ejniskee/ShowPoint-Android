package com.example.showpointrma.model;

public class LoginResponse {
    private String token;
    private int id;
    private String email;
    private String ime;
    private String prezime;
    private String uloga;

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getUloga() {
        return uloga;
    }
}
