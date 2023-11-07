package com.psyal5.comp3018_cw1.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityMainBinding;
import com.psyal5.comp3018_cw1.viewmodel.MainViewModel;
import com.psyal5.comp3018_cw1.model.MusicService;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private boolean isBound = false;
    private static final String TAG = "CW1";
    private static final int REQUEST_MANAGE_ALL_FILES_ACCESS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "On create");

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        setupBottomNavigation();
        checkAndRequestPermission();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.main);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            return handleBottomNavigationActions(item.getItemId());
        });
    }

    private boolean handleBottomNavigationActions(int itemId) {
        // Handle different navigation options
        if (itemId == R.id.player) {
            startActivity(new Intent(MainActivity.this, PlayerActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (itemId == R.id.settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else {
            return false;
        }
    }

    private void checkAndRequestPermission() {
        if (Environment.isExternalStorageManager()) {
            Log.d(TAG, "The app already has the MANAGE_EXTERNAL_STORAGE permission");
            readMusicFromFolder();
        } else {
            Log.d(TAG, "Request the MANAGE_EXTERNAL_STORAGE permission");
            startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
        }
    }

    private void readMusicFromFolder() {
        ListView lv = findViewById(R.id.listView);
        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                null
        );

        lv.setAdapter(new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{MediaStore.Audio.Media.DATA},
                new int[]{android.R.id.text1}
        ));

        lv.setOnItemClickListener((myAdapter, myView, myItemInt, myIng) -> {
            Cursor c = (Cursor) lv.getItemAtPosition(myItemInt);
            @SuppressLint("Range") String selectedSongURI = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

            if(isBound){
                Log.d(TAG, "songSelected MusicService launched");
                mainViewModel.setSongUri(selectedSongURI);
            }

        });
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override  // Triggered when the service is successfully bound
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            // Linking the service to musicService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            MusicService musicService = binder.getService();
            Log.d(TAG, "isBound set to true");
            isBound = true; // Flag that the binding is successful.
            mainViewModel.setMusicService(musicService);
        }
        @Override //Triggered if the service unexpectedly disconnects
        public void onServiceDisconnected(ComponentName name) {
            isBound = false; // Flagging that the binding is no longer active.
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_ALL_FILES_ACCESS) {
            if (Environment.isExternalStorageManager()) {
                Log.d(TAG, "Permission granted; proceed with accessing external storage");
                readMusicFromFolder();
            } else {
                Log.d(TAG, "Permission denied; handle accordingly or inform the user");
            }
        }
    }

    @Override
    protected void onStart(){
        Log.d(TAG, "OnStart called");
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "OnStop called");
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
        mainViewModel = null;
    }
}