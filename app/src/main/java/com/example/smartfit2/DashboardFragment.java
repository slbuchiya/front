package com.example.smartfit2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    TextView stepsText, caloriesText;
    Button btnRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        stepsText = view.findViewById(R.id.stepsText);
        caloriesText = view.findViewById(R.id.caloriesText);
        btnRefresh = view.findViewById(R.id.btnRefresh);

        // Refresh button demo
        btnRefresh.setOnClickListener(v -> {
            stepsText.setText("6,500");
            caloriesText.setText("320 kcal");
        });

        return view;
    }
}
