package com.manassorn.shopbox;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PimpdeedTextView extends TextView {

	public PimpdeedTextView(Context context) {
		super(context);
		setPimpdeedFont(context);
	}

	public PimpdeedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPimpdeedFont(context);
	}

	public PimpdeedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPimpdeedFont(context);
	}

	public void setPimpdeedFont(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Pspimpdeed.ttf");
        this.setTypeface(typeface);
	}
}
