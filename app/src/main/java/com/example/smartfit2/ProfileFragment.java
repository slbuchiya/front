package com.example.smartfit2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvAge, tvGender, tvHeight, tvWeight;
    private Button btnEditProfile, btnLogout;

    private static final String BASE_URL = "https://smartfit-backend-qwq8.onrender.com/api";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAge = view.findViewById(R.id.tvAge);
        tvGender = view.findViewById(R.id.tvGender);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnEditProfile.setOnClickListener(v -> {
            if(getActivity() == null) return;
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            if(getActivity() == null) return;

            SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            sp.edit().clear().apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        loadProfileFromLocal();

        return view;
    }

    private void loadProfileFromLocal() {
        if(getActivity() == null) return;

        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        tvName.setText(sp.getString("name", ""));
        tvEmail.setText(sp.getString("user_email", ""));
        tvAge.setText(sp.getString("age", "") + " years");
        tvGender.setText(sp.getString("gender", ""));
        tvHeight.setText(sp.getString("height", "") + " cm");
        tvWeight.setText(sp.getString("weight", "") + " kg");
    }

    private void fetchProfileFromBackend() {
        if(getActivity() == null) return;

        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userEmail = sp.getString("user_email", null);
        if(userEmail == null) return;

        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "/users?email=" + userEmail;

        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(getActivity() != null)
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Failed to fetch profile", Toast.LENGTH_SHORT).show());
                Log.e("ProfileError", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body() == null) return;
                String res = response.body().string();
                Log.d("ProfileResponse", res);

                try {
                    JSONObject user = new JSONObject(res);

                    String name = user.getString("full_name");
                    int age = user.getInt("age");
                    String gender = user.getString("gender");
                    int height = user.getInt("height");
                    int weight = user.getInt("weight");

                    if(getActivity() != null)
                        getActivity().runOnUiThread(() -> {
                            tvName.setText(name);
                            tvAge.setText(age + " years");
                            tvGender.setText(gender);
                            tvHeight.setText(height + " cm");
                            tvWeight.setText(weight + " kg");
                        });

                    // Update local SharedPreferences
                    sp.edit()
                            .putString("name", name)
                            .putString("age", String.valueOf(age))
                            .putString("gender", gender)
                            .putString("height", String.valueOf(height))
                            .putString("weight", String.valueOf(weight))
                            .apply();

                } catch (Exception e) {
                    Log.e("ProfileError", "Parsing error: " + e);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfileFromLocal();       // Show latest local data immediately
        fetchProfileFromBackend();    // Sync backend in background
    }
}
