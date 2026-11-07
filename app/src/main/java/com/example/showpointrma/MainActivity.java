package com.example.showpointrma;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.showpointrma.api.RetrofitClient;
import com.example.showpointrma.model.LoginRequest;
import com.example.showpointrma.model.LoginResponse;
import com.example.showpointrma.model.Serija;
import com.example.showpointrma.ui.LoginFragment;
import com.example.showpointrma.ui.PrazanEkranFragment;
import com.example.showpointrma.ui.RegistracijaFragment;
import com.example.showpointrma.ui.SerijaDetaljiFragment;
import com.example.showpointrma.ui.SerijeListFragment;
import com.example.showpointrma.util.TokenManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_IZABRANA = "izabrana_serija";

    private boolean tabletMode;
    private int izabranaSerijaId = -1; // na pocetku, trenutno otvorena (tab)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* samo layout sa 600dp ce imati desni kontejner,
        * android autom. bira odgovarajuci activity_main xml,
        *  na osnovu uredjaja */
        tabletMode = findViewById(R.id.detail_container) != null;

        if (savedInstanceState != null) {
            // rotacija: android sam vraca fragmente, samo treba zapamcena serija
            izabranaSerijaId = savedInstanceState.getInt(KEY_IZABRANA, -1);
        } else {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SerijeListFragment());

            if (tabletMode)
                t.replace(R.id.detail_container, new PrazanEkranFragment());

            t.commit();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_IZABRANA, izabranaSerijaId);
    }

    private int kontejnerZaEkrane() {
        return tabletMode ? R.id.detail_container : R.id.fragment_container;
    }

    public void otvoriLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(kontejnerZaEkrane(), new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    public void otvoriRegistraciju() {
        getSupportFragmentManager().beginTransaction()
                .replace(kontejnerZaEkrane(), new RegistracijaFragment())
                .addToBackStack(null)
                .commit();
    }

    public void otvoriDetalje(int serijaId) {

        izabranaSerijaId = serijaId;
        SerijaDetaljiFragment fragment = SerijaDetaljiFragment.newInstance(serijaId);

        if (tabletMode) {
            // zatvori login/register ako su bili otvoreni, pa zameni desni panel
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, fragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, SerijaDetaljiFragment.newInstance(serijaId))
                    .addToBackStack(null)
                    .commit();
        }


    }

    // vraca na pocetnu listu (zatvara login i reg. odjednom)
    public void vratiNaListu() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); // fon

        // tablet: lista nonstop na ekranu, treba joj rucno osveziti traku (prijava/odjava)
        Fragment lista = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (lista instanceof SerijeListFragment)
            ((SerijeListFragment) lista).updateAuthUI();

        osveziDetalje();
    }

    // za tablet: ponovo ucitaj otvorene detalje (nakon prijave/odjave, da se vide podaci tog korisnika)
    public void osveziDetalje() {
        if (tabletMode && izabranaSerijaId != -1)
            otvoriDetalje(izabranaSerijaId);
    }
}