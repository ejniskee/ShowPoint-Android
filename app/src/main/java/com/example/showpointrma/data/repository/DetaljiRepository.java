package com.example.showpointrma.data.repository;

import android.content.Context;

import com.example.showpointrma.api.ApiService;
import com.example.showpointrma.api.RetrofitClient;
import com.example.showpointrma.model.Ocena;
import com.example.showpointrma.model.OcenaRequest;
import com.example.showpointrma.model.SerijaDetalji;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Detalji serije, ocenjivanje i evidentiranje ZA SAD sve preko servera */
public class DetaljiRepository {
    public interface Result<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService api;

    public DetaljiRepository(Context context) {
        this.api = RetrofitClient.getApi(context.getApplicationContext());
    }

    // enqueue da RF zahtev izvrsi asinhrono, da ne blokira glavnu nit
    public void getDetalji(int serijaId, Result<SerijaDetalji> result) {
        api.getSerija(serijaId).enqueue(callback(result));
    }

    public void oceni(int serijaId, int vrednost, String komentar, Result<Ocena> result) {
        api.oceni(serijaId, new OcenaRequest(vrednost, komentar)).enqueue(callback(result));
    }

    public void oznaci(int epizodaId, boolean odgledana, Result<Map<String, Double>> result) {
        Call<Map<String, Double>> call = odgledana
                ? api.oznaciOdgledanu(epizodaId)
                : api.ukloniOdgledanu(epizodaId);
        call.enqueue(callback(result));
    }

    // jedan zajednicki kolbek, pretvara http kodove i greske u poruke za korisnika
    private <T> Callback<T> callback(Result<T> result) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful())
                    result.onSuccess(response.body());
                else
                    result.onError(poruka(response.code()));
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                result.onError("Nema konekcije sa serverom.");
            }
        };
    }

    private static String poruka(int code) {
        switch (code) {
            case 400: return "Zahtev nije ispravan.";
            case 401: return "Prijava neophodna za ovu opciju.";
            case 404: return "Nije pronadjeno.";
            default: return "Serverska greska (" + code + ").";
        }
    }


}





















