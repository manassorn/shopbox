package com.manassorn.shopbox;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.ProductDao;

public class EnterBarcodeFragment extends Fragment implements TextWatcher, OnClickListener {
	private EditText searchView;
	private ListView listView;
	private String queryBarcode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_enter_barcode, container, false);

		searchView = (EditText) view.findViewById(R.id.search_barcode);
		searchView.addTextChangedListener(this);

		listView = (ListView) view.findViewById(R.id.product_list);

		Button cancel = (Button) view.findViewById(R.id.cancel_button);
		cancel.setOnClickListener(this);

		return view;
	}

	private void queryBarcode(String barcode) {
		queryBarcode = barcode;
		DbHelper dbHelper = DbHelper.getHelper(getActivity());
		Cursor cursor = ProductDao.getInstance(dbHelper).queryForBarcodeStartsWith(barcode);

		String[] from = new String[] { ProductDao.NAME, ProductDao.PRICE, ProductDao.BARCODE };

		int[] to = new int[] { R.id.product_name, R.id.product_price, R.id.product_barcode };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.product_barcode_list_item, cursor, from, to, 0);
		adapter.setViewBinder(new MyViewBinder());
		listView.setAdapter(adapter);
	}

	private final class MyViewBinder implements SimpleCursorAdapter.ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			String columnName = cursor.getColumnName(columnIndex);
			if ("Price".equals(columnName)) {
				String price = cursor.getString(columnIndex);
				((TextView) view).setText("ß" + price);
				return true;
			} else if ("Barcode".equals(columnName)) {
				String barcode = cursor.getString(columnIndex);
				int queryLen = queryBarcode.length();
				((TextView) view).setText(Html.fromHtml("<b>" + barcode.substring(0, queryLen)
						+ "</b>" + barcode.substring(queryLen)));
				return true;
			}
			return false;
		}

	}

	@Override
	public void afterTextChanged(Editable editable) {
		queryBarcode(editable.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.cancel_button:
				SelectProductActivity activity = (SelectProductActivity) getActivity();
				activity.openScanBarcode();
		}

	}
}
