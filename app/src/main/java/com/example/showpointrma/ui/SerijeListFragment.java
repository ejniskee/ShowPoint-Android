package com.example.showpointrma.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showpointrma.MainActivity;
import com.example.showpointrma.R;
import com.example.showpointrma.data.local.SerijaEntity;
import com.example.showpointrma.util.TokenManager;
import com.example.showpointrma.viewmodel.SerijeViewModel;

import java.util.List;

// fragment: vid podaktivnosti - sopstveni ziv. cikl., prima svoje ul. dog.
// moze se dodavati, uklanjati, menjati dok maticna aktivnost radi

public class SerijeListFragment extends Fragment {

    private SerijeViewModel viewModel;
    private TokenManager tokenManager; //
    private SerijaAdapter adapter;
    private ProgressBar progress;
    private TextView tvEmpty;
    //
    private TextView tvKorisnik;
    private Button btnAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serije_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = new TokenManager(requireContext()); //
        
        progress = view.findViewById(R.id.progress);
        tvEmpty = view.findViewById(R.id.tv_empty);
        tvKorisnik = view.findViewById(R.id.tv_korisnik);
        btnAuth = view.findViewById(R.id.btn_auth);

        // samo privremeno
        btnAuth.setVisibility(View.VISIBLE);
        btnAuth.setText("TEST PRIJAVA");
        // --

        RecyclerView recycler = view.findViewById(R.id.recycler_serije);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SerijaAdapter(serija -> 
                // kasnije: otvoriti fragment sa detaljima serije
                //Toast.makeText(requireContext(), serija.naziv, Toast.LENGTH_SHORT).show()
                ((MainActivity) requireActivity()).otvoriDetalje(serija.id));

        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SerijeViewModel.class);

        // fragment posmatra bazu (preko ViewModela): kad se Room napuni, lista se sama osvezi
        viewModel.getSerije().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            updateEmptyState();
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
            updateEmptyState();
        });

        viewModel.getError().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();



                viewModel.clearError();
            }
        });

        // prijava / odjava

        btnAuth.setOnClickListener(v -> {
            if (tokenManager.isLoggedIn()) {
                tokenManager.clear();
                Toast.makeText(requireContext(), "Odjavljen si.", Toast.LENGTH_SHORT).show();
                updateAuthUI();
                ((MainActivity) requireActivity()).osveziDetalje();
            }
            else
                ((MainActivity) requireActivity()).otvoriLogin();
        });
        
    }

    // posle povratka sa login ekrana (nazad na listu) osvezi traku
    @Override
    public void onResume() {
        super.onResume();
        if (btnAuth != null)
            updateAuthUI();
    }

    public void updateAuthUI() {
        if (tokenManager.isLoggedIn()) {
            String ime = tokenManager.getIme();
            tvKorisnik.setText(ime == null ? "Prijavljen" : "Prijavljen: " + ime);
            tvKorisnik.setVisibility(View.VISIBLE);
            btnAuth.setText("Odjava");
        } else {
            tvKorisnik.setVisibility(View.GONE);
            btnAuth.setText("Prijava");
        }
    }

    private void updateEmptyState() {
        boolean loading = Boolean.TRUE.equals(viewModel.getLoading().getValue());
        List<SerijaEntity> list = viewModel.getSerije().getValue();
        boolean empty = list == null || list.isEmpty();
        tvEmpty.setVisibility(!loading && empty ? View.VISIBLE : View.GONE);
    }

}




























