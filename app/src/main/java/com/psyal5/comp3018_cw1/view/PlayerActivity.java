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
    private static final String TAG = "CW1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "On create [Player]");

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
        TextView songName = findViewById(R.id.songNameTextView);
        ImageButton stopButton = findViewById(R.id.stopButton);
        ImageButton playButton = findViewById(R.id.playButton);
        ImageButton pauseButton = findViewById(R.id.pauseButton);

        playerViewModel.songName.observe(this, s -> {
            // Update song name UI using data binding or findViewById
            songName.setText(s);
        });

        stopButton.setOnClickListener(v -> {
            playerViewModel.onStopButtonClicked();
            songName.setText("No song");
            stopMusicService();
        });

        playButton.setOnClickListener(v -> {
            playerViewModel.onPlayButtonClicked();
        });

        pauseButton.setOnClickListener(v -> {
            playerViewModel.onPauseButtonClicked();
        });

    }

    private void stopMusicService() {
        Log.d(TAG, "stopService [Player]");
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override  // Triggered when the service is successfully bound
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected [Player]");
            // Linking the service to musicService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            MusicService musicService = binder.getService();
            Log.d(TAG, "isBound set to true [Player]");
            isBound = true; // Flag that the binding is successful.
            playerViewModel.setMusicService(musicService);
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
            playerViewModel.setMusicService(null);
        }
    };


    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called [Player]");
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "OnStop called [Player]");
        super.onStop();
        stopMusicService();
        playerViewModel = null;
    }
}