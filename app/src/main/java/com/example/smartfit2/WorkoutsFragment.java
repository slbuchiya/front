package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutsFragment extends Fragment {

    private RecyclerView recyclerView;
    private WorkoutsAdapter workoutsAdapter;
    private ArrayList<Workout> workoutList;

    public WorkoutsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize workout list
        workoutList = new ArrayList<>();
        workoutList.add(new Workout("Arms", R.drawable.arms));
        workoutList.add(new Workout("Legs", R.drawable.legs));
        workoutList.add(new Workout("Chest", R.drawable.chest));
        workoutList.add(new Workout("Abs", R.drawable.abs));
        workoutList.add(new Workout("Back", R.drawable.back));

        // Adapter setup with click listener
        workoutsAdapter = new WorkoutsAdapter(workoutList, getContext(), workout -> {
            Intent intent;

            // Map workout name to corresponding Activity
            switch (workout.getName()) {
                case "Arms":
                    intent = new Intent(getContext(), ArmsActivity.class);
                    break;
                case "Legs":
                    intent = new Intent(getContext(), LegsActivity.class);
                    break;
                case "Chest":
                    intent = new Intent(getContext(), ChestActivity.class);
                    break;
                case "Abs":
                    intent = new Intent(getContext(), AbsActivity.class);
                    break;
                case "Back":
                    intent = new Intent(getContext(), BackActivity.class);
                    break;
                default:
                    // fallback: just show a toast instead of crashing
                    return;
            }

            startActivity(intent);
        });

        recyclerView.setAdapter(workoutsAdapter);
        return view;
    }
}
