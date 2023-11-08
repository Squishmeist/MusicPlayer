package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class PlayerViewModel extends ViewModel {
    public MutableLiveData<String> songName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private MusicService musicService;

    public void setMusicService(MusicService service) {
        musicService = service;
        songName.setValue(musicService.getSongName());
        isPlaying.setValue(musicService.isPlaying());
    }

    // Button click methods
    public void onStatusButtonClicked() {
        if (musicService != null) {
            if (musicService.isPlaying()) {
                pauseMusic();
            } else {
                playMusic();
            }
        }
    }

    public void playMusic(){
        if (musicService != null) {
            musicService.playMusic();
            isPlaying.setValue(musicService.isPlaying());
        }
    }

    public void pauseMusic(){
        if (musicService != null) {
            musicService.pauseMusic();
            isPlaying.setValue(musicService.isPlaying());
        }
    }

    public void stopMusic(){
        if (musicService != null) {
            musicService.stopMusic();
            isPlaying.setValue(musicService.isPlaying());
        }
    }

    public void onStopButtonClicked() {
        if (musicService != null) {
            musicService.stopMusic();
            isPlaying.setValue(musicService.isPlaying());
        }
    }

    public Boolean getIsPlaying() {
        return musicService.isPlaying();
    }
}