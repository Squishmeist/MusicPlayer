package com.psyal5.comp3018_cw1.viewmodel.data;

import android.net.Uri;

import com.psyal5.comp3018_cw1.viewmodel.MP3Player;

import java.io.File;

public class CurrentSongManager {
    private static String songUri = "";
    private static String songName = "";
    private static MP3Player mp3Player = new MP3Player();

    public static void setSongUri(String uri) {
        songUri = uri;
        songName = getSongNameFromUri(songUri);

        mp3Player.stop();
        mp3Player.load(songUri, 1.0f); // Load the song with normal speed
        mp3Player.play();
    }

    private static String getSongNameFromUri(String songUri) {
        // Convert the URI string to a Uri object
        Uri uri = Uri.parse(songUri);

        // Extract the file path from the URI
        String filePath = uri.getPath();

        // Create a File object using the file path
        File file = new File(filePath);

        // Retrieve the name of the file
        String songName = file.getName();

        // Remove the file extension (".mp3")
        int extensionIndex = songName.lastIndexOf('.');
        if (extensionIndex > 0) {
            songName = songName.substring(0, extensionIndex);
        }

        return songName;
    }

    public static String getSongUri() {
        return songUri;
    }

    public static String getSongName() {
        return songName;
    }

}