package com.manassorn.shopbox;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.manassorn.shopbox.db.BillItemDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.SellBill;
import com.manassorn.shopbox.value.ShopAttributes;

public class ReceiveMoneyActivity extends Activity implements OnClickListener,
		OnEditorActionListener {
	private static final String TAG = ReceiveMoneyActivity.class.getSimpleName();
	private ShopAttributes shopAttributes;
	private ArrayList<BillItem> billItems;
	private double total = 0;
	private int billId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_money);

		// action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);

		initParameters();
		initTotalView();
		bindListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receive_money, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ok_button:
				ok();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				|| (actionId == EditorInfo.IME_ACTION_DONE)) {
			ok();
			return true;
		}
		return false;
	}
	
	protected void initParameters() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			shopAttributes = bundle.getParcelable("SHOP_ATTRIBUTES");
			billItems = bundle.getParcelableArrayList("BILL_ITEM_ARRAY");
			total = bundle.getDouble("TOTAL");
		}
	}
	
	protected void initTotalView() {
		TextView totalView = (TextView) findViewById(R.id.total);
		totalView.setText(String.format("�%,.2f", total));
	}
	
	protected void bindListener() {
		EditText receiveMoneyText = (EditText) findViewById(R.id.receive_money);
		receiveMoneyText.setOnEditorActionListener(this);

		Button ok = (Button) findViewById(R.id.ok_button);
		ok.setOnClickListener(this);
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

	protected int insertBill() throws SQLException {
		SellBill sellBill = new SellBill(billItems);
		sellBill.setShopAttributesId(shopAttributes.getId());
		sellBill.setReceiveMoney(getReceiveMoney());
		// long billId = billDbAdapter.insertBill(bill);
		DbHelper dbHelper = DbHelper.getHelper(this);
		SellBillDao sellBillDao = SellBillDao.getInstance(dbHelper);
		BillItemDao billItemDao = BillItemDao.getInstance(dbHelper);
		billId = sellBillDao.insert(sellBill);
		for (BillItem billItem : billItems) {
			billItem.setBillId(billId);
		}
		billItemDao.insert(billItems);

		return billId;
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

	protected void ok() {
		if (validateInput()) {
			try {
				billId = insertBill();
				startGiveChangeActivity();
			} catch (SQLException e) {
				Log.e(TAG, "Database Error", e);
				Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
