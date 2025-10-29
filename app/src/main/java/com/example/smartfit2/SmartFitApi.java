package com.example.smartfit2;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class SmartFitApi {

    private static final String BASE_URL = "https://smartfit-backend-qwq8.onrender.com/api";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    // 🔹 REGISTER
    public static void registerUser(String fullName, String email, String password,
                                    int age, String gender, int height, int weight,
                                    ApiCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("full_name", fullName);
            json.put("email", email);
            json.put("password", password);
            json.put("age", age);
            json.put("gender", gender);
            json.put("height", height);
            json.put("weight", weight);
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .post(body)
                .build();

        executeRequest(request, callback);
    }

    // 🔹 LOGIN
    public static void loginUser(String email, String password, ApiCallback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        executeRequest(request, callback);
    }

    // 🔹 Internal executor
    private static void executeRequest(Request request, ApiCallback callback) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                callback.onSuccess(response.code(), res);
            }
        });
    }

    // 🔹 Callback interface
    public interface ApiCallback {
        void onSuccess(int statusCode, String response);
        void onFailure(String error);
    }
}
