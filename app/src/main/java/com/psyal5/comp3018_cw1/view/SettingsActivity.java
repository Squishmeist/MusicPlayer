package com.psyal5.comp3018_cw1.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivitySettingsBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private SettingsViewModel settingsViewModel;
    private boolean isBound = false;
    private RelativeLayout settingsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Settings]");

        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        settingsViewModel = new ViewModelProvider(SettingsActivity.this).get(SettingsViewModel.class);
        binding.setSettingsViewModel(settingsViewModel);
        binding.setLifecycleOwner(this);

        settingsContent = findViewById(R.id.settingsContent);
        observeViewModel();

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")) {
                Integer backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                Float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                settingsViewModel.setBackgroundColour(backgroundColour);
                settingsViewModel.setPlaybackSpeed(playbackSpeed);
            }
        }
    }

    private void observeViewModel() {
        settingsViewModel.getBackgroundColour().observe(this, backgroundColour -> {
            if (backgroundColour != null) {
                settingsContent.setBackgroundColor(Integer.parseInt(backgroundColour));
            }
        });

        settingsViewModel.getListActivity().observe(this, listActivity -> {
            if (listActivity) {
                Intent replyIntent = new Intent();
                replyIntent.putExtra("returnedBackgroundColour", settingsViewModel.getBackgroundColourInt());
                replyIntent.putExtra("returnedPlaybackSpeed", settingsViewModel.getPlaybackSpeedFloat());
                setResult(RESULT_OK, replyIntent);
                finish();
            }
        });

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
            settingsViewModel.setMusicService(musicService);
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
            settingsViewModel.setMusicService(null);
        }
    };


    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called [Settings]");
        super.onStart();
        Intent intent = new Intent(SettingsActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Settings]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}