package com.example.showpointrma.model;

public class Epizoda {
    private int id;
    private int redniBroj;
    private String naziv;
    private int trajanjeMin;
    private boolean odgledana; // moze biti tacno samo za ulog. kor.

    public int getId() {
        return id;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getTrajanjeMin() {
        return trajanjeMin;
    }

    public boolean isOdgledana() {
        return odgledana;
    }
}
