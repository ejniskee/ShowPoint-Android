package com.example.showpointrma.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.showpointrma.R;
import com.example.showpointrma.model.Epizoda;
import com.example.showpointrma.model.Sezona;

import java.util.ArrayList;
import java.util.List;

public class EpizodaAdapter extends ListAdapter<EpizodaAdapter.Row, RecyclerView.ViewHolder> {

    public interface OnToggleListener {
        void onToggle(Epizoda epizoda, boolean novoStanje);
    }

    public static class Row {
        final String key;
        final boolean header;
        final String tekst;
        final boolean odgledana;
        final Epizoda epizoda;

        Row(String key, boolean header, String tekst, boolean odgledana, Epizoda epizoda) {
            this.key = key;
            this.header = header;
            this.tekst = tekst;
            this.odgledana = odgledana;
            this.epizoda = epizoda;
        }
    }

    public static List<Row> napraviRedove(List<Sezona> sezone) {
        List<Row> rows = new ArrayList<>();
        if (sezone == null)
            return rows;

        for (Sezona s : sezone) {
            rows.add(new Row("s" + s.getId(), true, "Sezona " + s.getRedniBroj(), false, null));

            if (s.getEpizode() == null)
                continue;

            for (Epizoda e : s.getEpizode()) {
                String tekst = "S" + s.getRedniBroj() + "E" + e.getRedniBroj() + "  "
                        + e.getNaziv() + " (" + e.getTrajanjeMin() + " min)";

                rows.add(new Row("e" + e.getId(), false, tekst, e.isOdgledana(), e));
            }

        }

        return rows;
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_EPIZODA = 1;

    private final OnToggleListener listener;
    private boolean trackingMode = false; // true: dodir na epizodu je oznaci ili ponisti

    public EpizodaAdapter(OnToggleListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    public void setTrackingMode(boolean trackingMode) {
        this.trackingMode = trackingMode;
        notifyDataSetChanged();
    }

    public boolean isTrackingMode() {
        return trackingMode;
    }

    private static final DiffUtil.ItemCallback<Row> DIFF = new DiffUtil.ItemCallback<Row>() {
        @Override
        public boolean areItemsTheSame(@NonNull Row oldItem, @NonNull Row newItem) {
            return oldItem.key.equals(newItem.key);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Row oldItem, @NonNull Row newItem) {
            return oldItem.tekst.equals(newItem.tekst) && oldItem.odgledana == newItem.odgledana;
        }
    };

    @Override
    public int getItemViewType(int position) {
        return getItem(position).header ? TYPE_HEADER : TYPE_EPIZODA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER)
            return new HeaderHolder(inflater.inflate(R.layout.item_epizoda_header, parent, false));

        return new EpizodaHolder(inflater.inflate(R.layout.item_epizoda, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Row row = getItem(position);

        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).tv.setText(row.tekst);
            return;
        }

        EpizodaHolder h = (EpizodaHolder) holder;
        h.tv.setText(row.tekst);
        h.cb.setChecked(row.odgledana); // CB je samo prikaz, stanje dolazi sa servera

        // klik radi samo u modu evidentiranja
        h.itemView.setClickable(trackingMode);
        h.itemView.setOnClickListener(v -> {
            if (trackingMode)
                listener.onToggle(row.epizoda, !row.odgledana);
        });
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        final TextView tv;

        HeaderHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_sezona);
        }
    }

    static class EpizodaHolder extends RecyclerView.ViewHolder {
        final CheckBox cb;
        final TextView tv;

        public EpizodaHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb_odgledana);
            tv = itemView.findViewById(R.id.tv_epizoda);
        }
    }

}











