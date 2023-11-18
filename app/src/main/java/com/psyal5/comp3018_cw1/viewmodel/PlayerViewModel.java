package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.model.MusicService;
import com.psyal5.comp3018_cw1.view.MainActivity;

public class PlayerViewModel extends ViewModel {
    private MusicService musicService;
    private final MutableLiveData<Class> activityClass = new MutableLiveData<>();
    private final MutableLiveData<Boolean> serviceRunning = new MutableLiveData<>();

    public void setMusicService(MusicService service) {
        musicService = service;
        serviceRunning.setValue(true);
    }

    public MutableLiveData<Boolean> getServiceRunning(){
        return serviceRunning;
    }

    public void onPlayButtonClick() {
        if (musicService != null) {
            musicService.playMusic();
        }
    }

    public void onPauseButtonClick() {
        if (musicService != null) {
            musicService.pauseMusic();
        }
    }

    public void onStopButtonClick() {
        if (musicService != null) {
            musicService.stopMusic();
            serviceRunning.setValue(false);
        }
    }

    public MutableLiveData<Class> getActivity(){
        return activityClass;
    }

    public void onListButtonClick(){
        activityClass.setValue(MainActivity.class);
    }
}