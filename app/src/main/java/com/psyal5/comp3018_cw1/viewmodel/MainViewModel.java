package com.psyal5.comp3018_cw1.viewmodel;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MainViewModel: ViewModel for managing data related to the MainActivity.
 */
public class MainViewModel extends ViewModel {
    // MutableLiveData for background colour, playback speed, and list of music paths
    private final MutableLiveData<Integer> backgroundColour = new MutableLiveData<>();
    private final MutableLiveData<Float> playbackSpeed = new MutableLiveData<>();
    private final MutableLiveData<List<String>> musicPaths = new MutableLiveData<>();

    /**
     * Getter method for retrieving the background colour as an integer.
     * @return The background colour as an integer.
     */
    public int getBackgroundColourInt() {
        Integer colour = backgroundColour.getValue();
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
        Float speed = playbackSpeed.getValue();
        if (speed != null) {
            return speed;
        } else {
            // Handle the case where speed is null
            return 1;
        }
    }

    /**
     * Getter method for retrieving the MutableLiveData for background colour.
     * @return MutableLiveData for background colour.
     */
    public MutableLiveData<Integer> getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * Getter method for retrieving the MutableLiveData for the list of music paths.
     * @return MutableLiveData for the list of music paths.
     */
    public MutableLiveData<List<String>> getMusicPaths() {
        return musicPaths;
    }

    /**
     * Setter method for updating the background colour.
     * @param backgroundColour The new background colour.
     */
    public void setBackgroundColour(Integer backgroundColour) {
        this.backgroundColour.setValue(backgroundColour);
    }

    /**
     * Setter method for updating the playback speed.
     * @param playbackSpeed The new playback speed.
     */
    public void setPlaybackSpeed(float playbackSpeed) {
        this.playbackSpeed.setValue(playbackSpeed);
    }

    /**
     * Setter method for updating the list of music paths.
     * @param newMusicList The new list of music paths.
     */
    public void setMusicPaths(List<String> newMusicList) {
        musicPaths.setValue(newMusicList);
    }

    /**
     * Reads music paths from the external storage using a ContentResolver and updates the LiveData.
     * @param context The context of the application.
     */
    public void readMusicFromFolder(Context context) {
        List<String> musicPaths = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + "!= 0",
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String musicPath = cursor.getString(dataIndex);
                musicPaths.add(musicPath);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Update the LiveData with the new list
        setMusicPaths(musicPaths);
    }
}