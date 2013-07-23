package com.manassorn.shopbox;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;

public class BillFolderListAdapter extends ArrayAdapter<BillFolder> {
	private Context context;
	
	public BillFolderListAdapter(Context context, List<BillFolder> list) {
		super(context, R.layout.bill_folder_list_item, list);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.bill_folder_list_item, parent, false);
		TextView billGroupName = (TextView) rowView.findViewById(R.id.bill_folder_name);
		TextView billGroupCount = (TextView) rowView.findViewById(R.id.bill_folder_count);
		
		BillFolder billFolder = getItem(position);
		billGroupName.setText(billFolder.getLabel());
		billGroupCount.setText("" + billFolder.getCount());
		rowView.setTag(billFolder.getValue());
		return rowView;
	}

	public static class BillFolder {
		private String label;
		private int count;
		private Object value;
		public BillFolder() {
		}
		public BillFolder(String label, int count) {
			this.label = label;
			this.count = count;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
}
