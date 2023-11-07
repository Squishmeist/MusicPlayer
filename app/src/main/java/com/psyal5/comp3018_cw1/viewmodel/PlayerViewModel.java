package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;


public class PlayerViewModel extends ViewModel {
    private MusicService musicService;
    private MutableLiveData<String> songName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>(false);


    // Method to update the song name
    public LiveData<String> getSongName() {
        songName.setValue(musicService.getSongName());
        togglePlaying();
        return songName;
    }

    // Method to toggle the isPlaying state
    public void togglePlaying() {
        if (musicService.getState().equals("PLAYING")) {
            isPlaying.setValue(true);
        } else {
            isPlaying.setValue(false);
        }
    }

    // Access the MusicService instance and perform pause/play actions
    public void onClickStatusButton() {
        if (musicService != null) {
            if (musicService.getState().equals("PLAYING")) {
                musicService.pauseSong();
            } else if (musicService.getState().equals("PAUSED")) {
                musicService.playSong();
            }
        }
        togglePlaying();
    }
}
