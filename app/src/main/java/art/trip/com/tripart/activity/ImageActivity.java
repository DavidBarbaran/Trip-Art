package art.trip.com.tripart.activity;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.adapter.ImageAdapter;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.networking.RestApi;
import art.trip.com.tripart.widget.FontEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.image_recycler)
    RecyclerView imageRecycler;

    @BindView(R.id.load)
    AVLoadingIndicatorView loadView;

    @BindView(R.id.search_linear)
    View searchLinear;

    @BindView(R.id.search_edittext)
    FontEditText searchEditText;

    @BindView(R.id.search_btn)
    FloatingActionButton searchBtn;

    @BindView(R.id.search_cardview)
    CardView searchCardview;

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    RestApi restApi = RestApi.RETROFIT.create(RestApi.class);
    ImageAdapter imageAdapter;
    int value = 0;
    private boolean isShowKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        isShowKeyboard = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageRecycler.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(Setting.imageList, this, ImageAdapter.TYPE_2);
        imageRecycler.setAdapter(imageAdapter);
        imageRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if (loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        loadView.show();
                        loading = false;
                        getAudio();
                        Log.e("ID", "" + Setting.imageList.get(Setting.imageList.size() - 1).getId());
                        Log.e("tag", "LOAD NEXT ITEM");
                    }
                }
            }
        });
        searchEditText.setKeyImeChangeListener(new FontEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                   isShowKeyboard = false;

                }
            }
        });
    }

    private void getAudio() {
        int index = Setting.imageList.get(Setting.imageList.size() - 1).getId();
        Log.e("id", index + "");
        restApi.getImageById(Setting.ID, index - 1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.body().toString().equals("{}")) {
                    List<Image> list = jsonToList(response.body().toString(),
                            new TypeToken<List<Image>>() {
                            }.getType());
                    Log.e("SIZE list", "" + response.body().toString());
                    Setting.imageList.addAll(list);
                    Log.e("SIZE", "" + Setting.imageList.size());
                    value++;
                    if (value == 10) {
                        value = 0;
                        loadView.hide();
                        loading = true;
                        imageAdapter.setList(Setting.imageList);
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        getAudio();
                    }
                } else {
                    value = 0;
                    loadView.hide();
                    loading = true;
                    imageAdapter.setList(Setting.imageList);
                    imageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
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

    @OnClick(R.id.search_btn)
    public void actionSearch() {
        isShowKeyboard = true;
        searchBtn.hide();
        searchLinear.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        TranslateAnimation animation1 = new TranslateAnimation(1000.0f, 0.0f, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchEditText.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setDuration(600);
        animation1.setFillAfter(true);
        searchCardview.startAnimation(animation1);//your_view for mine is imageView

    }

    @OnClick(R.id.back_btn)
    public void backActionSearch() {

        TranslateAnimation animation1 = new TranslateAnimation(0.0f, 1000.0f, 0.0f, 0.0f);
        animation1.setDuration(400);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                searchLinear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setFillAfter(true);
        searchCardview.startAnimation(animation1);

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("hide","keyboard " + isShowKeyboard);
                if (isShowKeyboard){
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                searchBtn.show();
                isShowKeyboard = false;
            }
        }).start();

    }

    @OnClick(R.id.search_edittext)
    public void actionSearchEditText(){
        isShowKeyboard  =true;
        Log.e("click","editext search");
    }
}
