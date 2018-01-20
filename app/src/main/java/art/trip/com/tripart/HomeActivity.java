package art.trip.com.tripart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import art.trip.com.tripart.adapter.HomeAdapter;
import art.trip.com.tripart.adapter.ImageAdapter;
import art.trip.com.tripart.adapter.SoundAdapter;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Home;
import art.trip.com.tripart.model.Image;
import art.trip.com.tripart.model.Sound;
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
        homeRecycler.setAdapter(new HomeAdapter(getData(),this));
    }

    private List<Home> getData(){
        List<Home> list = new ArrayList<>();


        list.add(new Home("Imagenes", new ImageAdapter(Setting.imageList,this)));

        // Audio

        List<Sound> listAudio = new ArrayList<>();
        listAudio.add(new Sound("BingBang X - Reset brain",R.drawable.b6));
        listAudio.add(new Sound("Force",R.drawable.b7));
        listAudio.add(new Sound("Effect cypher ↑↑",R.drawable.b8));
        listAudio.add(new Sound("→$ Dolar one",R.drawable.b9));
        listAudio.add(new Sound("| Count the city |",R.drawable.b10));


        list.add(new Home("Sound", new SoundAdapter(listAudio,this)));

        return list;
    }
}
