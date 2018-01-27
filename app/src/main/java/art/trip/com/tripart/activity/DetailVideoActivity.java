package art.trip.com.tripart.activity;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import art.trip.com.tripart.R;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Video;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class DetailVideoActivity extends AppCompatActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);
        ButterKnife.bind(this);
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Video video = (Video) getIntent().getSerializableExtra(Setting.VIDEO);
        videoPlayer.setUp(video.getPath()
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, video.getTitle());
     videoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //videoPlayer.thumbImageView.setAdjustViewBounds(true);

        Glide.with(this).load(video.getImage()).into( videoPlayer.thumbImageView);



    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
