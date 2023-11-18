package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.view.PlayerActivity;
import com.psyal5.comp3018_cw1.view.SettingsActivity;

public class MainViewModel extends ViewModel {
    private MusicService musicService;
    private final MutableLiveData<Class> activity = new MutableLiveData<>();

    public void setMusicService(MusicService service) {
        musicService = service;
    }

    public void onMusicSelected(String musicUri){
        if (musicService != null) {
            musicService.loadMusic(musicUri);
        }
    }

    public MutableLiveData<Class> getActivity(){
        return activity;
    }

    public void onPlayerButtonClick(){
        activity.setValue(PlayerActivity.class);
    }

    public void onSettingsButtonClick(){
        activity.setValue(SettingsActivity.class);
    }

}