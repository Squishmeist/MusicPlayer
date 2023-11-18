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

public class MusicService extends Service {
    private static final String CHANNEL_ID = "MusicChannel";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private static final String TAG ="CW1";
    private boolean isPlaying = false;
    private boolean stopPlaying = false;
    private final IBinder binder = new LocalBinder();
    private MP3Player mp3Player;
    private MusicCallback callback;
    @Override
    public void onCreate(){
        Log.d(TAG, "On Create [Service]");
        super.onCreate();
        mp3Player = new MP3Player();
        notificationManager = getSystemService(NotificationManager.class);
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
                while(!stopPlaying){
                    Thread.sleep(1000);
                    if(callback != null){
                        callback.onMusicProgress((int) getProgress());
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Log.d(TAG, "StopSelf() called [Service]");
            stopSelf();
            stopPlaying = true;
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
            notificationManager.createNotificationChannel(channel);
            Log.d(TAG, "createNotificationChannel complete");
        }
    }

    public interface MusicCallback{
        void onMusicProgress(int progress);
    }

    public void setCallback(MusicCallback callback){
       this.callback = callback;
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

    public double getProgress(){
        return ((double) mp3Player.getProgress() / mp3Player.getDuration()) * 100;
    }

    public void loadMusic(String musicUri){
        Log.d(TAG, "Load Music [Service]");
        mp3Player.stop();
        mp3Player.load(musicUri, 1.0f);
    }

    public void playMusic() {
        Log.d(TAG, "Play Music [Service]");
        mp3Player.play();
        isPlaying = true;
    }

    public void pauseMusic(){
        Log.d(TAG, "Pause Music [Service]");
        mp3Player.pause();
        isPlaying = false;
    }

    public void stopMusic() {
        Log.d(TAG, "Stop Music [Service]");
        mp3Player.stop();
        stopPlaying = true;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy [Service]");
        super.onDestroy();
        mp3Player.stop();
    }
}