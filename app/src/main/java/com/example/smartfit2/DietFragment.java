package com.example.smartfit2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// આ બધા import સ્ટેટમેન્ટ્સ 'model' ફોલ્ડર વગરના છે
import com.example.smartfit2.ApiService;
import com.example.smartfit2.RetrofitClient;
import com.example.smartfit2.DietPlan;
import com.example.smartfit2.DietAdapter;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietFragment extends Fragment {

    RecyclerView recyclerView;
    DietAdapter adapter;
    List<DietPlan> dietPlanList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet, container, false);

        // --- આ સાચી રીત છે ---
        // XML Syntax ને બદલે Java Syntax વાપરો
        // ખાતરી કરો કે તમારા res/layout/fragment_diet.xml માં આ ID છે: "@+id/diet_recycler_view"
        recyclerView = view.findViewById(R.id.diet_recycler_view);
        // -------------------------

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 1. ખાલી લિસ્ટ સાથે એડેપ્ટર સેટ કરો
        dietPlanList = new ArrayList<>();
        adapter = new DietAdapter(getContext(), dietPlanList);
        recyclerView.setAdapter(adapter);

        // 2. API કૉલ કરીને ડેટા લાવો
        fetchDietPlansFromServer();

        return view;
    }

    private void fetchDietPlansFromServer() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<DietPlan>> call = apiService.getDietPlans();

        call.enqueue(new Callback<List<DietPlan>>() {
            @Override
            public void onResponse(Call<List<DietPlan>> call, Response<List<DietPlan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dietPlanList.clear();
                    dietPlanList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load diet plans", Toast.LENGTH_SHORT).show();
                    Log.e("DietFragment", "API Response Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<DietPlan>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DietFragment", "API Call Failure: ", t);
            }
        });
    }
}

