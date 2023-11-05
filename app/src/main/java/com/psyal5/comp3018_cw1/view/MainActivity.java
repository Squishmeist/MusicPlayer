package com.psyal5.comp3018_cw1.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.psyal5.comp3018_cw1.R;
import com.psyal5.comp3018_cw1.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;
    private static final int REQUEST_MANAGE_ALL_FILES_ACCESS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

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

            mainViewModel.setSongUri(selectedSongURI);
            startActivity(new Intent(MainActivity.this, PlayerActivity.class));
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
}