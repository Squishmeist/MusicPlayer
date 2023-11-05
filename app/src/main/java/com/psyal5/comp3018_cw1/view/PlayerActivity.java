package com.psyal5.comp3018_cw1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.player);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(PlayerActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if (itemId == R.id.player) {
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else {
                return false;
            }
        });

        updateSongName();
        updateButtonState(playerViewModel.getState());

    }

    public void onStatusButtonClick(View view) {
        Log.d("CW1", "button pressed");
    }

    // Method to update the song name based on the song being played
    private void updateSongName(){
        TextView songNameTextView = findViewById(R.id.songNameTextView);
        playerViewModel.getSongName().observe(this, songName -> {
            if (songName != null && !songName.isEmpty()) {
                songNameTextView.setText(songName);
            }
        });
    }

    // Method to update the button state based on the MP3Player's state
    private void updateButtonState(String state) {
        ImageButton statusButton = findViewById(R.id.statusButton);
        switch (state) {
            case "PLAYING":
                statusButton.setImageResource(R.drawable.ic_pause_foreground);
                break;
            case "PAUSED":
                statusButton.setImageResource(R.drawable.ic_play_foreground);
                break;
            default:
                statusButton.setImageResource(R.drawable.ic_pause_foreground);
                break;
        }
    }
}