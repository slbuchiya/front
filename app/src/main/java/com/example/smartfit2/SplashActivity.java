package com.example.smartfit2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Go to LoginActivity after splash
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish(); // close splash so user can't go back to it
            }
        }, SPLASH_TIME_OUT);
    }
}
