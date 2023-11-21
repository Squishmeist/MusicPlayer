package com.psyal5.comp3018_cw1.viewmodel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * PlayerViewModel: ViewModel for managing data related to the PlayerActivity.
 */
public class PlayerViewModel extends ViewModel {
    // SavedStateHandle to retain data across configuration changes
    private final SavedStateHandle savedStateHandle;

    /**
     * Constructor for the PlayerViewModel.
     *
     * @param savedStateHandle SavedStateHandle to retain data across configuration changes.
     */
    public PlayerViewModel(SavedStateHandle savedStateHandle){
        // MutableLiveData for background colour, playback speed, and playback progress
        MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
        playbackSpeed.setValue("1.0");
        MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
        backgroundColour.setValue(Color.WHITE);
        MutableLiveData<Integer> playbackProgress = new MutableLiveData<>();
        playbackProgress.setValue(0);
        this.savedStateHandle = savedStateHandle;
    }

    /**
     * Getter method for retrieving the background colour as an integer.
     * @return The background colour as an integer.
     */
    public int getBackgroundColourInt() {
        Integer colour = getBackgroundColour().getValue();
        if (colour != null) {
            return colour;
        } else {
            // Handle the case where colour is null
            return Color.WHITE;
        }
    }

    /**
     * Getter method for retrieving the playback speed as a float.
     * @return The playback speed as a float.
     */
    public float getPlaybackSpeedFloat() {
        String speed = getPlaybackSpeed().getValue();
        try {
            return (speed != null) ? Float.parseFloat(speed) : 1.0f;
        } catch (NumberFormatException e) {
            // Handle the case where the string is not a valid float
            System.err.println("Invalid float value. Returning default value.");
            return 1.0f;
        }
    }

    /**
     * Getter method for retrieving the MutableLiveData for background colour.
     * @return MutableLiveData for background colour.
     */
    public MutableLiveData<Integer> getBackgroundColour() {
        return savedStateHandle.getLiveData("backgroundColour");
    }

    /**
     * Getter method for retrieving the MutableLiveData for playback speed.
     * @return MutableLiveData for playback speed.
     */
    public MutableLiveData<String> getPlaybackSpeed() {
        return savedStateHandle.getLiveData("playbackSpeed");
    }

    /**
     * Getter method for retrieving the MutableLiveData for playback progress.
     * @return MutableLiveData for playback progress.
     */
    public MutableLiveData<Integer> getPlaybackProgress() {
        return savedStateHandle.getLiveData("playbackProgress");
    }

    /**
     * Setter method for updating the background colour.
     * @param backgroundColour The new background colour.
     */
    public void setBackgroundColour(Integer backgroundColour) {
        savedStateHandle.set("backgroundColour", backgroundColour);
    }

    /**
     * Setter method for updating the playback speed.
     * @param playbackSpeed The new playback speed.
     */
    public void setPlaybackSpeed(float playbackSpeed) {
        savedStateHandle.set("playbackSpeed", String.valueOf(playbackSpeed));
    }

    /**
     * Setter method for updating the playback progress.
     * @param playbackProgress The new playback progress.
     */
    public void setPlaybackProgress(Integer playbackProgress) {
        savedStateHandle.set("playbackProgress", playbackProgress);
    }
}