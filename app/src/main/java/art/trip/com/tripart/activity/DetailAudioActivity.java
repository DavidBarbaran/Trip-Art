package art.trip.com.tripart.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import art.trip.com.tripart.R;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Audio;
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

    @BindView(R.id.track_image)
    ImageView trackImage;

    @BindView(R.id.place_audio_image)
    ImageView placeAudioImage;

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
        Audio audio = (Audio) getIntent().getSerializableExtra(Setting.AUDIO);
        Glide.with(this).load(audio.getImage()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                placeAudioImage.setVisibility(View.GONE);
                return false;
            }
        }).into(trackImage);
        handler = new Handler();
        statusPlay = true;
        try {
            m = new MediaPlayer();
            m.setAudioStreamType(AudioManager.STREAM_MUSIC);
            m.setDataSource(audio.getTrack());

            // m.prepareAsync();
          /*  AssetFileDescriptor descriptor = getAssets().openFd("disclosure.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            */

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
            m.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    mp.start();
                    playCycle();
                    //mVisualizer.setEnabled(false);
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    m.prepareAsync();
                }
            }).start();

            m.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    seekBar.setSecondaryProgress(i);
                }
            });

        } catch (Exception e) {
            Log.e("error audio", e.getMessage() + "");
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
        if (m.isPlaying()) {
            seekBar.setProgress(m.getCurrentPosition());
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