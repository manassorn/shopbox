package com.manassorn.shopbox;

import com.manassorn.shopbox.value.Order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK) {
			int billId = data.getIntExtra("BILL_ID", 0);
			Intent intent = new Intent(this, ReturnProductActivity.class);
			intent.putExtra("BILL_ID", billId);
			startActivity(intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	Intent intent = null;
    	switch(position) {
    		case 0:
    		default: 
    			intent = new Intent(this, SelectBillByDateActivity.class);
    	    	startActivityForResult(intent, 0);
    			break;
    	}
	}

}
