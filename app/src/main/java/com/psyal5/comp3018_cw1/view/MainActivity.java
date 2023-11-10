package com.psyal5.comp3018_cw1.view;

import androidx.activity.result.ActivityResultLauncher;
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
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        observeViewModel();
        readMusicFromFolder();
    }

    private void observeViewModel() {
        Button settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
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
            @SuppressLint("Range") String selectedMusicUri = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));

            if(!isBound){
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            }

            mainViewModel.onMusicSelected(selectedMusicUri);
            startService(new Intent(MainActivity.this, MusicService.class));
            startActivity(new Intent(MainActivity.this, PlayerActivity.class));
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