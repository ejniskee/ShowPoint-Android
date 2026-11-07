package com.example.showpointrma.ui;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.showpointrma.MainActivity;
import com.example.showpointrma.R;
import com.example.showpointrma.viewmodel.AuthViewModel;

public class RegistracijaFragment extends Fragment {

    private static final int MIN_DUZINA_LOZINKE = 6;

    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registracija, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etIme = view.findViewById(R.id.et_ime);
        EditText etPrezime = view.findViewById(R.id.et_prezime);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etLozinka = view.findViewById(R.id.et_lozinka);
        EditText etPonovi = view.findViewById(R.id.et_lozinka_ponovi);

        Button btnRegistruj = view.findViewById(R.id.btn_registruj);
        TextView tvPrijava = view.findViewById(R.id.tv_prijava);
        ProgressBar progress = view.findViewById(R.id.progress);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean zauzet = Boolean.TRUE.equals(loading);
            progress.setVisibility(zauzet ? View.VISIBLE : View.GONE);
            btnRegistruj.setEnabled(!zauzet); // da ne moze dupli zahtev!
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                viewModel.clearMessage();
            }
        });

        // registracija + auto prijava uspeli => nazad na listu
        viewModel.getDone().observe(getViewLifecycleOwner(), done -> {
            if (Boolean.TRUE.equals(done))
                ((MainActivity) requireActivity()).vratiNaListu();
        });

        btnRegistruj.setOnClickListener(v -> {
            String ime = etIme.getText().toString().trim();
            String prezime = etPrezime.getText().toString().trim();

            String email = etEmail.getText().toString().trim();
            String lozinka = etLozinka.getText().toString();
            String ponovi = etPonovi.getText().toString();

            boolean ispravno = true;
            if (ime.isEmpty()) {
                etIme.setError("Unesite ime.");
                ispravno = false;
            }

            if (prezime.isEmpty()) {
                etPrezime.setError("Unesite prezime.");
                ispravno = false;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Unesi ispravan email.");
                ispravno = false;
            }

            if (lozinka.length() < MIN_DUZINA_LOZINKE) {
                etLozinka.setError("Lozinka mora imati bar " + MIN_DUZINA_LOZINKE + " znakova.");
                ispravno = false;
            }

            if (!lozinka.equals(ponovi)) {
                etPonovi.setError("Lozinke se ne poklapaju.");
                ispravno = false;
            }

            if (ispravno)
                viewModel.register(ime, prezime, email, lozinka);
        });

        // nazad na login ekran
        tvPrijava.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        /* .popBackStack() => sustinski se vracamo korak unazad */
    }
}









