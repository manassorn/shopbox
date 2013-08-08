package com.manassorn.shopbox;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class ShopkitAccountPreference extends Preference {

	public ShopkitAccountPreference(Context context) {
		super(context);
	}

	public ShopkitAccountPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ShopkitAccountPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public View getView(View convertView, ViewGroup parent) {
		View view = super.getView(convertView, parent);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(lp);
		return view;
	}

	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
	}

}
