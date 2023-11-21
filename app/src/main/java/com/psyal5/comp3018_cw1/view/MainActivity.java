package com.psyal5.comp3018_cw1.view;

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
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityMainBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.MainViewModel;

/**
 * MainActivity: The main activity of the application.
 * Responsible for displaying a list of music and providing navigation to other activities.
 */
public class MainActivity extends AppCompatActivity {
    // Tag for logging purposes
    private static final String TAG = "CW1";

    // ViewModel for managing main activity data
    private MainViewModel mainViewModel;

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
        Log.d(TAG, "On create [Main]");

        // Set up data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);

        // Set up item click listener for the music list
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            ListAdapter adapter = binding.listView.getAdapter();
            if (adapter instanceof ArrayAdapter<?>) {
                String selectedMusicUri = (String) adapter.getItem(position);
                onMusicItemClick(selectedMusicUri);
            } else {
                Log.d(TAG, "Incorrect adapter type");
            }
        });

        // Set ViewModel for data binding
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        // Read music from folder
        mainViewModel.readMusicFromFolder(this);

        if (savedInstanceState == null){
            // Handle the case when savedInstanceState is null (not a recreation due to rotation)
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")){
                Integer backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                mainViewModel.setBackgroundColour(backgroundColour);
                mainViewModel.setPlaybackSpeed(playbackSpeed);
            } else {
                mainViewModel.setBackgroundColour(Color.WHITE);
                mainViewModel.setPlaybackSpeed(1.0f);
            }
        }
    }

    /**
     * Helper method to add backgroundColour and playbackSpeed as extras to an intent.
     * @param intent The intent to which extras will be added.
     * @return The intent with extras added.
     */
    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", mainViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", mainViewModel.getPlaybackSpeedFloat());
        return intent;
    }

    /**
     * Called when an item in the music list is clicked.
     * Initiates actions related to the selected music item.
     * @param selectedMusicUri The URI of the selected music item.
     */
    public void onMusicItemClick(String selectedMusicUri){
        // Start the MusicService and load the selected music
        Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
        stopService(serviceIntent);
        startService(serviceIntent);
        musicService.loadMusic(selectedMusicUri, mainViewModel.getPlaybackSpeedFloat());

        // Start the PlayerActivity with updated settings
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(putExtra(intent));
    }

    /**
     * Called when the "Player" button is clicked.
     * Navigates to the PlayerActivity with updated settings.
     * @param v The clicked view.
     */
    public void onPlayerButtonClick(View v){
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(putExtra(intent));
    }

    /**
     * Called when the "Settings" button is clicked.
     * Navigates to the SettingsActivity with updated settings.
     * @param v The clicked view.
     */
    public void onSettingsButtonClick(View v){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
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
        Log.d(TAG, "OnStart called [Main]");
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Called when the activity is no longer visible to the user.
     * Unbinds from the MusicService if it was previously bound.
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Main]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
        }
    }
}