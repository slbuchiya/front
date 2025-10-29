package com.example.smartfit2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutViewHolder> {

    private final ArrayList<Workout> workouts;
    private final Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Workout workout);
    }

    public WorkoutsAdapter(ArrayList<Workout> workouts, Context context, OnItemClickListener listener) {
        this.workouts = workouts;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.textView.setText(workout.getName());
        holder.imageView.setImageResource(workout.getImageResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(workout));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            // ✅ Updated IDs according to your item_workout.xml
            imageView = itemView.findViewById(R.id.imageViewWorkout);
            textView = itemView.findViewById(R.id.textViewWorkoutName);
        }
    }
}
