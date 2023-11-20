package com.psyal5.comp3018_cw1.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

/**
 * PlayerViewModel: ViewModel for managing data related to the PlayerActivity.
 */
public class PlayerViewModel extends ViewModel {
    // MutableLiveData for background colour, playback speed, and playback progress
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<Integer> playbackProgress = new MutableLiveData<>();

    /**
     * Getter method for retrieving the background colour as an integer.
     * @return The background colour as an integer.
     */
    public Integer getBackgroundColourInt() {
        return Objects.requireNonNull(backgroundColour.getValue());
    }

    /**
     * Getter method for retrieving the playback speed as a float.
     * @return The playback speed as a float.
     */
    public Float getPlaybackSpeedFloat() {
        return Float.valueOf(Objects.requireNonNull(playbackSpeed.getValue()));
    }

    /**
     * Getter method for retrieving the MutableLiveData for background colour.
     * @return MutableLiveData for background colour.
     */
    public MutableLiveData<Integer> getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * Getter method for retrieving the MutableLiveData for playback speed.
     * @return MutableLiveData for playback speed.
     */
    public MutableLiveData<String> getPlaybackSpeed() {
        return playbackSpeed;
    }

    /**
     * Getter method for retrieving the MutableLiveData for playback progress.
     * @return MutableLiveData for playback progress.
     */
    public MutableLiveData<Integer> getPlaybackProgress() {
        return playbackProgress;
    }

    /**
     * Setter method for updating the background colour.
     * @param colour The new background colour.
     */
    public void setBackgroundColour(int colour) {
        backgroundColour.setValue(colour);
    }

    /**
     * Setter method for updating the playback speed.
     * @param playbackSpeed The new playback speed.
     */
    public void setPlaybackSpeed(float playbackSpeed) {
        this.playbackSpeed.setValue(String.valueOf(playbackSpeed));
    }

    /**
     * Setter method for updating the playback progress.
     * @param playbackProgress The new playback progress.
     */
    public void setPlaybackProgress(Integer playbackProgress) {
        this.playbackProgress.setValue(playbackProgress);
    }
}