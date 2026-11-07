package com.example.showpointrma.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.showpointrma.data.repository.DetaljiRepository;
import com.example.showpointrma.model.Epizoda;
import com.example.showpointrma.model.Ocena;
import com.example.showpointrma.model.SerijaDetalji;

import java.util.Map;

public class SerijaDetaljiViewModel extends AndroidViewModel {

    private final DetaljiRepository repository;
    private final MutableLiveData<SerijaDetalji> detalji = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>(); // za greske i obaves.

    private int serijaId = -1;

    public SerijaDetaljiViewModel(@NonNull Application application) {
        super(application);
        repository = new DetaljiRepository(application);
    }

    public LiveData<SerijaDetalji> getDetalji() {
        return detalji;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    // poziva se iz fragmenta: ako je ista serija (np. rotacija), nece ucitavati ponovo
    public void init(int id) {
        if (serijaId == id) return;
        serijaId = id;
        load(true);
    }

    private void load(boolean showSpinner) {
        if (showSpinner) loading.setValue(true);
        repository.getDetalji(serijaId, new DetaljiRepository.Result<SerijaDetalji>() {
            @Override
            public void onSuccess(SerijaDetalji data) {
                loading.setValue(false);
                detalji.setValue(data);
            }

            @Override
            public void onError(String msg) {
                loading.setValue(false);
                message.setValue(msg);
            }
        });
    }

    /* oznacavanje/ponistavanje epizode
    *   kad server potvrdi, tiho ponovo ucita detalje: novi procenat i stanje */

    public void toggleEpizoda(Epizoda epizoda, boolean odgledana) {
        repository.oznaci(epizoda.getId(), odgledana, new DetaljiRepository.Result<Map<String, Double>>() {
            @Override
            public void onSuccess(Map<String, Double> data) {
                load(false);
            }

            @Override
            public void onError(String msg) {
                message.setValue(msg);
            }
        });
    }

    public void oceni(int vrednost, String komentar) {
        repository.oceni(serijaId, vrednost, komentar, new DetaljiRepository.Result<Ocena>() {
            @Override
            public void onSuccess(Ocena data) {
                message.setValue("Ocena je sacuvana.");
                load(false);
            }

            @Override
            public void onError(String msg) {
                message.setValue(msg);
            }
        });
    }

    public void clearMessage() {
        message.setValue(null);
    }
}






















