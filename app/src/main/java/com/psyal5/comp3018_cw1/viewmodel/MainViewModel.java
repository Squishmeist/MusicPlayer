package com.psyal5.comp3018_cw1.viewmodel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

import java.util.Objects;

public class MainViewModel extends ViewModel {
    private MusicService musicService;
    private final MutableLiveData<Boolean> playerActivity = new MutableLiveData<>();
    private final MutableLiveData<Boolean> settingsActivity = new MutableLiveData<>();
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private float playbackSpeed;

    public MainViewModel() {
        backgroundColour.setValue(Color.WHITE);
        playbackSpeed = 1.0f;
    }

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

    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }

    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed = playbackSpeed;
    }

    public void setMusicService(MusicService service) {
        musicService = service;
    }

    public void onMusicSelected(String musicUri){
        if (musicService != null) {
            musicService.loadMusic(musicUri);
            musicService.setPlayback(getPlaybackSpeed());
        }
    }

    public void onPlayerButtonClick(){
        playerActivity.setValue(true);
    }

    public void onSettingsButtonClick(){
        settingsActivity.setValue(true);
    }

}