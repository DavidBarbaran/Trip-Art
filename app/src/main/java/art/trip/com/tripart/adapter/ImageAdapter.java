package art.trip.com.tripart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.activity.DetailImageActivity;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Image;

/**
 * Created by David on 03/12/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    List<Image> list;
    Context context;
    private int type;
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;

    public ImageAdapter(List<Image> list, Context context) {
        this.list = list;
        this.context = context;
        type = TYPE_1;
    }

    public ImageAdapter(List<Image> list, Context context, int type) {
        this.list = list;
        this.context = context;
        this.type = type;
    }


    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (type) {
            case 1:
                return new ImageAdapter.ImageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image, parent, false));
            case 2:
                return new ImageAdapter.ImageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_all_image, parent, false));
            default:
                return new ImageAdapter.ImageHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_image, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, final int position) {
        holder.gifText.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.VISIBLE : View.GONE);
        int size150 = (int) context.getResources().getDimension(R.dimen.size150);
        list.get(position).setPosition(position);
        //.override(size150, size150)
        Glide.with(context).asBitmap()
                .load(list.get(position).getPath()).apply(new RequestOptions()
                .placeholder(R.drawable.place_image).error(R.drawable.place_image).override(size150, size150))
                .thumbnail(0.2f).transition(BitmapTransitionOptions.withCrossFade(600))
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startSharedActivity(holder.image, list.get(position));
                Intent intent = new Intent(context, DetailImageActivity.class);
                intent.putExtra(Setting.IMAGE, list.get(position));
                intent.putExtra(Setting.LIST_IMAGE, type);
                Log.e("extra", list.get(position).getTitle());
                context.startActivity(intent);
            }
        });
    }

    public void setList(List<Image> list) {
       this.list.clear();
        this.list.addAll(list);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        View view;
        public ImageView image;
        TextView gifText;

        public ImageHolder(View itemView) {
            super(itemView);
            view = itemView;
            image = itemView.findViewById(R.id.image);
            gifText = itemView.findViewById(R.id.gif_text);



        }
    }
}