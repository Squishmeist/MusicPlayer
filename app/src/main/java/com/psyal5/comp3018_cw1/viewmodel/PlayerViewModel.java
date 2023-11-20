package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class PlayerViewModel extends ViewModel {
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<Integer> playbackProgress = new MutableLiveData<>();

    public Integer getBackgroundColourInt(){
        return Objects.requireNonNull(backgroundColour.getValue());
    }
    public Float getPlaybackSpeedFloat(){
        return Float.valueOf(Objects.requireNonNull(playbackSpeed.getValue()));
    }

    public  MutableLiveData<Integer> getBackgroundColour(){
        return backgroundColour;
    }
    public  MutableLiveData<String> getPlaybackSpeed(){
        return playbackSpeed;
    }
    public MutableLiveData<Integer> getPlaybackProgress(){
        return playbackProgress;
    }

    public void setBackgroundColour(int colour) {
        backgroundColour.setValue(colour);
    }
    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed.setValue(String.valueOf(playbackSpeed));
    }
    public void setPlaybackProgress(Integer playbackProgress) {
        this.playbackProgress.setValue(playbackProgress);
    }

}