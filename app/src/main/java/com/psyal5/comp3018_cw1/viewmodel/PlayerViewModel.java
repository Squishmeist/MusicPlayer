package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

import java.util.Objects;

public class PlayerViewModel extends ViewModel {
    private MusicService musicService;
    private final MutableLiveData<Boolean> listActivity = new MutableLiveData<>();
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<Boolean> serviceRunning = new MutableLiveData<>();

    public Integer getBackgroundColourInt(){
        return Objects.requireNonNull(backgroundColour.getValue());
    }

    public Float getPlaybackSpeedFloat(){
        return Float.valueOf(Objects.requireNonNull(playbackSpeed.getValue()));
    }

    public MutableLiveData<Boolean> getServiceRunning(){
        return serviceRunning;
    }

    public  MutableLiveData<Integer> getBackgroundColour(){
        return backgroundColour;
    }
    public  MutableLiveData<String> getPlaybackSpeed(){
        return playbackSpeed;
    }

    public MutableLiveData<Boolean> getListActivity(){
        return listActivity;
    }

    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }

    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed.setValue(String.valueOf(playbackSpeed));
    }

    public void setListActivity(Boolean isActive){
        listActivity.setValue(isActive);
    }

    public void setMusicService(MusicService service) {
        musicService = service;
        serviceRunning.setValue(true);
    }

    public void onPlayButtonClick() {
        if (musicService != null) {
            musicService.playMusic();
        }
    }

    public void onPauseButtonClick() {
        if (musicService != null) {
            musicService.pauseMusic();
        }
    }

    public void onStopButtonClick() {
        if (musicService != null) {
            musicService.stopMusic();
            serviceRunning.setValue(false);
        }
    }

    public void onListButtonClick(){
        listActivity.setValue(true);
    }
}