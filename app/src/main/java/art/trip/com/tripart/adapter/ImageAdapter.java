package art.trip.com.tripart.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

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

    public ImageAdapter(List<Image> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageAdapter.ImageHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final ImageHolder holder, final int position) {
        holder.gifText.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.VISIBLE : View.GONE);
        int size150 = (int) context.getResources().getDimension(R.dimen.size150);
        list.get(position).setPosition(position);
        //.override(size150, size150)
        Glide.with(context).asBitmap()
                .load(list.get(position).getPath()).apply(new RequestOptions()
                .placeholder(R.drawable.place).error(R.drawable.place).override(size150, size150))
                .thumbnail(0.2f).transition(BitmapTransitionOptions.withCrossFade(600))
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startSharedActivity(holder.image, list.get(position));
                Intent intent = new Intent(context, DetailImageActivity.class);
                intent.putExtra(Setting.IMAGE, list.get(position));
                context.startActivity(intent);
            }
        });
    }

    public void startSharedActivity(View view, Image item) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context, view, Setting.TRANSITION);

        Intent intent = new Intent(context, DetailImageActivity.class);
        intent.putExtra(Setting.IMAGE, item);
        ActivityCompat.startActivity(context, intent, options.toBundle());
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