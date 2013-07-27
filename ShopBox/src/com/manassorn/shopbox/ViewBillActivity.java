package com.manassorn.shopbox;

import java.sql.SQLException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.ConfirmBillActivity.BillArrayAdapter;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.value.Bill;

public class ViewBillActivity extends Activity {
	public static final String TAG = "ViewBillActivity";
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
		
		DbHelper dbHelper = DbHelper.getHelper(this);
		SellBillDao dao = SellBillDao.getInstance(dbHelper);
		Bill sellBill;
		try {
			sellBill = dao.getForId(billId);
			
			ListView listView = (ListView) findViewById(R.id.bill_item_list);
			listView.setAdapter(new BillArrayAdapter(this, sellBill.getBillItems()));
			
			findViewById(R.id.confirm_button).setVisibility(View.GONE);
			
			TextView totalText = (TextView) findViewById(R.id.total);
			totalText.setText(String.format("ß%,.2f", sellBill.getTotal()));
		} catch (SQLException e) {
			Log.e(TAG, "Database Error", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_bill, menu);
		return true;
	}

}
