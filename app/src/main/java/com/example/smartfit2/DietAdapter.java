package com.example.smartfit2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfit2.DietPlan; // .model વગર

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    private List<DietPlan> dietPlanList; // DietPlan વાપરો
    private Context context;

    public DietAdapter(Context context, List<DietPlan> dietPlanList) {
        this.context = context;
        this.dietPlanList = dietPlanList;
    }

    @NonNull
    @Override
    public DietAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_item, parent, false); // [cite: SMARTFITGYM-main/app/src/main/res/layout/diet_item.xml]
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietAdapter.ViewHolder holder, int position) {
        DietPlan plan = dietPlanList.get(position);
        holder.mealName.setText(plan.getPlanName());

        // ઈમેજ માટે લોજિક
        String goal = plan.getGoal(); // "Weight Loss", "Weight Gain"

        if ("Weight Loss".equals(goal)) {
            holder.mealImage.setImageResource(R.drawable.wl); // [cite: SMARTFITGYM-main/app/src/main/res/drawable/wl.png]
        } else if ("Weight Gain".equals(goal)) {
            holder.mealImage.setImageResource(R.drawable.mg2); // [cite: SMARTFITGYM-main/app/src/main/res/drawable/mg2.png]
        } else {
            holder.mealImage.setImageResource(R.drawable.bd); // [cite: SMARTFITGYM-main/app/src/main/res/drawable/bd.png]
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            String planName = plan.getPlanName();
            if (planName.contains("Muscle Gain") || "Weight Gain".equals(goal)) {
                Intent intent = new Intent(context, MuscleGainDietActivity.class); // [cite: SMARTFITGYM-main/app/src/main/java/com/example/smartfit2/MuscleGainDietActivity.java]
                context.startActivity(intent);
            } else if (planName.contains("Weight Loss") || "Weight Loss".equals(goal)) {
                Intent intent = new Intent(context, WeightLossDietActivity.class); // [cite: SMARTFITGYM-main/app/src/main/java/com/example/smartfit2/WeightLossDietActivity.java]
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, BalancedDietActivity.class); // [cite: SMARTFITGYM-main/app/src/main/java/com/example/smartfit2/BalancedDietActivity.java]
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietPlanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName); // [cite: SMARTFITGYM-main/app/src/main/res/layout/diet_item.xml]
            mealImage = itemView.findViewById(R.id.mealImage); // [cite: SMARTFITGYM-main/app/src/main/res/layout/diet_item.xml]
        }
    }
}
