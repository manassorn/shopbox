package com.manassorn.shopbox;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.manassorn.shopbox.R;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSubTotalItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Supplement;

public class EditOrderAdapter extends ArrayAdapter<BillItem> {
	private static final String TAG = EditOrderAdapter.class.getSimpleName();
	private Context context;
	private List<BillItem> billItems;
	private OnCheckBoxClickedListener checkBoxListener;
	private OnProductAmountChangedListener productAmountListener;
	
	public EditOrderAdapter(Context context, List<BillItem> billItems) {
		super(context, R.layout.edit_bill_product_item, billItems);
		this.context = context;
		this.billItems = billItems;
	}
	
	public void setOnProductAmountChangedListener(OnProductAmountChangedListener listener) {
		productAmountListener = listener;
	}
	
	public void setOnCheckBoxClickedListener(OnCheckBoxClickedListener listener) {
		checkBoxListener = listener;
	}

	@Override
	public int getCount() {
		return billItems.size();
	}

	@Override
	public BillItem getItem(int position) {
		return billItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "GetView position=" + position);
		BillItem billItem = getItem(position);
		if(billItem instanceof BillProductItem) {
			return getProductView(position, convertView, parent);
		} else if(billItem instanceof BillSupplementItem) {
			return getSupplementView(position, convertView, parent);
		} else if(billItem instanceof BillSubTotalItem) {
			return getSubTotalView(position, convertView, parent);
		}
		return null;
	}
	
	public View getProductView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.edit_bill_product_item, parent, false);
		TextView productName = (TextView) rowView.findViewById(R.id.product_name);
		EditText productAmount = (EditText) rowView.findViewById(R.id.product_amount);
		TextView productTotal = (TextView) rowView.findViewById(R.id.product_total);
		
		BillProductItem billProductItem = (BillProductItem) getItem(position);
		productName.setText(billProductItem.getProductName());
		productAmount.setText(String.format("%d", billProductItem.getAmount()));
		double total = billProductItem.getAmount() * billProductItem.getProductPrice();
		productTotal.setText(String.format("%.2f", total));

		final int pos = position;
		CheckBox cb = (CheckBox) rowView.findViewById(R.id.checkbox);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CheckBox checkBox = (CheckBox) v;
				checkBoxListener.onCheckBoxClicked(checkBox, pos);
			}
		});
		productAmount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					EditText editText = (EditText) v;
					productAmountListener.onProductAmountChanged(editText, pos);
				}
			}
		});
		return rowView;
	}
	
	public View getSupplementView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.edit_bill_supplement_item, parent, false);
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
		
		final int pos = position;
		final CheckBox cb = (CheckBox) rowView.findViewById(R.id.checkbox);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkBoxListener.onCheckBoxClicked(cb, pos);
			}
		});
		return rowView;
	}
	
	public View getSubTotalView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.edit_bill_subtotal_item, parent, false);
		TextView subTotal = (TextView) rowView.findViewById(R.id.subtotal);
		
		BillSubTotalItem billSubTotalItem = (BillSubTotalItem) getItem(position);
		subTotal.setText(String.format("%.2f", billSubTotalItem.getSubTotal()));
		
		return rowView;
	}
	
	public static interface OnCheckBoxClickedListener {
		public void onCheckBoxClicked(CheckBox checkBox, int position);
	}
	
	public static interface OnProductAmountChangedListener {
		public void onProductAmountChanged(EditText editText, int position);
	}
}
