package com.psyal5.comp3018_cw1.view;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityPlayerBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private PlayerViewModel playerViewModel;
    private MusicService musicService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Player]");

        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        playerViewModel = new ViewModelProvider(PlayerActivity.this).get(PlayerViewModel.class);
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        if (savedInstanceState != null) {
            playerViewModel.setBackgroundColour(playerViewModel.getBackgroundColourInt());
            playerViewModel.setPlaybackSpeed(playerViewModel.getPlaybackSpeedFloat());
        } else {
            // Handle the case when savedInstanceState is null (i.e., not a recreation due to rotation)
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")) {
                int backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                playerViewModel.setBackgroundColour(backgroundColour);
                playerViewModel.setPlaybackSpeed(playbackSpeed);
            }
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(putExtra(intent));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void onPlayButtonClick(View v){
        if (musicService != null) {
            musicService.playMusic(playerViewModel.getPlaybackSpeedFloat());
        }
    }

    public void onPauseButtonClick(View v){
        musicService.pauseMusic();
    }

    public void onStopButtonClick(View v){
        if (musicService != null) {
            musicService.stopMusic();
            stopService(new Intent(PlayerActivity.this, MusicService.class));
            if(isBound){
                unbindService(serviceConnection);
                isBound = false;
            }
        }
    }

    public void onListButtonClick(View v){
        Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
        startActivity(putExtra(intent));
    }

    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", playerViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", playerViewModel.getPlaybackSpeedFloat());
        return intent;
    }


    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override  // Triggered when the service is successfully bound
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected [Player]");
            // Linking the service to musicService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            Log.d(TAG, "isBound set to true [Player]");
            isBound = true; // Flag that the binding is successful.
            musicService.setCallback(progress -> runOnUiThread(() -> playerViewModel.setPlaybackProgress(progress)));
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
        }
    };

    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called [Player]");
        super.onStart();
        Intent intent = new Intent(PlayerActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Player]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}