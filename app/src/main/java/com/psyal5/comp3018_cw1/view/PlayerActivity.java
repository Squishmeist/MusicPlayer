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
import android.util.Log;
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
    private static final String TAG = "CW1-player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "On create");

        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        playerViewModel = new ViewModelProvider(PlayerActivity.this).get(PlayerViewModel.class);
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        setupBottomNavigation();
        observeViewModel();
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

    private void observeViewModel() {
        playerViewModel.songName.observe(this, s -> {
            // Update song name UI using data binding or findViewById
            TextView songName = findViewById(R.id.songNameTextView);
            songName.setText(s);
        });

        ImageButton statusButton = findViewById(R.id.statusButton);
        playerViewModel.isPlaying.observe(this, isPlaying -> {
            // Update status button UI based on isPlaying using data binding or findViewById
            statusButton.setImageResource(isPlaying ? R.drawable.ic_pause_foreground : R.drawable.ic_play_foreground);
        });

        statusButton.setOnClickListener(v -> {
            if (playerViewModel.getIsPlaying()) {
                // If the song is already playing, pause it
                playerViewModel.pauseMusic();
                stopMusicService();
            } else {
                // If the song is not playing, play it
                startMusicService();
                playerViewModel.playMusic();
            }
        });

        ImageButton stopButton = findViewById(R.id.stopButton);
        stopButton.setOnClickListener(v -> {
            playerViewModel.onStopButtonClicked();
            stopMusicService();
        });
    }

    private void startMusicService() {
        Log.d(TAG, "startService");
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void stopMusicService() {
        Log.d(TAG, "stopService");
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override  // Triggered when the service is successfully bound
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            // Linking the service to musicService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            MusicService musicService = binder.getService();
            Log.d(TAG, "isBound set to true");
            isBound = true; // Flag that the binding is successful.
            playerViewModel.setMusicService(musicService);
            if (playerViewModel.getIsPlaying()) {
                // Start the service when already playing
                startMusicService();
            }
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
        }
    };


    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called");
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "OnStop called");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
        playerViewModel = null;
    }
}