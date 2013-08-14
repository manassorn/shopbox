package com.manassorn.shopbox;

import java.sql.SQLException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.ReturnBillDao;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.db.ShopAttributesDao;
import com.manassorn.shopbox.value.ShopAttributes;

public class SettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener {
	private static final String TAG = "SettingsFragment";
	public static final String PREF_PASSCODE = "pref_passcode";
	public static final String PREF_PASSCODE_DEFAULT = "2580";
	public static final String PREF_PASSCODE_DEFAULT_SHA1 = "0EBCDC7BABC0DE9A1D6C7D1C180BFCB8183FA492";
	public static final String PREF_SHOP_NAME = "pref_shopName";
	public static final String PREF_SHOP_BRANCH = "pref_shopBranch";
	public static final String PREF_SHOP_TAX_ID = "pref_shopTaxId";
	ShopAttributesDao shopAttributesDao;
	SellBillDao sellBillDao;
	ReturnBillDao returnBillDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DbHelper dbHelper = DbHelper.getHelper(getActivity());
		shopAttributesDao = ShopAttributesDao.getInstance(dbHelper);
		sellBillDao = SellBillDao.getInstance(dbHelper);
		returnBillDao = ReturnBillDao.getInstance(dbHelper);

		addPreferencesFromResource(R.xml.preferences);

		Preference pref = findPreference(PREF_SHOP_NAME);
		pref.setOnPreferenceChangeListener(this);
		pref = findPreference(PREF_SHOP_BRANCH);
		pref.setOnPreferenceChangeListener(this);
		pref = findPreference(PREF_SHOP_TAX_ID);
		pref.setOnPreferenceChangeListener(this);

		ShopAttributes shopAttributes = shopAttributesDao.getForLast();
		syncPreference(PREF_SHOP_NAME, shopAttributes.getShopName());
		syncPreference(PREF_SHOP_BRANCH, shopAttributes.getBranchName());
		syncPreference(PREF_SHOP_TAX_ID, shopAttributes.getTaxId());

		initPreferenceSummary();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(updateShopAttributesDb(preference.getKey(), newValue.toString())) {
			preference.setSummary(newValue.toString());
			return true;
		}
		return false;
	}

	protected void syncPreference(String preferenceKey, String dbValue) {
		PreferenceScreen prefScreen = getPreferenceScreen();
		SharedPreferences prefs = prefScreen.getSharedPreferences();
		String prefValue = prefs.getString(preferenceKey, null);

		if (!dbValue.equals(prefValue)) {
			prefs.edit().putString(preferenceKey, dbValue).commit();
			
			Preference pref = findPreference(preferenceKey);
			if(pref instanceof EditTextPreference) {
				((EditTextPreference) pref).getEditText().setText(dbValue);
			}
		}
	}

	protected void initPreferenceSummary() {
		PreferenceScreen prefScreen = getPreferenceScreen();
		int count = prefScreen.getPreferenceCount();
		SharedPreferences sharedPreferences = prefScreen.getSharedPreferences();
		for (int i = 0; i < count; i++) {
			Preference pref = prefScreen.getPreference(i);
			String key = pref.getKey();
			String defaultValue = "";
			if (key.equals(PREF_SHOP_NAME)) {
				defaultValue = "ไม่มี";
			} else {
				defaultValue = "(ไม่จำเป็น)";
			}
			String prefValue = sharedPreferences.getString(key, defaultValue);
			prefValue = prefValue.length() == 0 ? defaultValue : prefValue;
			pref.setSummary(prefValue);
		}
	}

	protected boolean updateShopAttributesDb(String preferenceKey, String preferenceValue) {
		ShopAttributes shopAttributes = shopAttributesDao.getForLast();
		changeShopAttributes(shopAttributes, preferenceKey, preferenceValue);
		try {
			if (hasBillLinkWith(shopAttributes)) {
				shopAttributesDao.insert(shopAttributes);
			} else {
				shopAttributesDao.update(shopAttributes);
			}
			return true;
		} catch (SQLException e) {
			Toast.makeText(getActivity(), "แก้ไขข้อมูลไม่ได้", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Database Error", e);
			return false;
		}
	}

	protected void changeShopAttributes(ShopAttributes shopAttributes, String preferenceKey,
			String preferenceValue) {
		if (preferenceKey.equals(PREF_SHOP_NAME)) {
			shopAttributes.setShopName(preferenceValue);
		} else if (preferenceKey.equals(PREF_SHOP_BRANCH)) {
			shopAttributes.setBranchName(preferenceValue);
		} else if (preferenceKey.equals(PREF_SHOP_TAX_ID)) {
			shopAttributes.setTaxId(preferenceValue);
		}
	}

	protected boolean hasBillLinkWith(ShopAttributes shopAttributes) {
		long count = 0;
		count += sellBillDao
				.queryBuilder()
				.where(BillDao.SHOP_ATTRIBUTES_ID + "=?",
						new String[] { "" + shopAttributes.getId() }).count();
		count += returnBillDao
				.queryBuilder()
				.where(BillDao.SHOP_ATTRIBUTES_ID + "=?",
						new String[] { "" + shopAttributes.getId() }).count();
		return count != 0;
	}
}
