package art.trip.com.tripart.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(ImageHolder holder, int position) {
        holder.webView.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.VISIBLE : View.GONE);
        holder.gifText.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.VISIBLE : View.GONE);
        holder.image.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.GONE : View.VISIBLE);
        list.get(position).setPosition(position);
        if (!list.get(position).getPath().endsWith(".gif")){
            Glide.with(context).load(list.get(position).getPath()).into(holder.image);
        }
        else{
            holder.webView.loadDataWithBaseURL("", "<html>\n" + "<center>" +
                    "<body style=\"margin: 0; padding: 0\" bgcolor=\"white\">\n" +
                    "<div class=\"center-cropped\" style=\"background-image: url('" + list.get(position).getPath() + "'); width: 100%; padding-top: 100%; background-position: center center; background-repeat: no-repeat;background-size: cover;\">&nbsp;</div>"
                    +
                    "</body>", "text/html", "utf-8", "");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {

        View view;
        public WebView webView;
        public ImageView image;
        TextView gifText;

        public ImageHolder(View itemView) {
            super(itemView);
            view = itemView;
            webView = itemView.findViewById(R.id.web_view);
            image = itemView.findViewById(R.id.image);
            gifText = itemView.findViewById(R.id.gif_text);
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Intent intent = new Intent(context, DetailImageActivity.class);
                        intent.putExtra(Setting.IMAGE, list.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                    return (event.getAction() == MotionEvent.ACTION_MOVE);

                }
            });
           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailImageActivity.class);
                    intent.putExtra(Setting.IMAGE, list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }
    }
}