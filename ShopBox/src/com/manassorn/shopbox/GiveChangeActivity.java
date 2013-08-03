package com.manassorn.shopbox;

import com.manassorn.shopbox.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GiveChangeActivity extends Activity implements OnClickListener {
	private int billId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_give_change);

		// action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Button complete = (Button) findViewById(R.id.complete_button);
		complete.setOnClickListener(this);

		Button viewBill = (Button) findViewById(R.id.view_bill_button);
		viewBill.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			billId = bundle.getInt("BILL_ID");
			double receiveMoney = bundle.getDouble("RECEIVE_MONEY");
			double total = bundle.getDouble("TOTAL");

			TextView receiveMoneyText = (TextView) findViewById(R.id.receive_money);
			TextView totalText = (TextView) findViewById(R.id.total);
			TextView changeText = (TextView) findViewById(R.id.change);

			receiveMoneyText.setText(formatPrice(receiveMoney));
			totalText.setText(formatPrice(total));
			changeText.setText(formatPrice(receiveMoney - total));
		}
	}

	@Override
	public void onBackPressed() {
		startMainActivityClearTop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		intent.putExtra("BILL_ID", billId);
		startActivity(intent);
	}

	private String formatPrice(double price) {
		return String.format("ß%,.2f", price);
	}
}
