package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner; // જો Spinner વાપરતા હોવ તો
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword, etAge, etHeight, etWeight;
    Spinner spinnerGender; // જો Spinner હોય તો
    Button btnRegister;
    TextView tvLoginNow;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup); // [cite: SMARTFITGYM-main/app/src/main/res/layout/activity_signup.xml]

        // ‼ ‼ ‼
        // ચેતવણી: નીચેના IDs (R.id. ...) તમારા `front.git` ના
        // `res/layout/activity_signup.xml` [cite: SMARTFITGYM-main/app/src/main/res/layout/activity_signup.xml] ફાઇલ મુજબ હોવા જોઈએ.
        // જો ID અલગ હોય, તો અહીં બદલી નાખજો નહીંતર એપ ક્રેશ થશે.
        // ‼ ‼ ‼
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        spinnerGender = findViewById(R.id.spinnerGender); // XML માં આ ID હોવું જોઈએ
        btnRegister = findViewById(R.id.tvSignUp);
        tvLoginNow = findViewById(R.id.btnLogin);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        tvLoginNow.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        });

        btnRegister.setOnClickListener(v -> {
            registerUser();
        });
    }

    private void registerUser() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString(); // Spinner માંથી વેલ્યુ

        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        int height = Integer.parseInt(heightStr);
        int weight = Integer.parseInt(weightStr);

        // યુઝર ઑબ્જેક્ટ બનાવો
        User user = new User(fullName, email, password, age, gender, height, weight);

        Call<UserResponse> call = apiService.signupUser(user);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(SignupActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                    // રજીસ્ટ્રેશન પછી લોગિન પેજ પર મોકલો
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    Log.e("SignupActivity", "API Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(SignupActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("SignupActivity", "Network Failure: ", t);
            }
        });
    }
}
