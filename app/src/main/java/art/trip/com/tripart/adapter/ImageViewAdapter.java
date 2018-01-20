package art.trip.com.tripart.adapter;

import android.content.Context;
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
import art.trip.com.tripart.listener.OnItemClick;
import art.trip.com.tripart.model.Image;

/**
 * Created by David on 19/01/2018.
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>{

    List<Image> list;
    Context context;

    private OnItemClick onItemClick;

    public ImageViewAdapter(List<Image> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewAdapter.ImageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.gifWebView.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.VISIBLE : View.GONE);
        holder.photoView.setVisibility(list.get(position).getPath().endsWith(".gif") ? View.GONE : View.VISIBLE);
        if (!list.get(position).getPath().endsWith(".gif")){
            Glide.with(context).load(list.get(position).getPath()).into(holder.photoView);
        }
        else{
            holder.gifWebView.loadDataWithBaseURL("", "<html style=\"height=100%\">\n" +
                    "<body bgcolor=\"#212121\" style=\"height: 100%;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\">\n<center>" +
                    "  <div style=\"background-image: url('" + list.get(position).getPath() + "');   width: 100%;\n" +
                    "            height: 100%;\n" +
                    "            background-position: center center;\n" +
                    "            max-height: 100%;\n" +
                    "            margin: 0;\n" +
                    "               position: absolute;\n" +
                    "            padding: 0;\n" +
                    "            background-size:contain;\n" +
                    "            background-repeat: no-repeat;\">\n" +
                    "    &nbsp;\n" +
                    "  </div>\n" +
                    "  </body>\n" +
                    "</html>", "text/html", "utf-8", "");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;
        public WebView gifWebView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.photo_view);
            gifWebView = itemView.findViewById(R.id.web_view);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(getAdapterPosition());
                }
            });
            gifWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction()==MotionEvent.ACTION_UP){
                        onItemClick.onItemClick(getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }
}
