package com.example.smartfit2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    private final List<Meal> mealList;
    private final Context context;

    public DietAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public DietAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietAdapter.ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealName.setText(meal.getName());
        holder.mealImage.setImageResource(meal.getImageResId());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = null;

            switch (meal.getName()) {
                case "Muscle Gain Diet":
                    intent = new Intent(context, MuscleGainDietActivity.class);
                    break;
                case "Weight Loss Diet":
                    intent = new Intent(context, WeightLossDietActivity.class);
                    break;
                case "Balanced Diet":
                    intent = new Intent(context, BalancedDietActivity.class);
                    break;
            }

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealName;
        ImageView mealImage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName);
            mealImage = itemView.findViewById(R.id.mealImage);
            cardView = (CardView) itemView;
        }
    }
}
