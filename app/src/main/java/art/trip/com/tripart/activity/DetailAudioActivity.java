package art.trip.com.tripart.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import art.trip.com.tripart.R;
import art.trip.com.tripart.util.DesignUtil;
import art.trip.com.tripart.widget.VisualizerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailAudioActivity extends AppCompatActivity {


    @BindView(R.id.audio_seekbar)
    SeekBar seekBar;

    @BindView(R.id.play_btn)
    Button playBtn;

    @BindView(R.id.visualizer)
    VisualizerView visualizerView;

    MediaPlayer m;
    Runnable runnable;
    Handler handler;
    private boolean statusPlay;

    private Visualizer mVisualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_audio);
        ButterKnife.bind(this);
        DesignUtil.setTransparentStatusBar(this);
        seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        handler = new Handler();
        statusPlay = true;
        try {
            m = new MediaPlayer();
            m.setAudioStreamType(AudioManager.STREAM_MUSIC);
            AssetFileDescriptor descriptor = getAssets().openFd("disclosure.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mVisualizer = new Visualizer(m.getAudioSessionId());
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    DetailAudioActivity.this.visualizerView.updateVisualizer(waveform);
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false);
            mVisualizer.setEnabled(true);
            m.prepare();

            m.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    playCycle();
                    mp.start();
                    //mVisualizer.setEnabled(false);
                }
            });

        } catch (Exception e) {
            Log.e("error audio", e.getMessage());
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    m.seekTo(progress);
                }
            }
        });



    }

    public void playCycle() {
        seekBar.setProgress(m.getCurrentPosition());
        if (m.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

    }

    @OnClick(R.id.play_btn)
    public void actionPlay() {
        if (statusPlay) {
            playBtn.setBackgroundResource(R.drawable.ic_play);
            m.pause();
        } else {
            playBtn.setBackgroundResource(R.drawable.ic_pause);
            m.start();
            playCycle();

        }
        statusPlay = !statusPlay;
    }

    @Override
    protected void onResume() {
        super.onResume();
        m.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        m.release();
        handler.removeCallbacks(runnable);
    }
}
