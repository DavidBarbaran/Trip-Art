package art.trip.com.tripart.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.listener.OnItemClick;
import art.trip.com.tripart.model.Image;

/**
 * Created by David on 19/01/2018.
 */

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {

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
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        boolean isGif = list.get(position).getPath().endsWith(".gif");
        holder.gifWebView.setVisibility(isGif ? View.VISIBLE : View.GONE);
        holder.photoView.setVisibility(isGif ? View.GONE : View.VISIBLE);
        if (!isGif) {
            holder.gifWebView.setVisibility(View.GONE);
            Glide.with(context).load(list.get(position).getPath()).apply(new RequestOptions()
                    .error(R.drawable.ic_place_image).dontTransform())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.placeImage.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.photoView);
        } else {
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
            holder.gifWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    Log.e("load view", "yes");
                }
            });

            holder.gifWebView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    Log.e("pos", position + " - " + progress);
                    if (progress == 100) {
                        holder.placeImage.setVisibility(View.GONE);
                        holder.gifWebView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView photoView;
        public WebView gifWebView;
        public ImageView placeImage;

        public ImageViewHolder(View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.photo_view);
            gifWebView = itemView.findViewById(R.id.web_view);
            placeImage = itemView.findViewById(R.id.place_image);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onItemClick(getAdapterPosition());
                }
            });
            gifWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        onItemClick.onItemClick(getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }
}
