package art.trip.com.tripart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Audio;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.model.Video;
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
    private boolean statusVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        DesignUtil.setTransparentStatusBar(this);
        statusImage = false;
        statusAudio = false;
        statusVideo = false;

        restApi.getImage(Setting.ID, Setting.LENGTH_20).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Setting.imageList = jsonToList(response.body().toString(),
                            new TypeToken<List<Image>>() {
                            }.getType());
                    Collections.reverse(Setting.imageList);
                    if (statusAudio && statusVideo) {
                        loadView.hide();
                        continueBtn.setVisibility(View.VISIBLE);
                        continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                    } else {
                        statusImage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FAILURE IMAGE", t.toString());
            }
        });

      /*  restApi.getImage().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                Setting.imageList = response.body();
                Collections.reverse(Setting.imageList);
                if (statusAudio && statusVideo) {
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
*/
        restApi.getAudio(Setting.ID, Setting.LENGTH_20).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Setting.audioList = jsonToList(response.body().toString(),
                            new TypeToken<List<Audio>>() {
                            }.getType());
                    Collections.reverse(Setting.audioList);
                    if (statusImage && statusVideo) {
                        loadView.hide();
                        continueBtn.setVisibility(View.VISIBLE);
                        continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                    } else {
                        statusAudio = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FAILURE audio", t.toString());
            }
        });

      /*  restApi.getAudio().enqueue(new Callback<List<Audio>>() {
            @Override
            public void onResponse(Call<List<Audio>> call, Response<List<Audio>> response) {
                if (response.isSuccessful()) {
                    Log.e("rsponse", response.body().toString());
                    Setting.audioList = response.body();
                    Collections.reverse(Setting.audioList);
                    if (statusImage && statusVideo) {
                        loadView.hide();
                        continueBtn.setVisibility(View.VISIBLE);
                        continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                    } else {
                        statusAudio = true;
                    }
                } else {
                    Log.e("rsponse FAIL", response.toString());
                }

            }

            @Override
            public void onFailure(Call<List<Audio>> call, Throwable t) {
                Log.e("FAILURE", t.toString());
            }
        });
*/

        restApi.getVideo().enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Setting.videoList = response.body();
                Collections.reverse(Setting.videoList);
                if (statusImage && statusAudio) {
                    loadView.hide();
                    continueBtn.setVisibility(View.VISIBLE);
                    continueBtn.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_up));
                } else {
                    statusVideo = true;
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.continue_btn)
    public void continueBtn() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    private <T> List<T> jsonToList(String json, Type type) {
        JSONObject songs = null;
        JSONArray jsonArray = null;
        try {
            songs = new JSONObject(json);

            Iterator x = songs.keys();
            jsonArray = new JSONArray();

            while (x.hasNext()) {
                String key = (String) x.next();
                jsonArray.put(songs.get(key));
            }
        } catch (JSONException e) {
            Log.e("json key exception", e.getMessage());
        }

        Gson gson = new Gson();
        return gson.fromJson(jsonArray.toString(), type);
    }
}
