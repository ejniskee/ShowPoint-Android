package com.example.showpointrma.model;

import java.util.List;

public class Sezona {
    private int id;
    private int redniBroj;
    private List<Epizoda> epizode;

    public int getId() {
        return id;
    }

    public int getRedniBroj() {
        return redniBroj;
    }

    public List<Epizoda> getEpizode() {
        return epizode;
    }
}
