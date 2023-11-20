package com.psyal5.comp3018_cw1.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivitySettingsBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.SettingsViewModel;

/**
 * SettingsActivity: Activity for adjusting application settings.
 * Responsible for updating the background colour and playback speed.
 */
public class SettingsActivity extends AppCompatActivity {
    // Tag for logging purposes
    private static final String TAG = "CW1";

    // ViewModel for managing settings data
    private SettingsViewModel settingsViewModel;

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
        Log.d(TAG, "On create [Settings]");

        // Set up data binding
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsViewModel = new ViewModelProvider(SettingsActivity.this).get(SettingsViewModel.class);
        binding.setSettingsViewModel(settingsViewModel);
        binding.setLifecycleOwner(this);

        // Restore settings from savedInstanceState if available
        if (savedInstanceState != null) {
            settingsViewModel.setBackgroundColour(settingsViewModel.getBackgroundColourInt());
            settingsViewModel.setPlaybackSpeed(settingsViewModel.getPlaybackSpeedFloat());
        } else {
            // Handle the case when savedInstanceState is null (not a recreation due to rotation)
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")) {
                int backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                settingsViewModel.setBackgroundColour(backgroundColour);
                settingsViewModel.setPlaybackSpeed(playbackSpeed);
            }
        }

        // Override the default back button behavior
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(putExtra(intent));
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Helper method to add backgroundColour and playbackSpeed as extras to an intent.
     * @param intent The intent to which extras will be added.
     * @return The intent with extras added.
     */
    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", settingsViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", settingsViewModel.getPlaybackSpeedFloat());
        return intent;
    }

    /**
     * Called when the "Update" button is clicked.
     * Updates the background color and playback speed based on ViewModel values.
     * If the MusicService is bound, it also updates the playback speed in the service.
     * @param v The clicked view.
     */
    public void onUpdateButtonClick(View v){
        settingsViewModel.updateBackgroundColour();
        settingsViewModel.updatePlaybackSpeed();
        if(musicService != null && musicService.isPlaying){
            musicService.setPlayback(settingsViewModel.getPlaybackSpeedFloat());
        }
    }

    /**
     * Called when the "List" button is clicked.
     * Navigates back to the MainActivity with updated settings.
     * @param v The clicked view.
     */
    public void onListButtonClick(View v){
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(putExtra(intent));
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
        Log.d(TAG, "OnStart called [Settings]");
        super.onStart();
        Intent intent = new Intent(SettingsActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Unbinds from the MusicService if it was previously bound.
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Settings]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
        }
    }
}