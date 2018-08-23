package layouting.aplikasi.com.soundpoolmediaplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String ACTION_PACKAGE = "com.example.action";
    public static final String ACTION_PLAY = "com.example.play";
    public static final String ACTION_PAUSE = "com.example.pause";
    public static final String ACTION_STOP = "com.example.stop";
    public static final String ACTION_CREATE = "com.example.CREATE";

    MediaPlayer mp = null;

    int serviceID = 777;

    boolean isPaused = false;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void init() {
      mp = new MediaPlayer();
      mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
      AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.kaca_yang_berdebu);

      try {
          mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
      } catch (IOException e) {
          e.printStackTrace();
      }

      mp.setOnPreparedListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        switch (action) {
            case ACTION_CREATE:
                init();
                break;
            case ACTION_PLAY:
                if (!mp.isPlaying() && !isPaused) {
                    mp.prepareAsync();
                } else {
                    isPaused = false;
                    mp.start();
                }
                break;
            case ACTION_PAUSE:
                if (mp.isPlaying()) {
                    mp.pause();
                    isPaused = true;
                }
                break;
            case ACTION_STOP:
                if (mp.isPlaying()) {
                    mp.stop();
                }
        }

        return flags;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) mp.release();
    }
}
