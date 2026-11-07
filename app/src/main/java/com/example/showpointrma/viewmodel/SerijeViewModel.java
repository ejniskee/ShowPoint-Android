package com.example.showpointrma.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.showpointrma.data.local.SerijaEntity;
import com.example.showpointrma.data.repository.SerijaRepository;

import java.util.List;

public class SerijeViewModel extends AndroidViewModel {

    private final SerijaRepository repository;
    private final LiveData<List<SerijaEntity>> serije; // reaktivna lista iz Rooma
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public SerijeViewModel(@NonNull Application application) {
        super(application);
        repository = new SerijaRepository(application);
        serije = repository.getSerije();
        refresh(); // jednom po zivotu viewmodela (rotacija ekrana ga ne pravi ponovo)
    }

    public LiveData<List<SerijaEntity>> getSerije() { return serije; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    private void refresh() {
        loading.setValue(true);
        repository.refreshSerije(success -> {
            loading.setValue(false);
            if (!success) {
                error.setValue("Nije moguce osvezavanje sa servera. Prikazani su sacuvani podaci.");
            }
        });
    }

    public void clearError() {
        error.setValue(null);
    }
}
