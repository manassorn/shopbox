package com.manassorn.shopbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends DrawerFragmentActivity implements OnItemClickListener {
	private static final int PASSCODE_REQUEST_CODE = 0x1000;
	private SellFragment sellFragment;
	private ReturnMenuFragment returnMenuFragment;
	private DeveloperMenuFragment developerMenuFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		ListView drawerListView = getDrawerListView();
		drawerListView.setOnItemClickListener(this);

        String[] menus = getResources().getStringArray(R.array.drawer_cashier_array);
        drawerListView.setAdapter(new DrawerListArrayAdapter(this, menus));
        
        replaceFragment(sellFragment());
        
//    	Intent intent = null;
//    	intent = new Intent(this, ManagerActivity.class);
//    	startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
        	case 1:
        	default: 
        		replaceFragment(sellFragment());
        		break;
        	case 2:
        		replaceFragment(returnMenuFragment());
        		break;
        	case 15:
        		startPasscodeActivity();
        		break;
        	case 17:
        		replaceFragment(developerMenuFragment());
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == PASSCODE_REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				startManagerActivity();
			}
		} else {
			getCurrentFragment().onActivityResult(requestCode, resultCode, data);
		}
	}

	protected void startPasscodeActivity() {
		Intent intent = new Intent(this, PasscodeActivity.class);
		startActivityForResult(intent, PASSCODE_REQUEST_CODE);
		overridePendingTransition(R.anim.slide_up_in, R.anim.stay_still);
	}
	
	protected void startManagerActivity() {
		Intent intent = new Intent(this, ManagerActivity.class);
		startActivity(intent);
	}

	protected SellFragment sellFragment() {
		if(sellFragment == null) {
			sellFragment = new SellFragment();
		}
		return sellFragment;
	}
	
	protected ReturnMenuFragment returnMenuFragment() {
		if(returnMenuFragment == null) {
			returnMenuFragment = new ReturnMenuFragment();
		}
		return returnMenuFragment;
	}
	
	protected DeveloperMenuFragment developerMenuFragment() {
		if(developerMenuFragment == null) {
			developerMenuFragment = new DeveloperMenuFragment();
		}
		return developerMenuFragment;
	}
}
