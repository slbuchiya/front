package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FitnessGoalActivity extends AppCompatActivity {

    private String selectedGoal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitness_goal);

        // Find Views
        LinearLayout cardLoseWeight = findViewById(R.id.cardLoseWeight);
        LinearLayout cardBuildMuscle = findViewById(R.id.cardBuildMuscle);
        LinearLayout cardStayFit = findViewById(R.id.cardStayFit);
        Button btnContinue = findViewById(R.id.btnContinue);

        // Card click listeners
        cardLoseWeight.setOnClickListener(v -> {
            selectedGoal = "Lose Weight";
            highlightSelected(cardLoseWeight, cardBuildMuscle, cardStayFit);
        });

        cardBuildMuscle.setOnClickListener(v -> {
            selectedGoal = "Build Muscle";
            highlightSelected(cardBuildMuscle, cardLoseWeight, cardStayFit);
        });

        cardStayFit.setOnClickListener(v -> {
            selectedGoal = "Stay Fit";
            highlightSelected(cardStayFit, cardLoseWeight, cardBuildMuscle);
        });

        // Continue button logic
        btnContinue.setOnClickListener(v -> {
            if (selectedGoal.isEmpty()) {
                Toast.makeText(this, "Please select a goal first", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(FitnessGoalActivity.this, MainActivity.class);
                intent.putExtra("goal", selectedGoal);
                startActivity(intent);
                finish();
            }
        });
    }

    // Method to visually highlight selected goal
    private void highlightSelected(LinearLayout selected, LinearLayout... others) {
        selected.setBackgroundColor(getColor(R.color.teal_200));
        for (LinearLayout other : others) {
            other.setBackgroundResource(R.drawable.goal_card_bg);
        }
    }
}
