package com.psyal5.comp3018_cw1.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.viewmodel.data.CurrentSongManager;


public class PlayerViewModel extends ViewModel {
    private MutableLiveData<String> songName = new MutableLiveData<>();

    public LiveData<String> getSongName() {
        songName.setValue(CurrentSongManager.getSongName());
        return songName;
    }

    public String getState(){
        return CurrentSongManager.getState();
    }

}