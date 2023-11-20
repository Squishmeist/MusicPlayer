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

/**
 * PlayerActivity: Activity responsible for controlling music playback.
 * Provides controls for play, pause, stop, and displays playback progress.
 */
public class PlayerActivity extends AppCompatActivity {
    // Tag for logging purposes
    private static final String TAG = "CW1";

    // ViewModel for managing player activity data
    private PlayerViewModel playerViewModel;

    // Reference to the MusicService
    private MusicService musicService;

    // Flag indicating whether the service is bound
    private boolean isBound = false;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                            shut down, this Bundle contains the data it most recently
     *                            supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Player]");

        // Set up data binding
        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        playerViewModel = new ViewModelProvider(PlayerActivity.this).get(PlayerViewModel.class);
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        // Restore settings from savedInstanceState if available
        if (savedInstanceState != null) {
            playerViewModel.setBackgroundColour(playerViewModel.getBackgroundColourInt());
            playerViewModel.setPlaybackSpeed(playerViewModel.getPlaybackSpeedFloat());
        } else {
            // Handle the case when savedInstanceState is null (not a recreation due to rotation)
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")) {
                int backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                playerViewModel.setBackgroundColour(backgroundColour);
                playerViewModel.setPlaybackSpeed(playbackSpeed);
            }
        }

        // Override the default back button behavior
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(putExtra(intent));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Called when the "Play" button is clicked.
     * Initiates playback of the loaded music with the current playback speed.
     * @param v The clicked view.
     */
    public void onPlayButtonClick(View v){
        if (musicService != null) {
            musicService.playMusic(playerViewModel.getPlaybackSpeedFloat());
        }
    }

    /**
     * Called when the "Pause" button is clicked.
     * Pauses the currently playing music.
     * @param v The clicked view.
     */
    public void onPauseButtonClick(View v){
        musicService.pauseMusic();
    }

    /**
     * Called when the "Stop" button is clicked.
     * Stops the currently playing music, unbinds from the MusicService, and stops the service.
     * @param v The clicked view.
     */
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

    /**
     * Called when the "List" button is clicked.
     * Navigates back to the MainActivity with updated settings.
     * @param v The clicked view.
     */
    public void onListButtonClick(View v){
        Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
        startActivity(putExtra(intent));
    }

    /**
     * Helper method to add backgroundColour and playbackSpeed as extras to an intent.
     * @param intent The intent to which extras will be added.
     * @return The intent with extras added.
     */
    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", playerViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", playerViewModel.getPlaybackSpeedFloat());
        return intent;
    }

    /**
     * ServiceConnection for binding to the MusicService.
     * Handles successful connection and unexpected disconnection of the service.
     */
    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected [Player]");
            // Linking the service to musicService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            Log.d(TAG, "isBound set to true [Player]");
            isBound = true; // Flag that the binding is successful.
            // Set a callback to update playback progress on the UI thread
            musicService.setCallback(progress -> runOnUiThread(() -> playerViewModel.setPlaybackProgress(progress)));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
        }
    };

    /**
     * Called when the activity becomes visible to the user.
     * Binds to the MusicService to establish a connection.
     */
    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called [Player]");
        super.onStart();
        Intent intent = new Intent(PlayerActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Unbinds from the MusicService if it was previously bound.
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Player]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
        }
    }
}