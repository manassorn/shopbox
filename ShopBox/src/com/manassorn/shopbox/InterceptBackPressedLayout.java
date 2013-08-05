package com.manassorn.shopbox;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class InterceptBackPressedLayout extends FrameLayout {

	public InterceptBackPressedLayout(Context context) {
		super(context);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setLayoutParams(lp);
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			KeyEvent.DispatcherState state = getKeyDispatcherState();
			if(state != null) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
					state.startTracking(event, this);
					return true;
				} else if(event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && state.isTracking(event)) {
					((Activity) getContext()).onBackPressed();
					return true;
				}
			}
		}
		return super.dispatchKeyEventPreIme(event);
	}
	
	
}