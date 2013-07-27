package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSubTotalItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Supplement;

public class ConfirmBillActivity extends Activity implements OnClickListener {
	ArrayList<BillItem> billItems;
	double total;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_bill);

		// action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// bill
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			billItems = bundle.getParcelableArrayList("BILL_ITEM_ARRAY");
		}
		ListView listView = (ListView) findViewById(R.id.bill_item_list);
		listView.setAdapter(new BillArrayAdapter(this, billItems));

		BillSubTotalItem totalItem = (BillSubTotalItem) billItems.get(billItems.size() - 1);
		total = totalItem.getSubTotal();
		setTotalView(total);

		Button submitButton = (Button) findViewById(R.id.submit_button);
		submitButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.billing, menu);
		return true;
	}

	public static class BillArrayAdapter extends ArrayAdapter<BillItem> {
		private Context context;
		protected int productItemResId = R.layout.confirm_bill_product_item;
		protected int supplementItemResId = R.layout.confirm_bill_supplement_item;
		protected int subTotalItemResId = R.layout.confirm_bill_subtotal_item;

		public BillArrayAdapter(Context context, List<BillItem> items) {
			super(context, R.layout.confirm_bill_product_item, items);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			BillItem billItem = getItem(position);
			if (billItem instanceof BillProductItem) {
				return getProductView(position, convertView, parent);
			} else if (billItem instanceof BillSupplementItem) {
				return getSupplementView(position, convertView, parent);
			} else if (billItem instanceof BillSubTotalItem) {
				return getSubTotalView(position, convertView, parent);
			}
			return null;
		}

		protected View getProductView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(productItemResId, parent, false);
			TextView productName = (TextView) rowView.findViewById(R.id.product_name);
			TextView productAmount = (TextView) rowView.findViewById(R.id.product_amount);
			TextView productTotal = (TextView) rowView.findViewById(R.id.product_total);

			BillProductItem billProductItem = (BillProductItem) getItem(position);
			productName.setText(billProductItem.getProductName());
			productAmount.setText(String.format("%d", billProductItem.getAmount()));
			double total = billProductItem.getAmount() * billProductItem.getProductPrice();
			productTotal.setText(String.format("%.2f", total));
			return rowView;
		}

		protected View getSupplementView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(supplementItemResId, parent, false);
			TextView supplementName = (TextView) rowView.findViewById(R.id.supplement_name);
			TextView supplementValue = (TextView) rowView.findViewById(R.id.supplement_value);
			TextView supplementTotal = (TextView) rowView.findViewById(R.id.supplement_total);

			BillSupplementItem billSupplementItem = (BillSupplementItem) getItem(position);
			supplementName.setText(billSupplementItem.getSupplementName());
			String formatValue = Supplement.formatValue(billSupplementItem.getSupplementType(),
					billSupplementItem.getSupplementPercent(),
					billSupplementItem.getSupplementConstant());
			supplementValue.setText(formatValue);
			supplementTotal.setText(String.format("%.2f", billSupplementItem.getTotal()));

			return rowView;
		}

		protected View getSubTotalView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(subTotalItemResId, parent, false);
			TextView subTotal = (TextView) rowView.findViewById(R.id.subtotal);

			BillSubTotalItem billSubTotalItem = (BillSubTotalItem) getItem(position);
			subTotal.setText(String.format("%.2f", billSubTotalItem.getSubTotal()));

			return rowView;
		}
	}

	protected void setTotalView(double total) {
		TextView totalView = (TextView) findViewById(R.id.total);
		totalView.setText(String.format("ß%,.2f", total));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.submit_button:
				startReceiveMoneyActivity();
				break;
		}

	}

	protected void startReceiveMoneyActivity() {
		Intent intent = new Intent(this, ReceiveMoneyActivity.class);
		intent.putParcelableArrayListExtra("BILL_ITEM_ARRAY", billItems);
		intent.putExtra("TOTAL", total);
		startActivity(intent);
	}
}
