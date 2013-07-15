package com.manassorn.shopbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.manassorn.shopbox.BillGroupListAdapter.BillGroup;

public class BillGroupListAdapter extends ArrayAdapter<BillGroup> {
	private Context context;
	
	public BillGroupListAdapter(Context context, List<BillGroup> list) {
		super(context, R.layout.bill_group_list_item, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.bill_group_list_item, parent, false);
		TextView billGroupName = (TextView) rowView.findViewById(R.id.bill_group_name);
		TextView billGroupCount = (TextView) rowView.findViewById(R.id.bill_group_count);
		
		BillGroup billGroup = getItem(position);
		billGroupName.setText(billGroup.getName());
		billGroupCount.setText(billGroup.getCount());
		return rowView;
	}

	public static class BillGroup {
		private String name;
		private int count;
		public BillGroup(String name, int count) {
			super();
			this.name = name;
			this.count = count;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
	}
}
