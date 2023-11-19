package com.psyal5.comp3018_cw1.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityMainBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private MainViewModel mainViewModel;
    private boolean isBound = false;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Main]");

        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();

        mainViewModel.readMusicFromFolder(this);

        if (savedInstanceState == null) {
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

    private void observeViewModel() {
        mainViewModel.getPlayerActivity().observe(this, playerActivity ->{
            if(playerActivity){
                if(Boolean.TRUE.equals(mainViewModel.getStartService().getValue())){
                    mainViewModel.setStartService(false);
                    startService(new Intent(MainActivity.this, MusicService.class));
                }
                mainViewModel.setPlayerActivity(false);
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(putExtra(intent));
            }
        });

        mainViewModel.getSettingsActivity().observe(this, settingsActivity ->{
            if(settingsActivity){
                mainViewModel.setSettingsActivity(false);
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                resultLauncher.launch(putExtra(intent));
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Integer returnedBackgroundColour = result.getData().getIntExtra("returnedBackgroundColour", Color.WHITE);
                        float returnedPlaybackSpeed = result.getData().getFloatExtra("returnedPlaybackSpeed", 1.0f);
                        mainViewModel.setBackgroundColour(returnedBackgroundColour);
                        mainViewModel.setPlaybackSpeed(returnedPlaybackSpeed);
                    }
                });
    }

    private Intent putExtra(Intent intent){
        intent.putExtra("backgroundColour", mainViewModel.getBackgroundColourInt());
        intent.putExtra("playbackSpeed", mainViewModel.getPlaybackSpeed());
        return intent;
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
            mainViewModel.setMusicService(musicService);
        }
        @Override // Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
            mainViewModel.setMusicService(null);
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