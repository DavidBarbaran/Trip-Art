package art.trip.com.tripart.model;

import android.support.v7.widget.RecyclerView;

/**
 * Created by David on 03/12/2017.
 */

public class Home {

    private String title;
    private RecyclerView.Adapter adapter;
    private Class<?> activity;

    public Home(String title, RecyclerView.Adapter adapter, Class<?> activity) {
        this.title = title;
        this.adapter = adapter;
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }
}
