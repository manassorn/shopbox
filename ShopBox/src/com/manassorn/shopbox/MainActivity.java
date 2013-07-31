package com.manassorn.shopbox;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends DrawerActivity implements OnItemClickListener, OnClickListener {
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private SellMenuFragment sellMenuFragment;
	private ReturnMenuFragment returnMenuFragment;
	private DeveloperMenuFragment developerMenuFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().hide();
		
//        getActionBar().setDisplayHomeAsUpEnabled(true);
////        getActionBar().setIcon(R.drawable.barcode_icon);
//        getActionBar().setCustomView(R.layout.main_actionbar);
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        
        drawerLayout = getDrawerLayout();
        drawerListView = getDrawerListView();
        drawerListView.setOnItemClickListener(this);

        String[] menus = getResources().getStringArray(R.array.drawer_nav_array);
        drawerListView.setAdapter(new DrawerListArrayAdapter(this, menus));
        
        findViewById(R.id.drawer_button).setOnClickListener(this);
        //
//    	Intent intent = null;
//    	intent = new Intent(this, ReturnOptionMenuActivity.class);
//    	startActivity(intent);
    	Intent intent = null;
    	intent = new Intent(this, PasscodeActivity.class);
    	startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.drawer_button) {
			if(drawerLayout.isDrawerOpen(drawerListView)) {
		        drawerLayout.closeDrawer(drawerListView);
			} else {
				drawerLayout.openDrawer(drawerListView);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
        	case 1:
        	default: 
        		replaceFragment(sellMenuFragment());
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
		if(resultCode == Activity.RESULT_OK) {
//			replaceFragment(managerFragment());
		}
	}
	
	protected void startPasscodeActivity() {
		Intent intent = new Intent(this, PasscodeActivity.class);
		startActivityForResult(intent, 0);
		overridePendingTransition(R.anim.slide_up_in, R.anim.stay_still);
	}
	
	protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        drawerLayout.closeDrawer(drawerListView);
	}

//	@Override
//	public void onBackPressed() {
//		if(drawerLayout.isDrawerOpen(drawerListView)) {
//	        drawerLayout.closeDrawer(drawerListView);
//		} else {
//			drawerLayout.openDrawer(drawerListView);
//		}
//	}

	protected SellMenuFragment sellMenuFragment() {
		if(sellMenuFragment == null) {
			sellMenuFragment = new SellMenuFragment();
		}
		return sellMenuFragment;
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
