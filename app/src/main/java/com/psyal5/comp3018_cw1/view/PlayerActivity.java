package com.psyal5.comp3018_cw1.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityPlayerBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.PlayerViewModel;

public class PlayerActivity extends AppCompatActivity {
    private PlayerViewModel playerViewModel;
    // Represents the MyService that the Activity will communicate with.
    private MusicService musicService;
    // A flag to check if the Activity is currently bound to the service.
    private boolean isBound = false;
    // A tool that helps in scheduling tasks to run at some future time.
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPlayerBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_player);
        // Create an instance of PlayerViewModel
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        // Set the ViewModel to the binding
        binding.setPlayerViewModel(playerViewModel);
        binding.setLifecycleOwner(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.player);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                startActivity(new Intent(PlayerActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if (itemId == R.id.player) {
                return true;
            } else if (itemId == R.id.settings) {
                startActivity(new Intent(PlayerActivity.this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensuring the Activity unbinds from the service properly when it's about to be destroyed.
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    // A bridge or listener between the MainActivity and the MyService.
    // Triggered when the service is successfully bound
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Linking the service to our myService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            // Flag that the binding is successful.
            isBound = true;
        }
        //Triggered if the service unexpectedly disconnects
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Flagging that the binding is no longer active.
            isBound = false;
        }
    };
}