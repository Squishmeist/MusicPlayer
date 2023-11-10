package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class PlayerViewModel extends ViewModel {
    private MusicService musicService;

    public void setMusicService(MusicService service) {
        musicService = service;
    }

    public void onMusicSelected(String musicUri){
        if (musicService != null) {
            musicService.loadMusic(musicUri);
        }
    }

    public void onStopButtonClicked() {
        if (musicService != null) {
            musicService.stopMusic();
        }
    }

    public void onPlayButtonClicked() {
        if (musicService != null) {
            musicService.playMusic();
        }
    }

    public void onPauseButtonClicked() {
        if (musicService != null) {
            musicService.pauseMusic();
        }
    }
}