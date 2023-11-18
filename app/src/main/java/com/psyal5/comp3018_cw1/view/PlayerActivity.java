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
import android.widget.ProgressBar;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityPlayerBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;


public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private PlayerViewModel playerViewModel;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Player]");

        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        playerViewModel = new ViewModelProvider(PlayerActivity.this).get(PlayerViewModel.class);
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();
    }
    private void observeViewModel() {
        playerViewModel.getActivity().observe(this, activityClass -> {
            if (activityClass != null) {
                startActivity(new Intent(PlayerActivity.this, activityClass));
            }
        });

        playerViewModel.getServiceRunning().observe(this, serviceRunning -> {
            if (!serviceRunning) {
                stopService(new Intent(PlayerActivity.this, MusicService.class));
                if(isBound){
                    unbindService(serviceConnection);
                    isBound = false;
                }
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
            playerViewModel.setMusicService(musicService);

            ProgressBar progressBar = findViewById(R.id.progressBar);
            musicService.setCallback(progress -> runOnUiThread(() -> progressBar.setProgress(progress)));

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