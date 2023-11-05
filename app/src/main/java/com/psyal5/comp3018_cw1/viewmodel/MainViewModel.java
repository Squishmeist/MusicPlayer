package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.viewmodel.data.CurrentSongManager;

public class MainViewModel extends ViewModel {

    public void setSongUri(String uri) {
        CurrentSongManager.setSongUri(uri);
    }
}