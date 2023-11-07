package com.psyal5.comp3018_cw1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityPlayerBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    private boolean isBound = false;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);
        bindMusicService();
        observeViewModel();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.player);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            return handleBottomNavigationActions(item.getItemId());
        });
    }

    private boolean handleBottomNavigationActions(int itemId) {
        // Handle different navigation options
        if (itemId == R.id.main) {
            startActivity(new Intent(PlayerActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }

    private void bindMusicService() {
        // A bridge or listener between the MainActivity and the MusicService.
        serviceConnection = new ServiceConnection() {
            @Override  // Triggered when the service is successfully bound
            public void onServiceConnected(ComponentName className, IBinder service) {
                // Linking the service to musicService variable.
                MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
                MusicService musicService = binder.getService();
                playerViewModel.setMusicService(musicService);
                isBound = true; // Flag that the binding is successful.
            }

            @Override //Triggered if the service unexpectedly disconnects
            public void onServiceDisconnected(ComponentName name) {
                isBound = false; // Flagging that the binding is no longer active.
            }
        };

        if (!isBound) {
            Intent intent = new Intent(PlayerActivity.this, MusicService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }


    private void observeViewModel() {
        playerViewModel.getSongName().observe(this, s -> {
            // Update song name UI using data binding or findViewById
            TextView songName = findViewById(R.id.songNameTextView);
            songName.setText(s);
        });

        playerViewModel.getIsPlaying().observe(this, isPlaying -> {
            // Update status button UI based on isPlaying using data binding or findViewById
            ImageButton statusButton = findViewById(R.id.statusButton);
            if (isPlaying) {
                statusButton.setImageResource(R.drawable.ic_pause_foreground);
            } else {
                statusButton.setImageResource(R.drawable.ic_play_foreground);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playerViewModel = null;
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}