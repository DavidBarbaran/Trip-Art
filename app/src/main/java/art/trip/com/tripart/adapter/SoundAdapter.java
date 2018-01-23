package art.trip.com.tripart.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import art.trip.com.tripart.R;
import art.trip.com.tripart.activity.DetailAudioActivity;
import art.trip.com.tripart.config.Setting;
import art.trip.com.tripart.model.Audio;

/**
 * Created by David on 04/12/2017.
 */

public class SoundAdapter  extends RecyclerView.Adapter<SoundAdapter.SoundHolder>{

    List<Audio> list;
    Context context;

    public SoundAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SoundAdapter.SoundHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sound, parent, false));
    }

    @Override
    public void onBindViewHolder(SoundHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);
        holder.titleText.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class SoundHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        TextView titleText;

        public SoundHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.sound_image);
            titleText = itemView.findViewById(R.id.title_sound);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailAudioActivity.class);
                    intent.putExtra(Setting.AUDIO, list.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
