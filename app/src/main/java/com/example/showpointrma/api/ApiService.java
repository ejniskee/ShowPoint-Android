package com.example.showpointrma.api;

import com.example.showpointrma.model.LoginRequest;
import com.example.showpointrma.model.LoginResponse;
import com.example.showpointrma.model.Ocena;
import com.example.showpointrma.model.OcenaRequest;
import com.example.showpointrma.model.RegistracijaRequest;
import com.example.showpointrma.model.Serija;
import com.example.showpointrma.model.SerijaDetalji;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // pisano po endpoint-ima testiranim u Swaggeru

    /* za svaki novi endpoint (ocene, odgledane epizodde) dodajemo po jednu metodu */

    // pre koriscenja liste proveriti != null jer procenat i odgledaneEpizode mogu biti null


    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<Map<String, String>> register(@Body RegistracijaRequest request);

    @GET("api/serije")
    Call<List<Serija>> getSerije();

    @GET("api/serije/{id}")
    Call<SerijaDetalji> getSerija(@Path("id") long id);

    // zahtevaju token, interceptor ga dodaje sam

    @PUT("api/serije/{id}/ocena")
    Call<Ocena> oceni(@Path("id") int serijaId, @Body OcenaRequest body);

    @POST("api/epizode/{id}/odgledana")
    Call<Map<String, Double>> oznaciOdgledanu(@Path("id") int epizodaId);

    @DELETE("api/epizode/{id}/odgledana")
    Call<Map<String, Double>> ukloniOdgledanu(@Path("id") int epizodaId);


}
