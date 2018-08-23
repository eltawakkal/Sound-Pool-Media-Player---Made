package layouting.aplikasi.com.soundpoolmediaplayer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    SoundPool sp;
    boolean isLoadedSound = false;
    int sound;

    Intent serviceMP;

    Button btPlaySp, btPlayMp, btPauseMp, btStopMp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btPlaySp = findViewById(R.id.bt_sp_play);
        btPlayMp = findViewById(R.id.bt_mp_play);
        btPauseMp = findViewById(R.id.bt_mp_pause);
        btStopMp = findViewById(R.id.bt_mp_stop);

        serviceMP = new Intent(this, MediaPlayerService.class);
        serviceMP.setAction(MediaPlayerService.ACTION_CREATE);
        serviceMP.setPackage(MediaPlayerService.ACTION_PACKAGE);
        startService(serviceMP);

        btPlaySp.setOnClickListener(this);
        btPlayMp.setOnClickListener(this);
        btPauseMp.setOnClickListener(this);
        btStopMp.setOnClickListener(this);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoadedSound = true;
            }
        });

        sound = sp.load(this, R.raw.bell, 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceMP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sp_play:
                if (isLoadedSound) {
                    sp.play(sound, 1, 1, 0, 0, 0);
                }
                break;
            case R.id.bt_mp_play:
                serviceMP
                        .setAction(MediaPlayerService.ACTION_PLAY)
                        .setPackage(MediaPlayerService.ACTION_PACKAGE);
                startService(serviceMP);
                break;
            case R.id.bt_mp_pause:
                serviceMP
                        .setAction(MediaPlayerService.ACTION_PAUSE)
                        .setPackage(MediaPlayerService.ACTION_PACKAGE);
                startService(serviceMP);
            case R.id.bt_mp_stop:
                serviceMP
                        .setAction(MediaPlayerService.ACTION_STOP)
                        .setPackage(MediaPlayerService.ACTION_PACKAGE);
                startService(serviceMP);
        }
    }
}
