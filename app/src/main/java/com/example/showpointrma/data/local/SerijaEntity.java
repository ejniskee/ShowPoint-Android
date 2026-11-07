package com.example.showpointrma.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/* Room tabela "serija" - lokalni kes liste serija.
    Odvojeno od API modela (model.Serija) jer Room ne zna da cuva List<String>,
    pa se zanrovi upisuju kao jedan tekst ("Drama, Triler"). */

@Entity(tableName = "serija")
public class SerijaEntity {

    @PrimaryKey
    public int id;

    public String naziv;
    public int godinaPocetka;
    public String posterUrl;
    public Double prosecnaOcena;   // moze biti null
    public String zanrovi;
}
