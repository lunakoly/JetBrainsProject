package ru.luna_koly.jetbrainsproject.customUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;

import ru.luna_koly.jetbrainsproject.R;

/**
 * Created with love by iMac on 17.06.17.
 */

public class TypefaceButton extends Button {

    public TypefaceButton(Context context) {
        super(context);
    }

    public TypefaceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface tf;

        try {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TypefaceButton);
            String font = ta.getString(R.styleable.TypefaceButton_typeface);

            tf = Typeface.createFromAsset(context.getAssets(), font);
        } catch (Exception e) {
            String font = getResources().getString(R.string.free_sans_bold_font);
            tf = Typeface.createFromAsset(context.getAssets(), font);
        }

        setTypeface(tf);
    }

    public TypefaceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
