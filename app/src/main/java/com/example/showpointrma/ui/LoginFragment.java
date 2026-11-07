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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.showpointrma.MainActivity;
import com.example.showpointrma.R;
import com.example.showpointrma.viewmodel.AuthViewModel;

public class LoginFragment extends Fragment {

    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etLozinka = view.findViewById(R.id.et_lozinka);
        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView tvRegistracija = view.findViewById(R.id.tv_registracija);
        ProgressBar progress = view.findViewById(R.id.progress);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            boolean zauzet = Boolean.TRUE.equals(loading);
            progress.setVisibility(zauzet ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!zauzet);
        });

        // prijava uspesna, nazad na listu
        viewModel.getDone().observe(getViewLifecycleOwner(), done -> {
            if (Boolean.TRUE.equals(done)) {
                ((MainActivity) requireActivity()).vratiNaListu();
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String lozinka = etLozinka.getText().toString();

            boolean ispravno = true;
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Unesi ispravan email.");
                ispravno = false;
            }
            if (lozinka.isEmpty()) {
                etLozinka.setError("Unesi lozinku.");
                ispravno = false;
            }
            if (ispravno)
                viewModel.login(email, lozinka);
        });

        tvRegistracija.setOnClickListener(v ->
                ((MainActivity) requireActivity()).otvoriRegistraciju());
    }
}
