package com.example.showpointrma.model;

public class LoginRequest {
    private String email;
    private String lozinka;

    public LoginRequest(String email, String lozinka) {
        this.email = email;
        this.lozinka = lozinka;
    }
}
