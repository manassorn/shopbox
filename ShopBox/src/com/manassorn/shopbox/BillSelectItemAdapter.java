package com.manassorn.shopbox;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.manassorn.shopbox.ConfirmBillActivity.BillArrayAdapter;
import com.manassorn.shopbox.value.BillItem;

public class BillSelectItemAdapter extends BillArrayAdapter {
	protected OnCheckBoxClickedListener checkBoxListener;

	public BillSelectItemAdapter(Context context, List<BillItem> items) {
		super(context, items);
		productItemResId = R.layout.return_bill_product_item;
	}

	@Override
	protected View getProductView(int position, View convertView, ViewGroup parent) {
		View rowView = super.getProductView(position, convertView, parent);
		if(checkBoxListener == null) return rowView;
		
		final int pos = position;
		CheckBox cb = (CheckBox) rowView.findViewById(R.id.checkbox);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox checkBox = (CheckBox) v;
				checkBoxListener.onCheckBoxClicked(checkBox, pos);
			}
		});
		return rowView;
	}
	
	public void setOnCheckBoxClickedListener(OnCheckBoxClickedListener listener) {
		checkBoxListener = listener;
	}
	
	public static interface OnCheckBoxClickedListener {
		public void onCheckBoxClicked(CheckBox checkBox, int position);
	}
}
