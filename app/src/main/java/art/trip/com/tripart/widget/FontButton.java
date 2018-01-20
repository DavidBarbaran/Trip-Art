package art.trip.com.tripart.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import art.trip.com.tripart.R;

/**
 * Created by David on 29/10/2017.
 */

public class FontButton extends AppCompatButton{

    private String fontPath;
    public FontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontButton);
            fontPath = typedArray.getString(R.styleable.FontButton_typeface);
            if (fontPath != null && !fontPath.isEmpty()) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
                setTypeface(typeface);
            }

            setTransformationMethod(null);
        }
    }
}
