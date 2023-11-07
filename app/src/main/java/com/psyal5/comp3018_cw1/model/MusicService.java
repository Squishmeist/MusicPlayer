package com.psyal5.comp3018_cw1.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.psyal5.comp3018_cw1.R;

import java.io.File;

public class MusicService extends Service {
    private static final String CHANNEL_ID = "MusicChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG ="CW1";
    private boolean isPlaying = false;
    private boolean cancelPlaying = false;
    private final IBinder binder = new LocalBinder();
    private MusicCallback callback;
    private final MP3Player mp3Player = new MP3Player();

    @Override
    public void onCreate(){
        Log.d(TAG, "On Create [Service]");
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand [Service]");

        if(isPlaying){
            Log.d(TAG,"Music is already playing [Service]");
            return START_NOT_STICKY;
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Service")
                .setContentText("Playing music")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        Log.d(TAG, "Start foreground called [Service]");
        startForeground(NOTIFICATION_ID, notification);

        new Thread(() -> {
            try{
                isPlaying = true;
                Log.d(TAG, "Music is playing [Service]");
                while(!cancelPlaying){
                    Thread.sleep(1000);
                    Log.d(TAG, String.valueOf(getProgress()));

                    if(callback != null){
                        callback.onMusicProgress(getProgress());
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Log.d(TAG, "StopSelf() called [Service]");
            stopSelf();
            cancelPlaying = true;
            isPlaying = true;
        }).start();
        return START_NOT_STICKY;
    }

    private void createNotificationChannel(){
        Log.d(TAG, "createNotificationChannel called [Service]");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Music Service";
            String description = "Used for playing music";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "createNotificationChannel complete");
        }
    }

    public interface MusicCallback{
        void onMusicProgress(int progress);
    }

    public void setCallback(boolean value){
        cancelPlaying = value;
    }


    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
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

    public int getProgress(){
        return mp3Player.getProgress();
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