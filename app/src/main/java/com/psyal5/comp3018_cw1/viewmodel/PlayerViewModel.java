package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;

public class PlayerViewModel extends ViewModel {
    private MutableLiveData<String> songName = new MutableLiveData<>("");
    private MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();
    private MusicService musicService;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
        updateSongAndIsPlaying(); // Update song and isPlaying state when setting the service
    }

    // Observing MutableLiveData for the song name
    public MutableLiveData<String> getSongName() {
        return songName;
    }

    // Observing MutableLiveData for the isPlaying state
    public MutableLiveData<Boolean> getIsPlaying() {
        return isPlaying;
    }

    // Method to handle button click for play/pause
    public void onClickStatusButton() {
        if (musicService != null) {
            if (musicService.getState().equals("PLAYING")) {
                musicService.pauseSong();
            } else if (musicService.getState().equals("PAUSED")) {
                musicService.playSong();
            }
        }
        updateSongAndIsPlaying();
    }

    // Method to update the song name and isPlaying state
    private void updateSongAndIsPlaying() {
        if (musicService != null) {
            String currentSong = musicService.getSongName();
            songName.setValue(currentSong);
            boolean isCurrentlyPlaying = musicService.getState().equals("PLAYING");
            isPlaying.setValue(isCurrentlyPlaying);
        }
    }
}
