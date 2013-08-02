package com.manassorn.shopbox;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AutoHideKayboardLayout extends FrameLayout {
	ArrayList<TextView> observedViews = new ArrayList<TextView>();

	public AutoHideKayboardLayout(Context context) {
		super(context);
	}

	public AutoHideKayboardLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoHideKayboardLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(hasEditTextFocus()) {
			if(isTouchOutsideEditText(ev)) {
				hideSoftInput();
				clearEditTextFocus();
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void addObservedView(TextView view) {
		observedViews.add(view);
	}
	
	public void addObservedViewResource(int resourceId) {
		TextView textView = (TextView) findViewById(resourceId);
		addObservedView(textView);
	}
	
	boolean isTouchOutsideEditText(MotionEvent ev) {
		boolean outside = true;
		float touchX = ev.getRawX(); float touchY = ev.getRawY();
		for(TextView view : observedViews) {
			int[] location = new int[2];
			view.getLocationOnScreen(location);
			if(location[0] <= touchX && touchX <= location[0] + view.getWidth() &&
					location[1] <= touchY && touchY <= location[1] + view.getHeight()) {
				outside = false;
				break;
			}
		}
		return outside;
	}
	
	boolean hasEditTextFocus() {
		for(TextView view : observedViews) {
			if(view.hasFocus())
				return true;
		}
		return false;
	}
	
	void clearEditTextFocus() {
		for(TextView view : observedViews) {
			view.clearFocus();
		}
	}
	
	void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		for(TextView editText : observedViews) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}
}
