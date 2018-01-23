package art.trip.com.tripart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;


import com.wang.avi.AVLoadingIndicatorView;

import java.util.Collections;
import java.util.List;

import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Audio;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.networking.RestApi;
import art.trip.com.tripart.util.DesignUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.load_view)
    AVLoadingIndicatorView loadView;

    @BindView(R.id.continue_btn)
    Button continueBtn;

    RestApi restApi = RestApi.RETROFIT.create(RestApi.class);
    private boolean statusImage;
    private boolean statusAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        DesignUtil.setTransparentStatusBar(this);
        statusImage = false;
        statusAudio = false;

        restApi.getImage().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                Setting.imageList = response.body();
                Collections.reverse(Setting.imageList);
                if (statusAudio) {
                    loadView.hide();
                    continueBtn.setVisibility(View.VISIBLE);
                    continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                } else {
                    statusImage = true;
                }
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.e("FAILURE", t.toString());
            }
        });

        restApi.getAudio().enqueue(new Callback<List<Audio>>() {
            @Override
            public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                Log.e("rsponse", response.body().toString());
                Setting.audioList = response.body();
                Collections.reverse(Setting.audioList);
                if (statusImage) {
                    loadView.hide();
                    continueBtn.setVisibility(View.VISIBLE);
                    continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                } else {
                    statusAudio = true;
                }
            }

            @Override
            public void onFailure(Call<List<Audio>> call, Throwable t) {
                Log.e("FAILURE", t.toString());
            }
        });
    }

    @OnClick(R.id.continue_btn)
    public void continueBtn() {

        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
}
