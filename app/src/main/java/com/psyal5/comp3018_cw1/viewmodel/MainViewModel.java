package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class MainViewModel extends ViewModel {
    private MusicService musicService;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    public void setSong(String songUri) {
        if(musicService != null){
            musicService.setMusic(songUri);
        }
    }

    public Boolean getIsPlaying() {
        return musicService.isPlaying();
    }

}