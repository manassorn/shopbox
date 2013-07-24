package com.manassorn.shopbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.db.Dao;
import com.manassorn.shopbox.utils.DateUtils;
import com.manassorn.shopbox.value.Bill;

public class BillListAdapter extends ArrayAdapter<Bill> {
	Context context;

	public BillListAdapter(Context context, List<Bill> data) {
		super(context, R.layout.today_bill_list_item, data);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.today_bill_list_item, parent, false);
		TextView idText = (TextView) rowView.findViewById(R.id.bill_id);
		TextView totalText = (TextView) rowView.findViewById(R.id.bill_total);
		TextView createdTimeText = (TextView) rowView.findViewById(R.id.bill_created_time);
		
		Bill bill = getItem(position);
		idText.setText("#" + bill.getId());
		totalText.setText("ß" + bill.getTotal());
		String friendlyDate = DateUtils.formatFriendly(context, bill.getCreatedTime());
		createdTimeText.setText(friendlyDate);
		rowView.setTag(bill.getId());
		return rowView;
	}

}
