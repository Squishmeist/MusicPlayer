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
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.databinding.ActivityMainBinding;
import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CW1";
    private MainViewModel mainViewModel;
    private boolean isBound = false;
    private RelativeLayout mainContent;
    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On create [Main]");

        mainViewModel = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setMainViewModel(mainViewModel);
        binding.setLifecycleOwner(this);

        mainContent = findViewById(R.id.mainContent);
        observeViewModel();
        readMusicFromFolder();

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("backgroundColour")){
            Integer backgroundColour = intent.getIntExtra("backgroundColour", Color.WHITE);
            Float playbackSpeed = intent.getFloatExtra("playbackSpeed", 1.0f);
            mainViewModel.setBackgroundColour(backgroundColour);
            mainViewModel.setPlaybackSpeed(playbackSpeed);
        }
    }

    private void observeViewModel() {
        mainViewModel.getBackgroundColour().observe(this, backgroundColour -> {
            if (backgroundColour != null) {
                mainContent.setBackgroundColor(backgroundColour);
            }
        });

        mainViewModel.getNextActivity().observe(this, nextActivity -> {
            if (nextActivity != null) {
                Class nextActivityClass = null;
                if(nextActivity == "Player"){
                    nextActivityClass = PlayerActivity.class;
                }else if(nextActivity == "Settings"){
                    nextActivityClass = SettingsActivity.class;
                }
                if(nextActivityClass != null){
                    Intent intent = new Intent(MainActivity.this, nextActivityClass);
                    intent.putExtra("backgroundColour", mainViewModel.getBackgroundColourInt());
                    intent.putExtra("playbackSpeed", mainViewModel.getPlaybackSpeed());
                    if(nextActivity == "Player"){
                        startActivity(intent);
                    }else if (nextActivity == "Settings"){
                        resultLauncher.launch(intent);
                    }

                }
            }
        });

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Integer returnedBackgroundColour = result.getData().getIntExtra("returnedBackgroundColour", Color.WHITE);
                        Float returnedPlaybackSpeed = result.getData().getFloatExtra("returnedPlaybackSpeed", 1.0f);
                        mainViewModel.setBackgroundColour(returnedBackgroundColour);
                        mainViewModel.setPlaybackSpeed(returnedPlaybackSpeed);
                    }
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
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            intent.putExtra("backgroundColour", mainViewModel.getBackgroundColourInt());
            intent.putExtra("playbackSpeed", mainViewModel.getPlaybackSpeed());
            startActivity(intent);
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