package com.example.showpointrma.data.repository;

import android.content.Context;

import com.example.showpointrma.api.ApiService;
import com.example.showpointrma.api.RetrofitClient;
import com.example.showpointrma.model.LoginRequest;
import com.example.showpointrma.model.LoginResponse;
import com.example.showpointrma.model.RegistracijaRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// vrsi login i registraciju preko servera
public class AuthRepository {

    public interface Result<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    private final ApiService api;

    public AuthRepository(Context context) {
        this.api = RetrofitClient.getApi(context.getApplicationContext());
    }

    public void login(String email, String lozinka, Result<LoginResponse> result) {
        api.login(new LoginRequest(email, lozinka))
                .enqueue(callback(result, "Pogresan email ili lozinka.", null));
    }

    public void register(RegistracijaRequest request, Result<Map<String, String>> result) {
        api.register(request)
                .enqueue(callback(result, null, "Uneti email je zauzet"));
    }

    // pretvara HTTP kodove i greske mreze u poruke za korisnika
    private <T> Callback<T> callback(Result<T> result, String porukaZa401, String porukaZa409) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    result.onSuccess(response.body());
                    return;
                }
                int code = response.code();

                if (code == 401 && porukaZa401 != null)
                    result.onError(porukaZa401);

                else if (code == 409 && porukaZa409 != null)
                    result.onError(porukaZa409);

                else if (code == 400)
                    result.onError("Neispravni podaci.");

                else
                    result.onError("Serverska greska (" + code + ").");

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                result.onError("Nema konekcije sa serverom.");
            }
        };
    }
}












