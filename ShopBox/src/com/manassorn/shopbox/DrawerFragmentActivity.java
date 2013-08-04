package com.manassorn.shopbox;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerFragmentActivity extends DrawerActivity implements OnClickListener {
	private Fragment currentFragment;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_drawer_fragment);
		
		getActionBar().hide();
		
		drawerLayout = getDrawerLayout();
		drawerListView = getDrawerListView();
        
        findViewById(R.id.drawer_button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.drawer_button) {
			toggleDrawer();
		} else {
			//because MainActivity only have 2 onclick, drawer_button and menu buttons
			MenuItem menuItem = (MenuItem) v.getTag();
			getCurrentFragment().onOptionsItemSelected(menuItem);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(getCurrentFragment() != null) {
			// clear all menu
			ViewGroup menuLayout = (ViewGroup) findViewById(R.id.main_menu);
			menuLayout.removeAllViews();
			// call child fragment
			getCurrentFragment().onCreateOptionsMenu(menu, getMenuInflater());
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
		menuItemView.setImageDrawable(menuItem.getIcon());
		menuItemView.setId(menuItem.getItemId());
		menuItemView.setVisibility(menuItem.isVisible() ? View.VISIBLE : View.GONE);
		TypedArray a = obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
		Drawable bg = a.getDrawable(0);
		menuItemView.setBackground(bg);
		// setPadding must call after setBackground
		int pixels = getResources().getDimensionPixelSize(R.dimen.dp8);
		menuItemView.setPadding(pixels, pixels, pixels, pixels);
		a.recycle();
		return menuItemView;
	}

	protected void replaceFragment(Fragment fragment) {
		currentFragment = fragment;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        
        invalidateOptionsMenu();
        drawerLayout.closeDrawer(drawerListView);
	}

	public Fragment getCurrentFragment() {
		return currentFragment;
	}
	
	public void setCurrentFragment(Fragment fragment) {
		currentFragment = fragment;
	}
	
	public void setTitle(String title) {
		TextView view = (TextView) findViewById(R.id.title);
		view.setText(title);
	}
	
	public void setMyActionBarColor(int color) {
		View myActionBar = findViewById(R.id.my_actionbar);
		myActionBar.setBackgroundColor(color);
	}
}
