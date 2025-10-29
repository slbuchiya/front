package com.example.smartfit2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);

        // Debug toast (optional — remove when not needed)
        Toast.makeText(this, "BackActivity started", Toast.LENGTH_SHORT).show();

        // Optional: make status bar icons dark for light background
        getWindow().getDecorView().setSystemUiVisibility(
                getWindow().getDecorView().getSystemUiVisibility()
                        | android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
    }
}
