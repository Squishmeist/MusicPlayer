package com.psyal5.comp3018_cw1.model;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;

public class MusicService extends Service {
    private final IBinder binder = new LocalBinder();
    private final MP3Player mp3Player = new MP3Player();

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

        MP3Player getMP3Player(){
            return mp3Player;
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setSongUri(String songUri) {
        mp3Player.stop();
        mp3Player.load(songUri, 1.0f); // Load the song with normal speed
        mp3Player.play();
    }

    public void playSong(){
        String songUri = mp3Player.getFilePath();
        mp3Player.stop();
        mp3Player.load(songUri, 1.0f); // Load the song with normal speed
        mp3Player.play();
    }


    public void pauseSong() {
        if (mp3Player.getState().equals("PLAYING")) {
            mp3Player.pause();
        }
    }

    public void stopSong() {
        mp3Player.stop();
    }

    public String getSongName() {
        // Convert the URI string to a Uri object
        String filePath = mp3Player.getFilePath();

        // Create a File object using the file path
        assert filePath != null;
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

    public String getState(){
        return String.valueOf(mp3Player.getState());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSong();
    }
}