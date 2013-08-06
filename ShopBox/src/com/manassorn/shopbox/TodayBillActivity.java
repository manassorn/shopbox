package com.manassorn.shopbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.manassorn.shopbox.db.BillDao;
import com.manassorn.shopbox.db.Dao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.utils.DateUtils;

public class TodayBillActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_today_bill);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

		DbHelper dbHelper = DbHelper.getHelper(this);
		Cursor cursor = SellBillDao.getInstance(dbHelper).queryForToday();
		String[] from = new String[] { BillDao.ID, BillDao.TOTAL, BillDao.CREATED_TIME };
		int[] to = new int[] { R.id.bill_id, R.id.bill_total, R.id.bill_created_time };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.today_bill_list_item,
				cursor, from, to, 0);
		adapter.setViewBinder(new MyViewBinder());
		ListView billListView = (ListView) findViewById(R.id.today_bill_list);
		billListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.today_bill, menu);
		return true;
	}

	private final class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			String columnName = cursor.getColumnName(columnIndex);
			if ("Id".equals(columnName)) {
				String productId = cursor.getString(columnIndex);
				((TextView) view).setText("#" + productId);
				return true;
			} else if ("Total".equals(columnName)) {
				String price = cursor.getString(columnIndex);
				((TextView) view).setText("ß" + price);
				return true;
			} else if ("CreatedTime".equals(columnName)) {
				try {
					Date date = new SimpleDateFormat(Dao.DATETIME_FORMAT).parse(
							cursor.getString(columnIndex));
					String friendlyDate = DateUtils.formatFriendly(TodayBillActivity.this, date);
					((TextView) view).setText(friendlyDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}

	}

}
