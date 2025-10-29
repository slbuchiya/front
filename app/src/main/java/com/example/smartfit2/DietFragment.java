package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DietFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        // Click listeners for each card
        view.findViewById(R.id.cardMuscleGain).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MuscleGainDietActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.cardWeightLoss).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WeightLossDietActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.cardBalanced).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), BalancedDietActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
