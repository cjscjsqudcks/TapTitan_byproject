package com.example.TapTitan;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicService extends Service {
    static MediaPlayer m;
    static String path;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public void onDestroy() {
        m.stop();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        m=MediaPlayer.create(this,R.raw.title);
        m.setLooping(true);
        m.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
