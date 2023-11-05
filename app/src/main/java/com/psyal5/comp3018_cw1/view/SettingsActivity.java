package com.psyal5.comp3018_cw1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation); // Initialize and assign variable
        bottomNavigationView.setSelectedItemId(R.id.settings); // Set selected screen on navbar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.main) {
                    // Start Main Activity
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.player) {
                    // Start Player Activity
                    startActivity(new Intent(SettingsActivity.this, PlayerActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.settings) {
                    // Start Settings Activity
                    return true;
                } else {
                    return false;
                }
            }
        }); // Perform item selected listener
    }
}