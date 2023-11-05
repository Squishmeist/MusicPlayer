package com.psyal5.comp3018_cw1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    private TextView songNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        songNameTextView = findViewById(R.id.songNameTextView);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.player);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(PlayerActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.player) {
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else {
                return false;
            }
        });

        playerViewModel.getSongName().observe(this, songName -> {
            if (songName != null && !songName.isEmpty()) {
                songNameTextView.setText(songName);
            }
        });
    }
}