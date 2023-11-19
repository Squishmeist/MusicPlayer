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
    private final MutableLiveData<Boolean> playerActivity = new MutableLiveData<>();
    private final MutableLiveData<Boolean> settingsActivity = new MutableLiveData<>();
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private MutableLiveData<List<String>> musicList = new MutableLiveData<>();
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

    public MutableLiveData<Boolean> getPlayerActivity(){
        return playerActivity;
    }

    public MutableLiveData<Boolean> getSettingsActivity(){
        return settingsActivity;
    }

    public MutableLiveData<List<String>> getMusicList() {
        return musicList;
    }

    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }

    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed = playbackSpeed;
    }

    public void updateMusicList(List<String> newMusicList) {
        musicList.setValue(newMusicList);
        Log.d("test", musicList.toString());
    }

    public void setMusicService(MusicService service) {
        musicService = service;
    }

    public void setPlayerActivity(Boolean isActive){
        playerActivity.setValue(isActive);
    }

    public void setSettingsActivity(Boolean isActive){
        settingsActivity.setValue(isActive);
    }

    public void onMusicSelected(String musicUri){
        if (musicService != null) {
            musicService.loadMusic(musicUri);
            musicService.setPlayback(getPlaybackSpeed());
        }
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
        updateMusicList(musicPaths);
    }

    public void onListViewClick(int position) {
        // Get the list of items from your ViewModel
        List<String> musicList = getMusicList().getValue();

        // Ensure the position is valid
        int clippedPosition = Math.min(position, musicList.size() - 1);

        // Get the clicked item
        String clickedItem = musicList.get(clippedPosition);
        onMusicSelected(clickedItem);
        playerActivity.setValue(true);
    }


    public void onPlayerButtonClick(){
        playerActivity.setValue(true);
    }

    public void onSettingsButtonClick(){
        settingsActivity.setValue(true);
    }

}