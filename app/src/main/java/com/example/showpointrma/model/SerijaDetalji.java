package com.example.showpointrma.model;

import java.util.List;

public class SerijaDetalji extends Serija {

    private String opis;
    private Double procenat;                // null ako nije ulogovan
    private List<Integer> odgledaneEpizode; // takodje
    private Ocena mojaOcena;                // isto
    private List<Sezona> sezone;

    public String getOpis() {
        return opis;
    }

    public Double getProcenat() {
        return procenat;
    }

    public List<Integer> getOdgledaneEpizode() {
        return odgledaneEpizode;
    }

    public Ocena getMojaOcena() {
        return mojaOcena;
    }

    public List<Sezona> getSezone() {
        return sezone;
    }
}
