package com.example.showpointrma.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.showpointrma.data.repository.AuthRepository;
import com.example.showpointrma.model.LoginResponse;
import com.example.showpointrma.model.RegistracijaRequest;
import com.example.showpointrma.util.TokenManager;

import java.util.Map;

/* koriste ga i Login i Reg. Fragment (svaki svoju instancu) */
public class AuthViewModel extends AndroidViewModel {

    private final AuthRepository repository;
    private final TokenManager tokenManager;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<Boolean> done = new MutableLiveData<>(false); // true = ulogovan, zatvori ekran


    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        tokenManager = new TokenManager(application);
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<Boolean> getDone() {
        return done;
    }

    public void login(String email, String lozinka) {
        loading.setValue(true);
        repository.login(email, lozinka, new AuthRepository.Result<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse data) {
                sacuvajSesiju(data);
            }

            @Override
            public void onError(String msg) {
                loading.setValue(false);
                message.setValue(msg);
            }
        });
    }

    public void register(String ime, String prezime, String email, String lozinka) {
        loading.setValue(true);
        repository.register(new RegistracijaRequest(ime, prezime, email, lozinka), new AuthRepository.Result<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> data) { // po uspesnoj registraciji odradi i login
                repository.login(email, lozinka, new AuthRepository.Result<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse data) {
                        sacuvajSesiju(data);
                    }

                    @Override
                    public void onError(String msg) {
                        loading.setValue(false);
                        message.setValue("Registracija uspesna, ali prijava ne. Prijavi se rucno.");
                    }
                });
            }

            @Override
            public void onError(String msg) {
                loading.setValue(false);
                message.setValue(msg);
            }
        });
    }

    private void sacuvajSesiju(LoginResponse r) {
        tokenManager.saveSession(r.getToken(), r.getIme(), r.getEmail());
        loading.setValue(false);
        message.setValue("Dobrodosao, " + (r.getIme() == null ? "" : r.getIme()) + "!");
        done.setValue(true);
    }

    public void clearMessage() {
        message.setValue(null);
    }
}
