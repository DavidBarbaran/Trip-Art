package art.trip.com.tripart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.adapter.HomeAdapter;
import art.trip.com.tripart.adapter.ImageAdapter;
import art.trip.com.tripart.adapter.SoundAdapter;
import art.trip.com.tripart.adapter.VideoAdapter;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Home;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_recycler)
    RecyclerView homeRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        homeRecycler.setLayoutManager(new LinearLayoutManager(this));
        homeRecycler.setAdapter(new HomeAdapter(getData(), this));
    }

    private List<Home> getData() {
        List<Home> list = new ArrayList<>();
        list.add(new Home("Imagenes", new ImageAdapter(Setting.imageList, this), ImageActivity.class));
        list.add(new Home("Audio", new SoundAdapter(Setting.audioList, this), ImageActivity.class));
        list.add(new Home("Video", new VideoAdapter(Setting.videoList, this), ImageActivity.class));
        return list;
    }
}
