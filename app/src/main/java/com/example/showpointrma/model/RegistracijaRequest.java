package com.example.showpointrma.model;

public class RegistracijaRequest {
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;

    public RegistracijaRequest(String ime, String prezime, String email, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
    }
}
