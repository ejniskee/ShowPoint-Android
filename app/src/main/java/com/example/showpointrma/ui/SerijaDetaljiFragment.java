package com.example.showpointrma.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.showpointrma.R;
import com.example.showpointrma.model.Ocena;
import com.example.showpointrma.model.SerijaDetalji;
import com.example.showpointrma.util.TokenManager;
import com.example.showpointrma.viewmodel.SerijaDetaljiViewModel;

import java.util.Locale;

public class SerijaDetaljiFragment extends Fragment {

    private static final String ARG_ID = "serija_id";

    // fragment dobija samo id serije, ostalo ucitava viewmodel
    public static SerijaDetaljiFragment newInstance(int serijaId) {
        Bundle args = new Bundle(); // bezbedno pakovanje, vezuje ih za fragment
        args.putInt(ARG_ID, serijaId);
        SerijaDetaljiFragment fragment = new SerijaDetaljiFragment(); // prazan konst. bez arg.
        fragment.setArguments(args);
        return fragment;
    }

    private SerijaDetaljiViewModel viewModel;
    private TokenManager tokenManager;
    private EpizodaAdapter adapter;
    private SerijaDetalji trenutni;

    private ImageView imgPoster;
    private TextView tvNaziv, tvPodnaslov, tvOcena, tvMojaOcena, tvProgres, tvOpis;
    private Button btnOceni, btnEvidentiraj, btnTrailer;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serija_detalji, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = new TokenManager(requireContext());

        imgPoster = view.findViewById(R.id.img_poster);
        tvNaziv = view.findViewById(R.id.tv_naziv);
        tvPodnaslov = view.findViewById(R.id.tv_podnaslov);
        tvOcena = view.findViewById(R.id.tv_ocena);
        tvMojaOcena = view.findViewById(R.id.tv_moja_ocena);
        tvProgres = view.findViewById(R.id.tv_progres);
        tvOpis = view.findViewById(R.id.tv_opis);

        btnOceni = view.findViewById(R.id.btn_oceni);
        btnEvidentiraj = view.findViewById(R.id.btn_evidentiraj);
        btnTrailer = view.findViewById(R.id.btn_trailer);

        progress = view.findViewById(R.id.progress);

        RecyclerView recycler = view.findViewById(R.id.recycler_epizode);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new EpizodaAdapter((epizoda, novoStanje) ->
                viewModel.toggleEpizoda(epizoda, novoStanje));
        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(SerijaDetaljiViewModel.class);
        viewModel.init(requireArguments().getInt(ARG_ID));

        viewModel.getDetalji().observe(getViewLifecycleOwner(), this::prikazi);

        viewModel.getLoading().observe(getViewLifecycleOwner(), loading ->
                progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE));

        viewModel.getMessage().observe(getViewLifecycleOwner(), msg -> {
                if (msg != null) {
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    viewModel.clearMessage();
                }
        });

        btnOceni.setOnClickListener(v -> {
            if (!tokenManager.isLoggedIn())
                Toast.makeText(requireContext(), "Prijavite se da bi ocenili seriju!", Toast.LENGTH_SHORT).show();
            else
                prikaziDijalogOcene();
        });

        btnEvidentiraj.setOnClickListener(v -> {
            if (!tokenManager.isLoggedIn()) {
                Toast.makeText(requireContext(), "Prijavite se da bi evidentirali gledanje!", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean novi = !adapter.isTrackingMode();
            adapter.setTrackingMode(novi);
            btnEvidentiraj.setText(novi ? "Gotovo" : "Evidentiraj");

            if (novi)
                Toast.makeText(requireContext(), "Dodirni epizodu da oznacis ili ponistis.", Toast.LENGTH_SHORT).show();
        });

        btnTrailer.setOnClickListener(v -> {
            if (trenutni == null)
                return;

            Uri uri = Uri.parse("https://www.youtube.com/results?search_query="
                        + Uri.encode(trenutni.getNaziv() + " trailer"));

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(requireContext(), "Nema aplikacije za otvaranje URL-a.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prikazi(SerijaDetalji d) {
        if (d == null)
            return;

        trenutni = d;

        tvNaziv.setText(d.getNaziv());

        String zanrovi = d.getZanrovi() == null ? "" : TextUtils.join(", ", d.getZanrovi());
        tvPodnaslov.setText(d.getGodinaPocetka() + " \u2022 " + zanrovi);

        if (d.getProsecnaOcena() == null)
            tvOcena.setText("Nema ocena");
        else {
            tvOcena.setText(String.format(Locale.getDefault(),
                    "Prosecna ocena: \u2605 %.1f", d.getProsecnaOcena()));
        }

        tvOpis.setText(d.getOpis());
        Glide.with(imgPoster).load(d.getPosterUrl()).centerCrop().into(imgPoster);

        // moja ocena - samo ako je ulogovan i ocenio
        Ocena moja = d.getMojaOcena();
        if (moja != null) {
            String tekst = "Tvoja ocena: " + moja.getVrednost() + "/5";

            if (moja.getKomentar() != null && !moja.getKomentar().isEmpty())
                tekst += "\n\u201e" + moja.getKomentar() + "\u201c";

            tvMojaOcena.setText(tekst);
            tvMojaOcena.setVisibility(View.VISIBLE);
            btnOceni.setText("Promeni ocenu");
        } else {
            tvMojaOcena.setVisibility(View.GONE);
            btnOceni.setText("Oceni seriju");
        }

        // progress bar samo ako je ulogovan

        if (d.getProcenat() != null) {
            tvProgres.setText(String.format(Locale.getDefault(), "Odgledano: %.0f%%", d.getProcenat()));
            tvProgres.setVisibility(View.VISIBLE);
        }
        else
            tvProgres.setVisibility(View.GONE);

        adapter.submitList(EpizodaAdapter.napraviRedove(d.getSezone()));
    }

    private void prikaziDijalogOcene() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_ocena, null);
        RatingBar rating = dialogView.findViewById(R.id.rating_bar);
        EditText etKomentar = dialogView.findViewById(R.id.et_komentar);

        // ako je vec ocenjeno, popuni postojecu ocenu i komentar
        Ocena moja = trenutni == null ? null : trenutni.getMojaOcena();
        if (moja != null) {
            rating.setRating(moja.getVrednost());
            etKomentar.setText(moja.getKomentar());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Oceni seriju")
                .setView(dialogView)
                .setPositiveButton("Sacuvaj", null) // klik se postavlja ispod, da validac. ne skloni dijalog
                .setNegativeButton("Otkazi", null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int vrednost = Math.round(rating.getRating());
            if (vrednost < 1) {
                Toast.makeText(requireContext(), "Izaberi ocenu od 1 do 5", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.oceni(vrednost, etKomentar.getText().toString().trim());
            dialog.dismiss();
        });
    }
}











