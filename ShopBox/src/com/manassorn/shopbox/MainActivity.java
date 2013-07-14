package com.manassorn.shopbox;

import com.manassorn.shopbox.R;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends DrawerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// action bar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //
    	Intent intent = null;
    	intent = new Intent(this, TodayBillActivity.class);
    	startActivity(intent);
    	//e3e3e3
    	//d4d4d4
    	//ababab
	}


}
