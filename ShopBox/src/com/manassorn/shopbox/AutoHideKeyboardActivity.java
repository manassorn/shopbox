package com.manassorn.shopbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class AutoHideKeyboardActivity extends Activity {
	AutoHideKayboardLayout theView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theView = (AutoHideKayboardLayout) getLayoutInflater().inflate(
				R.layout.activity_auto_hide_keyboard, null);
		super.setContentView(theView);
	}

	@Override
	public void setContentView(final int layoutResID) {
		ViewGroup frame = (ViewGroup) findViewById(R.id.auto_hide_keyboard_frame);
		getLayoutInflater().inflate(layoutResID, frame, true);

	}

	public void addObservedView(TextView view) {
		theView.addObservedView(view);
    }
	
	public void addObservedViewResource(int resourceId) {
		theView.addObservedViewResource(resourceId);
	}
}
