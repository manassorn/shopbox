package com.manassorn.shopbox;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.BillSelectItemAdapter.OnCheckBoxClickedListener;
import com.manassorn.shopbox.db.BillItemDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Order;
import com.manassorn.shopbox.value.SellBill;

public class ReturnProductActivity extends Activity implements OnClickListener, OnCheckBoxClickedListener {
	private static final String TAG = "ReturnProductActivity";
	ArrayList<Integer> selectedItems;
	int billId;
	List<BillItem> billItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_bill);
		
		selectedItems = new ArrayList<Integer>();
		
		Button submit = (Button) findViewById(R.id.submit_button);
		submit.setText("¤×¹");
		submit.setOnClickListener(this);
		submit.setEnabled(false);

		billId = getIntent().getIntExtra("BILL_ID", 0);
		if(billId != 0) {
			DbHelper dbHelper = DbHelper.getHelper(this);
			BillItemDao billItemDao = BillItemDao.getInstance(dbHelper);
			
			billItems = billItemDao.getForEq(BillItemDao.BILL_ID, billId);
			ListView listView = (ListView) findViewById(R.id.bill_item_list);
			BillSelectItemAdapter adapter = new BillSelectItemAdapter(this, billItems);
			adapter.setOnCheckBoxClickedListener(this);
			listView.setAdapter(adapter);
			
			SellBillDao sellBillDao = SellBillDao.getInstance(dbHelper);
			try {
				SellBill sellBill = sellBillDao.getForId(billId);
				TextView total = (TextView) findViewById(R.id.total);
				total.setText(String.format("ß%,.2f", sellBill.getTotal()));
			} catch (SQLException e) {
				Log.e(TAG, "Database Error", e);
			}
		}
	}

	@Override
	public void onClick(View view) {
		startConfirmReturnActivity();
	}

	@Override
	public void onCheckBoxClicked(CheckBox checkBox, int position) {
		if (checkBox.isChecked()) {
			selectedItems.add(position);
		} else if (selectedItems.contains(position)) {
			selectedItems.remove(Integer.valueOf(position));
		}
		View submitButton = findViewById(R.id.submit_button);
		submitButton.setEnabled(selectedItems.size() > 0);
	}
	
	protected void startConfirmReturnActivity() {
		Intent intent = new Intent(this, EditReturnOrderActivity.class);
		intent.putExtra("SELL_BILL_ID", billId);
		intent.putExtra("ORDER", createOrder());
		startActivity(intent);
	}
	
	protected Order createOrder() {
		OrderManager orderManager = new OrderManager(new Order());
		ArrayList<BillItem> selectedBillItems = new ArrayList<BillItem>(selectedItems.size());
		for(int selectedIndex : selectedItems) {
			orderManager.addProduct((BillProductItem) billItems.get(selectedIndex));
		}
		for(BillItem billItem : billItems) {
			if(billItem instanceof BillSupplementItem) {
				orderManager.addSupplement((BillSupplementItem) billItem);
			}
		}
		return orderManager.getOrder();
	}

}
