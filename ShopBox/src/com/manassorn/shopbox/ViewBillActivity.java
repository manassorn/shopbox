package com.manassorn.shopbox;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manassorn.shopbox.ConfirmBillActivity.BillArrayAdapter;
import com.manassorn.shopbox.db.BillItemDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;
import com.manassorn.shopbox.db.ShopAttributesDao;
import com.manassorn.shopbox.value.Bill;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.ShopAttributes;

public class ViewBillActivity extends Activity {
	public static final String TAG = "ViewBillActivity";
	private Bill bill;
	private List<BillItem> billItems;
	private ShopAttributes shopAttributes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_bill);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		try {
			initParameters();
			initViews();
		} catch (SQLException e) {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_bill, menu);
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
	
	protected void initParameters() throws SQLException {
		int billId = getIntent().getIntExtra("BILL_ID", -1);
		if(billId == -1) {
			throw new RuntimeException("ไม่มีเลขที่ใบเสร็จ");
		}
		
		DbHelper dbHelper = DbHelper.getHelper(this);
		SellBillDao billDao = SellBillDao.getInstance(dbHelper);
		BillItemDao billItemDao = BillItemDao.getInstance(dbHelper);
		ShopAttributesDao shopAttributesDao = ShopAttributesDao.getInstance(dbHelper);
		try {
			bill = billDao.getForId(billId);
			billItems = billItemDao.getForEq(BillItemDao.BILL_ID, billId);
			shopAttributes = shopAttributesDao.getForId(bill.getShopAttributesId());
		} catch (SQLException e) {
			Toast.makeText(this, "เรียกดูไม่ได้ ลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Database Error", e);
			throw e;
		}
	}

	protected void initViews() {
		findViewById(R.id.submit_button).setVisibility(View.GONE);
		
		TextView totalText = (TextView) findViewById(R.id.total);
		totalText.setText(String.format("฿%,.2f", bill.getTotal()));

		ListView listView = (ListView) findViewById(R.id.bill_item_list);
		listView.setAdapter(new BillArrayAdapter(this, billItems));
		
		TextView shopName = (TextView) findViewById(R.id.shop_name);
		shopName.setText(shopAttributes.getShopName());
		// TODO - init shop branch
		TextView taxId= (TextView) findViewById(R.id.shop_tax_id);
		taxId.setText(shopAttributes.getTaxId());
	}
}
