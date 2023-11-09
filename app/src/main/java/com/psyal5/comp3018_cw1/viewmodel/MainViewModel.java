package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class MainViewModel extends ViewModel {
    private MusicService musicService;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;

    }

    public void loadMusic(String musicUri) {
        if (musicService != null) {
            musicService.loadMusic(musicUri);
        }
    }

    public Boolean getIsPlaying() {
        if (musicService != null) {
            return musicService.isPlaying();
        }
        return false;
    }
}