package com.manassorn.shopbox;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	public static final String PREF_PASSCODE = "pref_passcode";
	public static final String PREF_SHOP_NAME = "pref_shopName";
	public static final String PREF_SHOP_BRANCH = "pref_shopBranch";
	public static final String PREF_SHOP_TAX_ID = "pref_shopTaxId";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		PreferenceScreen prefScreen = getPreferenceScreen();
		int count = prefScreen.getPreferenceCount();
		SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
		for(int i=0; i<count; i++) {
			Preference pref = prefScreen.getPreference(i);
			String key = pref.getKey();
			String defaultValue = "";
			if(key.equals(PREF_SHOP_NAME)) {
				defaultValue = "ไม่มี";
			} else {
				defaultValue = "(ไม่จำเป็น)";
			}
			pref.setSummary(sharedPreferences.getString(key, defaultValue));
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
				this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference pref = findPreference(key);
		pref.setSummary(sharedPreferences.getString(key, ""));
	}
}
