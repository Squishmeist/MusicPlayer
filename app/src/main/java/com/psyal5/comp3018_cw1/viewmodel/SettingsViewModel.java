package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class SettingsViewModel extends ViewModel {
    private MusicService musicService;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public void setSongUri(String songUri) {
        if(musicService != null){
            musicService.setSongUri(songUri);
        }
    }

}