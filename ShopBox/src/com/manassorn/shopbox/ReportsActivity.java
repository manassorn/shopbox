package com.manassorn.shopbox;

import java.util.Date;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.manassorn.shopbox.SelectBillByDateFragment.OnSelectBillByDate;
import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.SellBillDao;

public class ReportsActivity extends Activity implements OnSelectBillByDate {
	SelectBillByDateFragment selectBillByDateFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getIntent().putExtra("BILL_DAO_CLASS", SellBillDao.class);

		// TODO - manage this selectBillByDateFragment
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// ft.add(selectBillByDateFragment(), "TAG").commit();
		ft.replace(R.id.frame, selectBillByDateFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO - display text 'Today' at menu item 
		getMenuInflater().inflate(R.menu.reports, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_today:
				selectBillByDateFragment().selectDate(new Date());
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (!selectBillByDateFragment().getChildFragmentManager().popBackStackImmediate()) {
			super.onBackPressed();
		}
	}

	SelectBillByDateFragment selectBillByDateFragment() {
		if (selectBillByDateFragment == null) {
			selectBillByDateFragment = new SelectBillByDateFragment();
		}
		return selectBillByDateFragment;
	}

	@Override
	public void onSelectBillId(BillDao dao, int billId) {
		startViewBillActivity(billId);
	}

	protected void startViewBillActivity(int billId) {
		Intent intent = new Intent(this, ViewBillActivity.class);
		intent.putExtra("BILL_ID", billId);
		startActivity(intent);
	}
}
