package com.example.showpointrma.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// za tablet: sadrzaj desnog panela dok nijedna serija nije izabrana!
public class PrazanEkranFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(requireContext());
        tv.setText("Izaberi seriju iz liste...");
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);
        return tv;
    }
}
