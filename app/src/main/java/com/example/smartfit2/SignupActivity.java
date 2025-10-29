package com.example.smartfit2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SignupActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword, etAge, etHeight, etWeight;
    private ImageView ivToggle, ivToggleConfirm;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSignup;
    private TextView tvSignIn;
    private boolean pwdVisible = false, confirmPwdVisible = false;

    private static final String BASE_URL = "https://smartfit-backend-qwq8.onrender.com/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        setupPasswordToggles();

        btnSignup.setOnClickListener(v -> handleSignup());
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        ivToggle = findViewById(R.id.ivToggle);
        ivToggleConfirm = findViewById(R.id.ivToggleConfirm);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSignup = findViewById(R.id.btnSignup);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void setupPasswordToggles() {
        ivToggle.setOnClickListener(v -> {
            pwdVisible = !pwdVisible;
            etPassword.setInputType(pwdVisible ?
                    android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.length());
            ivToggle.setImageResource(pwdVisible ? R.drawable.ic_visibility_24 : R.drawable.ic_visibility_off_24);
        });

        ivToggleConfirm.setOnClickListener(v -> {
            confirmPwdVisible = !confirmPwdVisible;
            etConfirmPassword.setInputType(confirmPwdVisible ?
                    android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etConfirmPassword.setSelection(etConfirmPassword.length());
            ivToggleConfirm.setImageResource(confirmPwdVisible ? R.drawable.ic_visibility_24 : R.drawable.ic_visibility_off_24);
        });
    }

    private void handleSignup() {
        String name = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String gender = rbMale.isChecked() ? "Male" : rbFemale.isChecked() ? "Female" : "";

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)
                || TextUtils.isEmpty(confirmPass) || TextUtils.isEmpty(age)
                || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight) || TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Email format validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        sendSignupRequest(name, email, pass, age, gender, height, weight);
    }

    private void sendSignupRequest(String name, String email, String password, String age,
                                   String gender, String height, String weight) {

        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("full_name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("age", Integer.parseInt(age));
            jsonObject.put("gender", gender);
            jsonObject.put("height", Integer.parseInt(height));
            jsonObject.put("weight", Integer.parseInt(weight));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating signup data", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(
                jsonObject.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(SignupActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
                Log.e("SignupError", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body() != null ? response.body().string() : "";
                Log.d("SignupResponse", responseData);

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        // Save email & name to SharedPreferences
                        getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("user_email", email)
                                .putString("name", name)
                                .apply();

                        Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, FitnessGoalActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "Signup failed: " + responseData, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
