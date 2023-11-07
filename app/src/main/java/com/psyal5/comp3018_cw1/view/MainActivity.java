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
import android.os.Handler;
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
    private static final int REQUEST_MANAGE_ALL_FILES_ACCESS = 100;
    // A flag to check if the Activity is currently bound to the service.
    private boolean isBound = false;
    // A tool that helps in scheduling tasks to run at some future time.
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        if (!isBound) {
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.main);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.main) {
                return true;
            }else if (itemId == R.id.player) {
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
        });

        checkAndRequestPermission();
    }


    // A bridge or listener between the MainActivity and the MyService.
    // Triggered when the service is successfully bound
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Linking the service to our myService variable.
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            MusicService musicService = binder.getService();
            mainViewModel.setMusicService(musicService);
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



    private void checkAndRequestPermission() {
        if (Environment.isExternalStorageManager()) {
            Log.d("CW1", "The app already has the MANAGE_EXTERNAL_STORAGE permission");
            readMusicFromFolder();
        } else {
            Log.d("CW1", "Request the MANAGE_EXTERNAL_STORAGE permission");
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
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
            Log.d("CW1", selectedSongURI);

            if (isBound) {  // Add a null check for musicService
                Log.d("CW1", "song selected");
                mainViewModel.setSongUri(selectedSongURI);
                Intent playerIntent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(playerIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_ALL_FILES_ACCESS) {
            if (Environment.isExternalStorageManager()) {
                Log.d("CW1", "Permission granted; proceed with accessing external storage");
                readMusicFromFolder();
            } else {
                Log.d("CW1", "Permission denied; handle accordingly or inform the user");
            }
        }
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
}