package art.trip.com.tripart.activity;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.UnknownHostException;
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

    @BindView(R.id.load_more_items)
    AVLoadingIndicatorView loadView;

    @BindView(R.id.load_search)
    AVLoadingIndicatorView loadSearch;

    @BindView(R.id.search_linear)
    View searchLinear;

    @BindView(R.id.search_edittext)
    FontEditText searchEditText;

    @BindView(R.id.search_btn)
    FloatingActionButton searchBtn;

    @BindView(R.id.search_cardview)
    CardView searchCardView;

    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private RestApi restApi = RestApi.RETROFIT.create(RestApi.class);
    private ImageAdapter imageAdapter;
    private int value = 0;
    private boolean isShowKeyboard;
    private StaggeredGridLayoutManager layoutManager;

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
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initRecycler();
        initSearchEdit();
    }

    private void initRecycler() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        imageRecycler.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(Setting.imageListFilter, this, ImageAdapter.TYPE_2);
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
                        getImageById();
                    }
                }
            }
        });
    }

    private void initSearchEdit() {
        searchEditText.setKeyImeChangeListener(new FontEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isShowKeyboard) {
                        hideSearchBar();
                    }
                }
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    loading = false;
                    loadSearch.setVisibility(View.VISIBLE);
                    imageRecycler.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    backActionSearch();
                    getImageByName();
                    return true;
                }
                return false;
            }
        });

        final String blockCharacterSet = "\"\\";
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        searchEditText.setFilters(new InputFilter[] { filter });
    }

    private void getImageById() {
        int index = Setting.imageListFilter.get(Setting.imageListFilter.size() - 1).getId();
        restApi.getImageById(Setting.ID, index - 1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.body().toString().equals("{}")) {
                    List<Image> list = jsonToList(response.body().toString(),
                            new TypeToken<List<Image>>() {
                            }.getType());
                    Setting.imageListFilter.addAll(list);
                    value++;
                    if (value == 10) {
                        value = 0;
                        loadView.hide();
                        loading = true;
                        imageAdapter.notifyDataSetChanged();
                    } else {
                        getImageById();
                    }
                } else {
                    value = 0;
                    loadView.hide();
                    loading = true;
                    imageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t instanceof UnknownHostException) {

                }
            }
        });
    }

    private void getImageByName() {
        String filter = searchEditText.getText().toString();
        String startAt = "\"" + filter + "\"";
        String endAt = "\"" + filter + "\\uf8ff" + "\"";
        restApi.getImageByName(Setting.TITLE, startAt, endAt, Setting.LENGTH_20).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("response", " " + response.body());
                if (response.isSuccessful()) {
                    List<Image> list = jsonToList(response.body().toString(), new TypeToken<List<Image>>() {
                    }.getType());
                    Setting.imageListFilter.clear();
                    Setting.imageListFilter = list;
                    imageAdapter.setList(Setting.imageListFilter);
                    imageAdapter.notifyDataSetChanged();
                    imageRecycler.setVisibility(View.VISIBLE);
                    loadSearch.setVisibility(View.GONE);
                } else {
                    Log.e("error", "no sucefull" + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("error", t + "");
                Log.e("error 2", call + "");
                if (t instanceof JsonSyntaxException) {
                    emptyView.setVisibility(View.VISIBLE);
                    loadSearch.setVisibility(View.GONE);
                }
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
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
        TranslateAnimation animation = new TranslateAnimation(1500.0f, 0.0f, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation.setDuration(Setting.DURATION_ANIMATION);
        animation.setFillAfter(true);
        searchCardView.startAnimation(animation);
    }

    @OnClick(R.id.back_btn)
    public void backActionSearch() {
        hideKeyboard();
        hideSearchBar();
    }

    @OnClick(R.id.search_edittext)
    public void actionSearchEditText() {
        isShowKeyboard = true;
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    private void hideSearchBar() {
        isShowKeyboard = false;
        TranslateAnimation animation1 = new TranslateAnimation(0.0f, 1500.0f, 0.0f, 0.0f);
        animation1.setDuration(Setting.DURATION_ANIMATION);
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
        searchCardView.startAnimation(animation1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(Setting.DURATION_ANIMATION);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                searchBtn.show();
                isShowKeyboard = false;
            }
        }).start();
    }
}
