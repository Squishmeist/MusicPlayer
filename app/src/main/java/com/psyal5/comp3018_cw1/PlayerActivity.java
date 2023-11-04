package com.psyal5.comp3018_cw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlayerActivity extends AppCompatActivity {
    private TextView songNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        songNameTextView = findViewById(R.id.songNameTextView);

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

        String songName = CurrentSongManager.getSongName();

        if (songName != null && !songName.isEmpty()) {
            songNameTextView.setText(songName);
        }
    }

}