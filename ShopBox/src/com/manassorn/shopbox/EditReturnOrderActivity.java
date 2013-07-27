package com.manassorn.shopbox;

import java.util.ArrayList;

import com.manassorn.shopbox.value.BillItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class EditReturnOrderActivity extends EditOrderActivity {
	private int sellBillId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Button discount = (Button) findViewById(R.id.discount_button);
		discount.setVisibility(View.GONE);

		Button markUp = (Button) findViewById(R.id.markup_button);
		markUp.setVisibility(View.GONE);

		Button submit = (Button) findViewById(R.id.submit_button);
		submit.setText("¤×¹à§Ô¹");
		
		sellBillId = getIntent().getIntExtra("SELL_BILL_ID", 0);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.submit_button) {
			startConfirmReturnBillActivity();
			return;
		}
		super.onClick(v);
	}
	
	void startConfirmReturnBillActivity() {
		ArrayList<BillItem> billItemList = orderManager.getBillItems();
		
		Intent intent = new Intent(this, ConfirmReturnBillActivity.class);
		intent.putExtra("SELL_BILL_ID", sellBillId);
		intent.putParcelableArrayListExtra("BILL_ITEM_ARRAY", billItemList);
		startActivity(intent);
	}
}
