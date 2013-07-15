package com.manassorn.shopbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReturnOptionMenuActivity extends Activity implements OnItemClickListener {
    private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_list);
        
        // content
		String[] menus = getResources().getStringArray(R.array.return_option_menu_array);
        listView = (ListView) findViewById(R.id.menu_list);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_list_item, menus));
        listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//    	Intent intent = null;
//    	switch(position) {
//    		case 0:
//    		default: 
//    			intent = new Intent(this, SelectProductActivity.class);
//    			break;
//    	}
//    	startActivity(intent);
	}

}
