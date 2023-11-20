package com.psyal5.comp3018_cw1.viewmodel;

import android.graphics.Color;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class SettingsViewModel extends ViewModel {
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<String> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<String> redValue = new MutableLiveData<>();
    private final MutableLiveData<String> greenValue = new MutableLiveData<>();
    private final MutableLiveData<String> blueValue = new MutableLiveData<>();

    public int getBackgroundColourInt(){
        return Objects.requireNonNull(backgroundColour.getValue());
    }
    public float getPlaybackSpeedFloat(){
        return Float.parseFloat(Objects.requireNonNull(playbackSpeed.getValue()));
    }

    public MutableLiveData<Integer> getBackgroundColour(){
        return backgroundColour;
    }
    public MutableLiveData<String> getPlaybackSpeed() {
        return playbackSpeed;
    }

    public void setBackgroundColour(int backgroundColour) {
        int red = Color.red(backgroundColour);
        int green = Color.green(backgroundColour);
        int blue = Color.blue(backgroundColour);

        // Ensure red, green, and blue are within the valid range (0 to 255)
        if (red > 255 || green > 255 || blue > 255) {
            redValue.setValue(String.valueOf(255));
            greenValue.setValue(String.valueOf(255));
            blueValue.setValue(String.valueOf(255));
            this.backgroundColour.setValue(Color.WHITE);
        } else {
            redValue.setValue(String.valueOf(red));
            greenValue.setValue(String.valueOf(green));
            blueValue.setValue(String.valueOf(blue));
            this.backgroundColour.setValue(backgroundColour);
        }
    }
    public void setPlaybackSpeed(float playbackSpeed){
        this.playbackSpeed.setValue(String.valueOf(playbackSpeed));
    }

    public MutableLiveData<String> getRedValue() {
        return redValue;
    }
    public MutableLiveData<String> getGreenValue() {
        return greenValue;
    }
    public MutableLiveData<String> getBlueValue() {
        return blueValue;
    }

    public void updateBackgroundColour() {
        int red = redValue.getValue() != null ? Integer.parseInt(redValue.getValue()) : 0;
        int green = greenValue.getValue() != null ? Integer.parseInt(greenValue.getValue()) : 0;
        int blue = blueValue.getValue() != null ? Integer.parseInt(blueValue.getValue()) : 0;

        // Ensure red, green, and blue are within the valid range (0 to 255)
        if (red > 255 || green > 255 || blue > 255) {
            redValue.setValue(String.valueOf(255));
            greenValue.setValue(String.valueOf(255));
            blueValue.setValue(String.valueOf(255));
            this.backgroundColour.setValue(Color.WHITE);
        } else {
            int colour = Color.rgb(red, green, blue);
            this.backgroundColour.setValue(colour);
        }
    }
    public void updatePlaybackSpeed(){
        float speed = getPlaybackSpeedFloat();
        if(speed > 5){
            speed = 1;
        }
        this.setPlaybackSpeed(speed);
    }
}