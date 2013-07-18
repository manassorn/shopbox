package com.manassorn.shopbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.R;
import com.manassorn.shopbox.ConfirmBillActivity.BillArrayAdapter;
import com.manassorn.shopbox.db.BillDbAdapter;
import com.manassorn.shopbox.value.Bill;

public class ViewBillActivity extends Activity {
	private BillDbAdapter billDbAdapter;
	private int billId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_bill);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			billId = bundle.getInt("BILL_ID");
		}
		billDbAdapter = new BillDbAdapter(this);
		billDbAdapter.open();
		
		Bill bill = billDbAdapter.getBill(billId);
		
		ListView listView = (ListView) findViewById(R.id.product_list);
		listView.setAdapter(new BillArrayAdapter(this, bill.getBillItems()));
		
		findViewById(R.id.confirm_button).setVisibility(View.GONE);
		
		TextView totalText = (TextView) findViewById(R.id.total);
		totalText.setText(String.format("ß%,.2f", bill.getTotal()));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (billDbAdapter != null) {
			billDbAdapter.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_bill, menu);
		return true;
	}

}
