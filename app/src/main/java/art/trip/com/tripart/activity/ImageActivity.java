package art.trip.com.tripart.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.image_recycler)
    RecyclerView imageRecycler;

    @BindView(R.id.load)
    AVLoadingIndicatorView loadView;

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    RestApi restApi = RestApi.RETROFIT.create(RestApi.class);
    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
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
                        Log.e("ID",  "" + Setting.imageList.get(Setting.imageList.size()-1).getId());


                        Log.e("tag", "LOAD NEXT ITEM");
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    int value = 0;

    private void getAudio(){
        int index = Setting.imageList.get(Setting.imageList.size()-1).getId();
        Log.e("id", index + "");
        restApi.getImageById(Setting.ID, index - 1).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.body().toString().equals("{}")){
                    List<Image> list = jsonToList(response.body().toString(),
                            new TypeToken<List<Image>>() {
                            }.getType());
                    Log.e("SIZE list",  "" + response.body().toString());
                    Setting.imageList.addAll(list);
                    Log.e("SIZE",  "" + Setting.imageList.size());
                    value++;
                    if (value == 10){
                        value = 0;
                        loadView.hide();
                        loading = true;
                        imageAdapter.setList(Setting.imageList);
                        imageAdapter.notifyDataSetChanged();
                    }else {
                        getAudio();
                    }
                }
                else {
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
}
