package art.trip.com.tripart.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import art.trip.com.tripart.R;

/**
 * Created by David on 04/12/2017.
 */

public class LikeButton extends AppCompatImageView {

    private boolean like;
    private int icon;
    private int iconPressed;

    public LikeButton(Context context) {
        super(context);
        setImageResource(R.drawable.ic_like);
    }

    public LikeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.ic_like);
    }

    public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.drawable.ic_like);
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIconPressed() {
        return iconPressed;
    }

    public void setIconPressed(int iconPressed) {
        this.iconPressed = iconPressed;
    }
}
