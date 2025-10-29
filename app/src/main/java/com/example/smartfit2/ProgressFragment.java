package com.example.smartfit2;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProgressFragment extends Fragment {

    private Button btnUpdateProgress;
    private TextView tvCurrentWeight, tvGoalWeight, tvBMI;
    private LineChart progressChart;

    private float currentWeight = 70f;
    private final float goalWeight = 65f;
    private float bmi = 22.4f;

    private final List<Float> weightHistory = new ArrayList<>();
    private final Random random = new Random();

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        // Initialize views
        btnUpdateProgress = view.findViewById(R.id.btnUpdateProgress);
        tvCurrentWeight = view.findViewById(R.id.tvCurrentWeight);
        tvGoalWeight = view.findViewById(R.id.tvGoalWeight);
        tvBMI = view.findViewById(R.id.tvBMI);
        progressChart = view.findViewById(R.id.progressChart);

        // Initial values
        tvCurrentWeight.setText(currentWeight + " kg");
        tvGoalWeight.setText(goalWeight + " kg");
        tvBMI.setText(String.format("%.1f", bmi));

        // Add initial weight to history and chart
        weightHistory.add(currentWeight);
        setChartData();

        // Update button click
        btnUpdateProgress.setOnClickListener(v -> {
            // Simulate weight change
            float change = (random.nextFloat() * 1f); // 0 to 1 kg
            currentWeight += change;
            bmi += change * 0.1f; // simulate BMI change

            // Update TextViews
            tvCurrentWeight.setText(String.format("%.1f kg", currentWeight));
            tvBMI.setText(String.format("%.1f", bmi));

            // Add new weight to history and update chart
            weightHistory.add(currentWeight);
            setChartData();
        });

        return view;
    }

    private void setChartData() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weightHistory.size(); i++) {
            entries.add(new Entry(i, weightHistory.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weight Progress");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        progressChart.setData(lineData);
        progressChart.getDescription().setEnabled(false);
        progressChart.getAxisRight().setEnabled(false);
        progressChart.invalidate(); // refresh chart
    }
}
