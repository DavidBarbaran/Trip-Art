package art.trip.com.tripart.model;

import android.support.v7.widget.RecyclerView;

/**
 * Created by David on 03/12/2017.
 */

public class Home {

    private String title;
    private RecyclerView.Adapter adapter;

    public Home(String title, RecyclerView.Adapter adapter) {
        this.title = title;
        this.adapter = adapter;
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
}
