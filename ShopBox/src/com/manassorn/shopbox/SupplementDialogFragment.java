package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.manassorn.shopbox.db.Dao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.value.Supplement;

public class SupplementDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {
	private static final String TAG = SupplementDialogFragment.class.getSimpleName();
	private OnClickListener listener;
	private AlertDialog alertDialog;
	private List<Supplement> supplements;
	private String[] supplementLabels = new String[0];
	protected List<Supplement> disabledSupplements;
	private Dao<Supplement, Integer> dao;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnClickListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
		initData();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		List<Supplement> selectedSupplements = getArguments().getParcelableArrayList(
				"selectedSupplements");
		if (alertDialog == null || !equalsList(disabledSupplements, selectedSupplements)) {
			disabledSupplements = selectedSupplements;

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("เลือกส่วนลด").setAdapter(
					new SupplementDialogAdapter(getActivity(), supplementLabels, supplements,
							disabledSupplements), this);
			alertDialog = builder.create();
		}

		return alertDialog;
	}

	public interface OnClickListener {
		public void onDiscountDialogClick(DialogFragment dialog, Supplement discount);
	}

	@Override
	public void onClick(DialogInterface dialog, int index) {
		if (listener != null) {
			listener.onDiscountDialogClick(SupplementDialogFragment.this, supplements.get(index));
		}
	}
	
	protected Dao<Supplement, Integer> getDao() {
		if(dao == null) {
			dao = DbHelper.getHelper(getActivity()).getSupplementDao();
		}
		return dao;
	}

	private void initData() {
		supplements = getSupplements();
		supplementLabels = new String[supplements.size()];
		for (int i = 0; i < supplements.size(); i++) {
			Supplement supplement = supplements.get(i);
			supplementLabels[i] = supplement.getName() + " " + supplement.formatValue();
		}
	}

	protected List<Supplement> getSupplements() {
		return getDao().getForAll();
	}

	private boolean equalsList(List list1, List list2) {
		if (list1.size() != list2.size())
			return false;
		List common = new ArrayList(list1);
		common.retainAll(list2);
		return common.size() == list2.size();
	}

	static class SupplementDialogAdapter extends ArrayAdapter<String> {
		private List<Supplement> disabledSupplements;
		private List<Supplement> supplements;

		public SupplementDialogAdapter(Context context, String[] labels, List<Supplement> supplements,
				List<Supplement> disabledSupplements) {
			super(context, android.R.layout.simple_list_item_1, labels);
			this.supplements = supplements;
			this.disabledSupplements = disabledSupplements;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			view.setEnabled(isEnabled(position));
			return view;
		}

		@Override
		public boolean isEnabled(int position) {
			return !disabledSupplements.contains(supplements.get(position));
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

	}
}
