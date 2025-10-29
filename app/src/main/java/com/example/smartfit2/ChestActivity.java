package com.example.smartfit2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the new chest XML layout
        setContentView(R.layout.activity_chest);

        // Debug toast to confirm activity opened
        Toast.makeText(this, "ChestActivity started", Toast.LENGTH_SHORT).show();

        // Optional: light status bar for modern look
        getWindow().getDecorView().setSystemUiVisibility(
                getWindow().getDecorView().getSystemUiVisibility()
                        | android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
    }
}
