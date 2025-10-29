package com.example.smartfit2;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private View dashboardScroll;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        dashboardScroll = findViewById(R.id.dashboardScroll);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Show dashboard by default
        dashboardScroll.setVisibility(View.VISIBLE);
        fragmentContainer.setVisibility(View.GONE);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                // Show dashboard
                dashboardScroll.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
                return true;

            } else if (id == R.id.navWorkout) {
                // Show workouts fragment
                showFragment(new WorkoutsFragment());
                return true;

            } else if (id == R.id.nav_home) {
                // Show diet fragment
                showFragment(new DietFragment());
                return true;

            } else if (id == R.id.nav_progress) {
                // Show progress fragment
                showFragment(new ProgressFragment());
                return true;

            } else if (id == R.id.nav_profile) {
                // Show profile fragment
                showFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        // Hide dashboard
        dashboardScroll.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);

        // Replace current fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }
}
