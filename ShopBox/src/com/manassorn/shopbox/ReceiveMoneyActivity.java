package com.manassorn.shopbox;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.BillItemDao;
import com.manassorn.shopbox.db.Dao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.value.Bill;
import com.manassorn.shopbox.value.BillItem;

public class ReceiveMoneyActivity extends Activity implements OnClickListener {
	private static final String TAG = ReceiveMoneyActivity.class.getSimpleName();
	private ArrayList<BillItem> billItems;
	private int billId;
	private double total = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_money);

		Button ok = (Button) findViewById(R.id.ok_button);
		ok.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			billItems = bundle.getParcelableArrayList("BILL_ITEM_ARRAY");
			total = bundle.getDouble("TOTAL");
		}

		TextView totalView = (TextView) findViewById(R.id.total);
		totalView.setText(String.format("ß%,.2f", total));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive_money, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ok_button:
				if (validateInput()) {
					billId = insertBill();
					startGiveChangeActivity();
				}
		}
	}

	protected boolean validateInput() {
		EditText receiveMoneyText = (EditText) findViewById(R.id.receive_money);
		String strReceiveMoney = receiveMoneyText.getText().toString();
		if (strReceiveMoney.length() == 0) {
			Toast.makeText(this, R.string.empty_receive_money_input, Toast.LENGTH_SHORT).show();
			return false;
		}
		double receiveMoney = Double.parseDouble(strReceiveMoney);
		if (receiveMoney < total) {
			Toast.makeText(this, R.string.receive_money_less_than_total, Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	protected int insertBill() {
		Bill bill = new Bill(billItems);
		bill.setReceiveMoney(getReceiveMoney());
//		long billId = billDbAdapter.insertBill(bill);
		DbHelper dbHelper = DbHelper.getHelper(this);
		BillDao billDao = BillDao.getInstance(dbHelper);
		BillItemDao billItemDao = BillItemDao.getInstance(dbHelper);
		try {
			billDao.insert(bill);
			billItemDao.insert(billItems);
		} catch (SQLException e) {
			Log.e(TAG, "Database Error", e);
		}
		
		return (int) billId;
	}

	protected void startGiveChangeActivity() {
		double receiveMoney = getReceiveMoney();
		Intent intent = new Intent(this, GiveChangeActivity.class);
		intent.putExtra("BILL_ID", billId);
		intent.putExtra("RECEIVE_MONEY", receiveMoney);
		intent.putExtra("TOTAL", total);
		startActivity(intent);
	}
	
	protected double getReceiveMoney() {
		EditText receiveMoneyText = (EditText) findViewById(R.id.receive_money);
		double receiveMoney = Double.parseDouble(receiveMoneyText.getText().toString());
		return receiveMoney;
	}
}
