package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.psyal5.comp3018_cw1.view.MainActivity;

public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<Class> activity = new MutableLiveData<>();

    public MutableLiveData<Class> getActivity(){
        return activity;
    }


    public void onListButtonClick(){
        activity.setValue(MainActivity.class);
    }

}