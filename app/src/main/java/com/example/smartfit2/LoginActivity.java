package com.example.smartfit2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    ImageView ivToggle;
    Button btnLogin;
    TextView tvSignUp;
    boolean pwdVisible = false;

    private static final String BASE_URL = "https://smartfit-backend-qwq8.onrender.com/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivToggle = findViewById(R.id.ivToggle);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        ivToggle.setOnClickListener(v -> {
            pwdVisible = !pwdVisible;
            if (pwdVisible) {
                etPassword.setTransformationMethod(null);
                ivToggle.setImageResource(R.drawable.ic_visibility_24);
            } else {
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                ivToggle.setImageResource(R.drawable.ic_visibility_off_24);
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter email");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                etPassword.setError("Enter password");
                return;
            }

            sendLoginRequest(email, pass);
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void sendLoginRequest(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {
            Toast.makeText(this, "Error preparing login data", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        Log.d("LOGIN_FLOW", "Sending login request...");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Server connection failed", Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN_FLOW", "Login failed: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body() != null ? response.body().string() : "";
                Log.d("LOGIN_FLOW", "Response: " + responseData);

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        SharedPreferences sp = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        sp.edit().putString("user_email", email).apply();

                        Log.d("LOGIN_FLOW", "Fetching name for email: " + email);
                        fetchNameAndProceed(email);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN_FLOW", "Response not successful");
                    }
                });
            }
        });
    }

    private void fetchNameAndProceed(String email) {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "/users?email=" + email;
        Log.d("LOGIN_FLOW", "Fetching user details from: " + url);

        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LOGIN_FLOW", "Fetch name failed: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() == null) {
                    Log.e("LOGIN_FLOW", "Response body null while fetching name");
                    return;
                }
                String res = response.body().string();
                Log.d("LOGIN_FLOW", "User fetch response: " + res);

                try {
                    JSONObject user = new JSONObject(res);
                    String fullName = user.getString("full_name");
                    Log.d("LOGIN_FLOW", "Full name fetched: " + fullName);

                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                            .edit()
                            .putString("name", fullName)
                            .apply();

                    runOnUiThread(() -> {
                        Log.d("LOGIN_FLOW", "Launching MainActivity now...");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                } catch (Exception e) {
                    Log.e("LOGIN_FLOW", "Error parsing user data: " + e);
                }
            }
        });
    }
}
