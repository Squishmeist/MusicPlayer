package com.psyal5.comp3018_cw1.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<Float> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<List<String>> musicPaths  = new MutableLiveData<>();

    public int getBackgroundColourInt() {
        return Objects.requireNonNull(backgroundColour.getValue());
    }
    public float getPlaybackSpeedFloat() {
        return  Objects.requireNonNull(playbackSpeed.getValue());
    }

    public  MutableLiveData<Integer> getBackgroundColour(){
        return backgroundColour;
    }
    public MutableLiveData<List<String>> getMusicPaths() {
        return musicPaths ;
    }

    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }
    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed.setValue(playbackSpeed);
    }

    public void setMusicPaths(List<String> newMusicList) {
        musicPaths.setValue(newMusicList);
    }
    public void readMusicFromFolder(Context context) {
        List<String> musicPaths = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String musicPath = cursor.getString(dataIndex);
                musicPaths.add(musicPath);
            } while (cursor.moveToNext());

            cursor.close();
        }

        Log.d("test", musicPaths.toString());
        // Update the LiveData with the new list
        setMusicPaths(musicPaths);
    }
}