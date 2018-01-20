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
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.model.Sound;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);
        DesignUtil.setTransparentStatusBar(this);

        restApi.getData().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                Setting.imageList =response.body();
                Collections.reverse(Setting.imageList);
                loadView.hide();
                continueBtn.setVisibility(View.VISIBLE);
                continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                Log.e("FAILURE",t.toString());
            }
        });

        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadView.hide();
                            continueBtn.setVisibility(View.VISIBLE);
                            continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));


                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
    }

    @OnClick(R.id.continue_btn)
    public void continueBtn() {

        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
}
