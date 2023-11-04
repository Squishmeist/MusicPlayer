package com.psyal5.comp3018_cw1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_MANAGE_ALL_FILES_ACCESS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        if (Environment.isExternalStorageManager()) {
            Log.d("LEE", "The app already has the MANAGE_EXTERNAL_STORAGE permission");
            readMusicFromFolder();
            // Access files and directories as needed
        } else {
            Log.d("LEE", "Request the MANAGE_EXTERNAL_STORAGE permission");
            // Request the MANAGE_EXTERNAL_STORAGE permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivityForResult(intent, REQUEST_MANAGE_ALL_FILES_ACCESS);
        }
    }

    private void readMusicFromFolder() {
        final ListView lv = findViewById(R.id.listView);
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0", null,
                null);
        lv.setAdapter(new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, cursor,
                new String[] { MediaStore.Audio.Media.DATA}, new
                int[] { android.R.id.text1 }));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() { public
        void onItemClick(AdapterView<?> myAdapter,
                         View myView,
                         int myItemInt,
                         long mylng) {
            Cursor c = (Cursor) lv.getItemAtPosition(myItemInt);
            @SuppressLint("Range") String uri =
                    c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.d("LEE", uri);
// do something with the selected uri string...
        }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_ALL_FILES_ACCESS) {
            if (Environment.isExternalStorageManager()) {
                Log.d("LEE", "Permission granted; proceed with accessing external storage");
                // Permission granted; proceed with accessing external storage
                // Access files and directories as needed
            } else {
                Log.d("LEE", "Permission denied; handle accordingly or inform the user");
                // Permission denied; handle accordingly or inform the user
            }
        }
    }
}