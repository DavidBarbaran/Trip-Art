package art.trip.com.tripart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import art.trip.com.tripart.R;

/**
 * Created by David on 9/02/2018.
 */

public class FontEditText extends AppCompatEditText {
    private String fontPath;

    private KeyImeChange keyImeChangeListener;

    public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontEditText);
            fontPath = typedArray.getString(R.styleable.FontEditText_typeface);
            if (fontPath != null && !fontPath.isEmpty()) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
                setTypeface(typeface);
            }
            setTransformationMethod(null);
        }
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyImeChangeListener != null) {
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }

    public interface KeyImeChange {
        public void onKeyIme(int keyCode, KeyEvent event);
    }
}