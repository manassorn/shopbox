package com.manassorn.shopbox;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends DrawerActivity implements OnItemClickListener, OnClickListener {
	private static final int PASSCODE_REQUEST_CODE = 0x1000;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private Fragment currentFragment;
	private SellFragment sellFragment;
	private ReturnMenuFragment returnMenuFragment;
	private DeveloperMenuFragment developerMenuFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getActionBar().hide();
        
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
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.drawer_button) {
			if(drawerLayout.isDrawerOpen(drawerListView)) {
		        drawerLayout.closeDrawer(drawerListView);
			} else {
				drawerLayout.openDrawer(drawerListView);
			}
		} else {
			//because MainActivity only have 2 onclick, drawer_button and menu buttons
			MenuItem menuItem = (MenuItem) v.getTag();
			currentFragment.onOptionsItemSelected(menuItem);
		}
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
	//			replaceFragment(managerFragment());
			}
		} else {
			currentFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(currentFragment != null) {
			// clear all menu
			ViewGroup menuLayout = (ViewGroup) findViewById(R.id.main_menu);
			menuLayout.removeAllViews();
			// call child fragment
			currentFragment.onCreateOptionsMenu(menu, getMenuInflater());
			// show menu in custom actionbar
			for(int i=0; i<menu.size(); i++) {
				MenuItem menuItem = menu.getItem(i);
				ImageView menuItemView = createMenuView(menuItem);
				menuLayout.addView(menuItemView);
				menuItemView.setTag(menuItem);
				menuItemView.setOnClickListener(this);
			}
		}
		// hide menu in real actionbar
		return false;
	}
	
	protected ImageView createMenuView(MenuItem menuItem) {
		ImageView menuItemView = new ImageView(this);
		menuItemView.setAdjustViewBounds(true);
		menuItemView.setClickable(true);
		//TODO - menu padding is wrong
		int pixels = getResources().getDimensionPixelSize(R.dimen.dp8);
		menuItemView.setPadding(pixels, pixels, pixels, pixels);
		menuItemView.setImageDrawable(menuItem.getIcon());
		menuItemView.setId(menuItem.getItemId());
		menuItemView.setVisibility(menuItem.isVisible() ? View.VISIBLE : View.GONE);
		TypedArray a = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
		Drawable bg = a.getDrawable(0);
		menuItemView.setBackground(bg);
		a.recycle();
		return menuItemView;
	}

	protected void startPasscodeActivity() {
		Intent intent = new Intent(this, PasscodeActivity.class);
		startActivityForResult(intent, PASSCODE_REQUEST_CODE);
		overridePendingTransition(R.anim.slide_up_in, R.anim.stay_still);
	}
	
	protected void replaceFragment(Fragment fragment) {
		currentFragment = fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
        invalidateOptionsMenu();
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
