package com.manassorn.shopbox;

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
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
        	case 1:
        	default: 
//        		replaceFragment(sellFragment());
        }
	}

//	protected SellFragment sellFragment() {
//		if(sellFragment == null) {
//			sellFragment = new SellFragment();
//		}
//		return sellFragment;
//	}
}
