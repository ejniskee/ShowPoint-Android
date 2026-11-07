package com.example.showpointrma.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.showpointrma.R;
import com.example.showpointrma.data.local.SerijaEntity;

import java.util.Locale;
import java.util.Objects;

/* viewholder je kao pomocna klasa za skladiste(kes) za reference na vizuelne elemente (Views)
    drasticno pojacava performanse */

// ListAdapter radi poredjenje liste u drugoj niti
public class SerijaAdapter extends ListAdapter<SerijaEntity, SerijaAdapter.ViewHolder> {

    public interface OnSerijaClickListener {
        void onSerijaClick(SerijaEntity serija);
    }

    private final OnSerijaClickListener listener;

    public SerijaAdapter(OnSerijaClickListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    // diffutil osvezava samo one redove koji su se promenili
    // poredi stari i novi objekat da bi odlucio sta treba menjati u listi
    // oldItem = trenutno prikazano; newItem = nova verzija podatka koju smo dobili
    private static final DiffUtil.ItemCallback<SerijaEntity> DIFF = new DiffUtil.ItemCallback<SerijaEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull SerijaEntity oldItem, @NonNull SerijaEntity newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override // ako se recimo promeni ocena od serija --> podaci se promenili
                public boolean areContentsTheSame(@NonNull SerijaEntity a, @NonNull SerijaEntity b) {
                    return Objects.equals(a.naziv, b.naziv)
                            && a.godinaPocetka == b.godinaPocetka
                            && Objects.equals(a.posterUrl, b.posterUrl)
                            && Objects.equals(a.prosecnaOcena, b.prosecnaOcena)
                            && Objects.equals(a.zanrovi, b.zanrovi);
                }

    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serija, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SerijaEntity s = getItem(position);

        holder.tvNaziv.setText(s.naziv);
        holder.tvPodnaslov.setText(s.godinaPocetka + " \u2022 " + s.zanrovi);

        // prosecna ocena moze biti null -- to je serija bez ocena

        if (s.prosecnaOcena == null)
            holder.tvOcena.setText("Nema ocena");
        else
            holder.tvOcena.setText(String.format(Locale.getDefault(), "\u2605 %.1f", s.prosecnaOcena));

        Glide.with(holder.imgPoster).load(s.posterUrl).centerCrop().into(holder.imgPoster);

        holder.itemView.setOnClickListener(v -> listener.onSerijaClick(s));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPoster;
        final TextView tvNaziv, tvPodnaslov, tvOcena;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvNaziv = itemView.findViewById(R.id.tv_naziv);
            tvPodnaslov = itemView.findViewById(R.id.tv_podnaslov);
            tvOcena = itemView.findViewById(R.id.tv_ocena);
        }
    }

}
