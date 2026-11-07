package com.example.showpointrma.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import com.example.showpointrma.api.ApiService;
import com.example.showpointrma.api.RetrofitClient;
import com.example.showpointrma.data.local.AppDatabase;
import com.example.showpointrma.data.local.SerijaDao;
import com.example.showpointrma.data.local.SerijaEntity;
import com.example.showpointrma.model.Serija;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerijaRepository {

    public interface RefreshCallback {
        void onResult(boolean success);
    }

    // room ne dozvoljava upis na glavnoj niti
    private static final ExecutorService IO = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper()); // obicna nit --> trajnja petlja za izvr. zad.

    private final AppDatabase db;
    private final SerijaDao dao;
    private final ApiService api;

    public SerijaRepository(Context context) {
        Context app = context.getApplicationContext();
        this.db = AppDatabase.getInstance(app);
        this.dao = db.serijaDao();
        this.api = RetrofitClient.getApi(app);
    }

    // 1) ono sto je vec u bazi - ekran to prikazuje odmah (bez neta)
    public LiveData<List<SerijaEntity>> getSerije() {
        return dao.getAll();
    }

    // 2) pita server: kad odgovori, menja sadrzaj baze -- livedata osvezava ekran
    public void refreshSerije(RefreshCallback callback) {
        api.getSerije().enqueue(new Callback<List<Serija>>() {
            @Override
            public void onResponse(Call<List<Serija>> call, Response<List<Serija>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final List<SerijaEntity> entities = toEntities(response.body());

                    IO.execute(() -> {
                        db.runInTransaction(() -> {
                            dao.deleteAll(); //da obrisane serije ne ostanu u kesu
                            dao.insertAll(entities);
                        });
                        mainHandler.post(() -> callback.onResult(true));
                    });
                } else {
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<List<Serija>> call, Throwable t) {
                callback.onResult(false);
            }
        });
    }

    private static List<SerijaEntity> toEntities(List<Serija> serije) {
        List<SerijaEntity> out = new ArrayList<>();

        for (Serija s : serije) {
            SerijaEntity e = new SerijaEntity();
            e.id = s.getId();
            e.naziv = s.getNaziv();
            e.godinaPocetka = s.getGodinaPocetka();
            e.posterUrl = s.getPosterUrl();
            e.prosecnaOcena = s.getProsecnaOcena();
            e.zanrovi = s.getZanrovi() == null ? "" : TextUtils.join(", ", s.getZanrovi());
            out.add(e);
        }
        return out;
    }

}
