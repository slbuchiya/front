package com.example.smartfit2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AbsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs);

        // Debug Toast (you can remove later)
        Toast.makeText(this, "AbsActivity started", Toast.LENGTH_SHORT).show();

        // Optional: make status bar text dark for white backgrounds
        getWindow().getDecorView().setSystemUiVisibility(
                getWindow().getDecorView().getSystemUiVisibility()
                        | android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
    }
}
