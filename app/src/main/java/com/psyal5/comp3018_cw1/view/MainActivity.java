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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private MainViewModel mainViewModel;
    private MusicService musicService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Main]");

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);

        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            ListAdapter adapter = binding.listView.getAdapter();
            if (adapter instanceof ArrayAdapter<?>) {
                String selectedMusicUri = (String) adapter.getItem(position);
                onMusicItemClick(selectedMusicUri);
            } else {
                Log.d(TAG, "Incorrect adapter type");
            }
        });

        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        mainViewModel.readMusicFromFolder(this);

        if (savedInstanceState != null) {
            mainViewModel.setBackgroundColour(mainViewModel.getBackgroundColourInt());
            mainViewModel.setPlaybackSpeed(mainViewModel.getPlaybackSpeedFloat());
        } else {
            // Handle the case when savedInstanceState is null (i.e., not a recreation due to rotation)
            Intent intent = getIntent();
            if(intent != null && intent.hasExtra("backgroundColour") && intent.hasExtra("playbackSpeed")){
                Integer backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
                float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
                mainViewModel.setBackgroundColour(backgroundColour);
                mainViewModel.setPlaybackSpeed(playbackSpeed);
            }else{
                mainViewModel.setBackgroundColour(Color.WHITE);
                mainViewModel.setPlaybackSpeed(1.0f);
            }
        }
    }

    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", mainViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", mainViewModel.getPlaybackSpeedFloat());
        return intent;
    }

    public void onMusicItemClick(String selectedMusicUri){
        Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
        stopService(serviceIntent);
        startService(serviceIntent);
        musicService.loadMusic(selectedMusicUri, mainViewModel.getPlaybackSpeedFloat());
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(putExtra(intent));
    }

    public void onPlayerButtonClick(View v){
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(putExtra(intent));
    }

    public void onSettingsButtonClick(View v){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(putExtra(intent));
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
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
        }
    };

    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called [Main]");
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "OnStop called [Main]");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}