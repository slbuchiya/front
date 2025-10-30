package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// ખાતરી કરો કે આ import સાચા છે ('model' ફોલ્ડર વગર)
import com.example.smartfit2.ApiService;
import com.example.smartfit2.RetrofitClient;
import com.example.smartfit2.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterNow; // tvSignup ને બદલે
    ApiService apiService; // API સર્વિસ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvSignUp); // XML માં tvSignup છે
        // ------------------------------------

        // Retrofit ક્લાયન્ટ
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // રજીસ્ટર પેજ પર જવા માટે
        tvRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        // લોગિન બટન ક્લિક
        btnLogin.setOnClickListener(v -> {
            loginUser();
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // લોગિન રિક્વેસ્ટ ઑબ્જેક્ટ
        ApiService.LoginRequest loginRequest = new ApiService.LoginRequest(email, password);

        Call<UserResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                // તમારા UserResponse.java મુજબ 'isSuccess()' ચેક કરો
                if (response.isSuccessful() && response.body() != null) {
                    // જો લોગિન સફળ થાય (તમારા બેકએન્ડ મુજબ 'message' અથવા 'success' ચેક કરો)
                    // અહીં હું માની લઉં છું કે સફળ થવા પર સારો response code (200) આવે છે

                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    // લોગિન થયા પછી MainActivity પર જાઓ
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // લોગિન એક્ટિવિટી બંધ કરો
                } else {
                    // જો રિસ્પોન્સમાં એરર હોય (દા.ત., 401 Unauthorized)
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "API Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // જો નેટવર્ક એરર હોય
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Network Failure: ", t);
            }
        });
    }
}
