package com.manassorn.shopbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ReturnMoneyActivity extends Activity implements OnClickListener {
	private int returnBillId;
	private double total;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return_money);

		Button complete = (Button) findViewById(R.id.complete_button);
		complete.setOnClickListener(this);

		Button viewBill = (Button) findViewById(R.id.view_bill_button);
		viewBill.setOnClickListener(this);
		
		Intent intent = getIntent();
		returnBillId = intent.getIntExtra("RETURN_BILL_ID", 0);
		total = intent.getDoubleExtra("TOTAL", 0);
		
		TextView returnMoney = (TextView) findViewById(R.id.return_money);
		returnMoney.setText(String.format("%,.2f", total));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.complete_button:
				startMainActivityClearTop();
				break;
			case R.id.view_bill_button:
				startViewBillActivity();
				break;
		}
	}

	protected void startMainActivityClearTop() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	protected void startViewBillActivity() {
		Intent intent = new Intent(this, ViewBillActivity.class);
		intent.putExtra("RETURN_BILL_ID", returnBillId);
		startActivity(intent);
	}

}
