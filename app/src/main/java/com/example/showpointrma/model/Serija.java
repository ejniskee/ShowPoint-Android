package com.example.showpointrma.model;

/* ne moraju biti analogni JPA entitetima jer rest kontoler radi sa DTO-ovima a ne sa entitetima*/

import java.util.List;

public class Serija {

    private int id;
    private String naziv;
    private int godinaPocetka;
    private String posterUrl;
    private Double prosecnaOcena;
    private List<String> zanrovi;

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getGodinaPocetka() {
        return godinaPocetka;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Double getProsecnaOcena() {
        return prosecnaOcena;
    }

    public List<String> getZanrovi() {
        return zanrovi;
    }
}
