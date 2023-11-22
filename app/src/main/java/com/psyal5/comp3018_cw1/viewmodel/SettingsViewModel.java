package com.psyal5.comp3018_cw1.viewmodel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * SettingsViewModel: ViewModel for managing data related to the SettingsActivity.
 */
public class SettingsViewModel extends ViewModel {
    // SavedStateHandle to retain data across configuration changes
    private final SavedStateHandle savedStateHandle;

    // MutableLiveData for RGB values
    private final MutableLiveData<String> redValue = new MutableLiveData<>();
    private final MutableLiveData<String> greenValue = new MutableLiveData<>();
    private final MutableLiveData<String> blueValue = new MutableLiveData<>();

    /**
     * Constructor for the SettingsViewModel.
     *
     * @param savedStateHandle SavedStateHandle to retain data across configuration changes.
     */
    public SettingsViewModel(SavedStateHandle savedStateHandle){
        MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
        playbackSpeed.setValue("1.0");
        MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
        backgroundColour.setValue(Color.WHITE);
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
     * Getter methods for retrieving the MutableLiveData for red, green, and blue values.
     * @return MutableLiveData for red, green, and blue values.
     */
    public MutableLiveData<String> getRedValue() {
        return redValue;
    }
    public MutableLiveData<String> getGreenValue() {
        return greenValue;
    }
    public MutableLiveData<String> getBlueValue() {
        return blueValue;
    }

    /**
     * Setter method for updating the background colour based on RGB values.
     * @param backgroundColour The new background colour.
     */
    public void setBackgroundColour(int backgroundColour) {
        int red = Color.red(backgroundColour);
        int green = Color.green(backgroundColour);
        int blue = Color.blue(backgroundColour);

        // Ensure red, green, and blue are within the valid range (0 to 255)
        if (red > 255 || green > 255 || blue > 255) {
            redValue.setValue(String.valueOf(255));
            greenValue.setValue(String.valueOf(255));
            blueValue.setValue(String.valueOf(255));
            savedStateHandle.set("backgroundColour", Color.WHITE);
        } else {
            // Set RGB values and update background colour
            redValue.setValue(String.valueOf(red));
            greenValue.setValue(String.valueOf(green));
            blueValue.setValue(String.valueOf(blue));
            savedStateHandle.set("backgroundColour", backgroundColour);
        }
    }

    /**
     * Setter method for updating the playback speed.
     * @param playbackSpeed The new playback speed.
     */
    public void setPlaybackSpeed(float playbackSpeed) {
        savedStateHandle.set("playbackSpeed", String.valueOf(playbackSpeed));
    }

    /**
     * Method for updating the background colour based on RGB values.
     * Called when the "Update" button is clicked in the SettingsActivity.
     */
    public void updateBackgroundColour() {
        int red = redValue.getValue() != null ? Integer.parseInt(redValue.getValue()) : 0;
        int green = greenValue.getValue() != null ? Integer.parseInt(greenValue.getValue()) : 0;
        int blue = blueValue.getValue() != null ? Integer.parseInt(blueValue.getValue()) : 0;

        // Ensure red, green, and blue are within the valid range (0 to 255)
        if (red > 255 || green > 255 || blue > 255) {
            redValue.setValue(String.valueOf(255));
            greenValue.setValue(String.valueOf(255));
            blueValue.setValue(String.valueOf(255));
            setBackgroundColour(Color.WHITE);
        } else {
            int colour = Color.rgb(red, green, blue);
            setBackgroundColour(colour);
        }
    }

    /**
     * Method for updating the playback speed.
     * Called when the "Update" button is clicked in the SettingsActivity.
     * Limits the playback speed to a maximum of 5.
     */
    public void updatePlaybackSpeed() {
        float speed = getPlaybackSpeedFloat();
        if (speed > 5 || speed < 1) {
            speed = 1;
        }
        setPlaybackSpeed(speed);
    }
}