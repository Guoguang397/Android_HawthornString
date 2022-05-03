package cn.edu.nefu.HawthornString;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SoundService extends Service {
    MediaPlayer mediaPlayer;
    public SoundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getIntExtra("music", 0);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.stop();
            mp.release();
            onDestroy();
        });
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}