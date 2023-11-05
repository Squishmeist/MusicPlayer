package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.viewmodel.data.CurrentSongManager;


public class PlayerViewModel extends ViewModel {
    private MutableLiveData<String> songName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);

    public LiveData<String> getSongName() {
        togglePlaying();
        songName.setValue(CurrentSongManager.getSongName());
        return songName;
    }

    // Method to toggle the isPlaying state
    public void togglePlaying() {
        if(CurrentSongManager.getState() == "PLAYING"){
            isPlaying.setValue(true);
        }else{
            isPlaying.setValue(false);
        }
    }

    // Update icon and action
    public void onClickStatusButton() {
        String currentState = CurrentSongManager.getState();
        switch (currentState) {
            case "PLAYING":
                CurrentSongManager.pauseSong();
                break;
            case "PAUSED":
                CurrentSongManager.playSong();
                break;
        }
        togglePlaying();
    }

}