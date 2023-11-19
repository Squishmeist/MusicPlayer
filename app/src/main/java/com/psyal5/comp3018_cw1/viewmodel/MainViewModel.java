package com.psyal5.comp3018_cw1.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainViewModel extends ViewModel {
    private MusicService musicService;
    private final MutableLiveData<Boolean> startService = new MutableLiveData<>();
    private final MutableLiveData<Boolean> playerActivity = new MutableLiveData<>();
    private final MutableLiveData<Boolean> settingsActivity = new MutableLiveData<>();
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<List<String>> musicPaths  = new MutableLiveData<>();
    private float playbackSpeed;

    public Integer getBackgroundColourInt() {
        return Objects.requireNonNull(backgroundColour.getValue());
    }

    public Float getPlaybackSpeed() {
        return playbackSpeed;
    }

    public  MutableLiveData<Integer> getBackgroundColour(){
        return backgroundColour;
    }
    public MutableLiveData<Boolean> getStartService(){
        return startService;
    }
    public MutableLiveData<Boolean> getPlayerActivity(){
        return playerActivity;
    }
    public MutableLiveData<Boolean> getSettingsActivity(){
        return settingsActivity;
    }
    public MutableLiveData<List<String>> getMusicPaths() {
        return musicPaths ;
    }

    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }
    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed = playbackSpeed;
    }

    public void setMusicPaths(List<String> newMusicList) {
        musicPaths.setValue(newMusicList);
    }

    public void setMusicService(MusicService service) {
        musicService = service;
    }
    public void setStartService(Boolean isActive){
        startService.setValue(isActive);
    }
    public void setPlayerActivity(Boolean isActive){
        playerActivity.setValue(isActive);
    }

    public void setSettingsActivity(Boolean isActive){
        settingsActivity.setValue(isActive);
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

    public void onMusicItemSelected(String selectedMusicUri) {
        if (musicService != null) {
            startService.setValue(true);
            musicService.loadMusic(selectedMusicUri);
            musicService.setPlayback(getPlaybackSpeed());
            playerActivity.setValue(true);
        }
    }

    public void onPlayerButtonClick(){
        playerActivity.setValue(true);
    }

    public void onSettingsButtonClick(){
        settingsActivity.setValue(true);
    }

}