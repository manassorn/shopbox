package com.manassorn.shopbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ManagerActivity extends DrawerFragmentActivity implements OnItemClickListener {
	private SellFragment sellFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ListView drawerListView = getDrawerListView();
		drawerListView.setOnItemClickListener(this);

		String[] menus = getResources().getStringArray(R.array.drawer_manager_array);
		drawerListView.setAdapter(new DrawerListArrayAdapter(this, menus));

		setMyActionBarColor(getResources().getColor(R.color.manager_violet));
		
		setTitle(getResources().getString(R.string.manager));
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("ออกเมนูผู้จัดการ")
				.setMessage("ต้องการออกจากเมนูผ้จัดการ?")
				.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ManagerActivity.super.onBackPressed();
					}
				}).setNegativeButton("ไม่ใช่", null).show();
		// super.onBackPressed();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			case 1:
			default:
				// replaceFragment(sellFragment());
			case 4:
				onBackPressed();
				break;
		}
	}

	// protected SellFragment sellFragment() {
	// if(sellFragment == null) {
	// sellFragment = new SellFragment();
	// }
	// return sellFragment;
	// }
}
