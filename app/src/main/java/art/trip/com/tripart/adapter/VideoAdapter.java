package art.trip.com.tripart.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.activity.DetailAudioActivity;
import art.trip.com.tripart.activity.DetailVideoActivity;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Video;

/**
 * Created by David on 26/01/2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {


    private Context context;
    private List<Video> list;

    public VideoAdapter(List<Video> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent,false);
        VideoAdapter.VideoHolder viewHolder = new VideoAdapter.VideoHolder(view);



       // int margin = (int) context.getResources().getDimension(R.dimen.spacing6);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        holder.titleText.setText(list.get(position).getTitle());
        int orientation = context.getResources().getConfiguration().orientation;

        int spacing = (int) context.getResources().getDimension(R.dimen.spacing26);
        int spacing20 = (int) context.getResources().getDimension(R.dimen.spacing20);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("orientation","portrait");
            holder.containView.getLayoutParams().width = getScreenWidth(context)-spacing-spacing20;
            holder.containView.getLayoutParams().height  =getScreenWidth(context)/2;
        } else {
            Log.e("orientation","not portrait");
            holder.containView.getLayoutParams().width = getScreenHeight(context)-spacing-spacing20;
            holder.containView.getLayoutParams().height  =getScreenHeight(context)/2;
        }

       /* MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(list.get(position).getPath(), new HashMap<String, String>());
        Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond
        holder.image.setImageBitmap(bmFrame);*/
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        TextView titleText;
        View containView;

        public VideoHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.video_image);
            titleText = itemView.findViewById(R.id.title_video_text);
            containView = itemView.findViewById(R.id.contain_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailVideoActivity.class);
                    intent.putExtra(Setting.VIDEO, list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
