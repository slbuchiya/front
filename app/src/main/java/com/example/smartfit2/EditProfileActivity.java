package com.example.smartfit2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {

    EditText etFullName, etAge, etHeight, etWeight;
    Spinner spinnerGender;
    Button btnSaveProfile;

    private static final String BASE_URL = "https://smartfit-backend-qwq8.onrender.com/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFullName = findViewById(R.id.etFullName);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // Spinner options
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);

        // Load existing data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        etFullName.setText(sp.getString("name", ""));
        etAge.setText(sp.getString("age", ""));
        etHeight.setText(sp.getString("height", ""));
        etWeight.setText(sp.getString("weight", ""));
        String genderValue = sp.getString("gender", "Select Gender");
        int spinnerPosition = adapter.getPosition(genderValue);
        spinnerGender.setSelection(spinnerPosition);

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        String name = etFullName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();

        if(name.isEmpty() || age.isEmpty() || gender.equals("Select Gender") ||
                height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get userId from SharedPreferences
        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sp.getString("user_id", null);
        if(userId == null) {
            Toast.makeText(this, "User ID not found, please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare JSON
        JSONObject json = new JSONObject();
        try {
            json.put("full_name", name);
            json.put("age", Integer.parseInt(age));
            json.put("gender", gender);
            json.put("height", Integer.parseInt(height));
            json.put("weight", Integer.parseInt(weight));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error preparing data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send PUT request to backend with userId
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/users/" + userId)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    Log.e("EditProfile", "Request failed", e);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body() != null ? response.body().string() : "";
                Log.d("EditProfile", "Response code: " + response.code() + ", Body: " + res);

                runOnUiThread(() -> {
                    if(response.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        // Update SharedPreferences
                        sp.edit()
                                .putString("name", name)
                                .putString("age", age)
                                .putString("gender", gender)
                                .putString("height", height)
                                .putString("weight", weight)
                                .apply();

                        finish(); // Close activity and return to ProfileFragment
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        Log.e("EditProfile", "Failed to update. Status code: " + response.code() + ", Body: " + res);
                    }
                });
            }
        });
    }
}
