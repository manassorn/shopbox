package com.manassorn.shopbox;

import java.sql.SQLException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.manassorn.shopbox.db.BillItemDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.ReturnBillDao;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.ReturnBill;

public class ConfirmReturnBillActivity extends ConfirmBillActivity {
	private static final String TAG = "ConfirmReturnBillActivity";
	int sellBillId;
	int returnBillId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sellBillId = getIntent().getIntExtra("SELL_BILL_ID", 0);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.submit_button:
				insertBill();
				startReturnMoneyActivity();
				break;
		}
	}
	
	long insertBill() {
		ReturnBill returnBill = new ReturnBill(billItems);
		returnBill.setSellBillId(sellBillId);
		
		DbHelper dbHelper = DbHelper.getHelper(this);
		ReturnBillDao returnBillDao = ReturnBillDao.getInstance(dbHelper);
		BillItemDao billItemDao = BillItemDao.getInstance(dbHelper);
		
		try {
			returnBillId = returnBillDao.insert(returnBill);
			for(BillItem billItem : billItems) {
				billItem.setBillId(returnBillId);
			}
			billItemDao.insert(billItems);
		} catch (SQLException e) {
			Log.e(TAG, "Database Error", e);
		}
		
		return returnBillId;
	}
	
	void startReturnMoneyActivity() {
		Intent intent = new Intent(this, ReturnMoneyActivity.class);
		intent.putParcelableArrayListExtra("BILL_ITEM_LIST", billItems);
		intent.putExtra("RETURN_BILL_ID", returnBillId);
		intent.putExtra("TOTAL", total);
		startActivity(intent);
		
	}
}
