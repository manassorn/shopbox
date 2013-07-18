package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.manassorn.shopbox.db.SupplementDbAdapter;
import com.manassorn.shopbox.value.Supplement;

public class SupplementDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {
	private static final String TAG = SupplementDialogFragment.class.getSimpleName();
	private OnClickListener listener;
	private AlertDialog alertDialog;
	private SupplementDbAdapter dbAdapter;
	private Supplement[] supplements;
	private String[] supplementLabels = new String[0];
	List<Supplement> disabledSupplements;

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

	@Override
	public void onDetach() {
		super.onDetach();
		super.onDestroyView();
		if (dbAdapter != null) {
			dbAdapter.close();
		}
	}

	public interface OnClickListener {
		public void onDiscountDialogClick(DialogFragment dialog, Supplement discount);
	}

	@Override
	public void onClick(DialogInterface dialog, int index) {
		if (listener != null) {
			listener.onDiscountDialogClick(SupplementDialogFragment.this, supplements[index]);
		}
	}
	
	protected SupplementDbAdapter getDbAdapter() {
		return dbAdapter;
	}

	private void initData() {
		dbAdapter = new SupplementDbAdapter(getActivity());
		dbAdapter.open();
		Cursor cursor = querySupplement();
		supplements = SupplementDbAdapter.cursorToSupplement(cursor);
		supplementLabels = new String[supplements.length];
		for (int i = 0; i < supplements.length; i++) {
			Supplement supplement = supplements[i];
			supplementLabels[i] = supplement.getName() + " " + supplement.formatValue();
		}
	}

	protected Cursor querySupplement() {
		return dbAdapter.querySupplement();
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
		private Supplement[] supplements;

		public SupplementDialogAdapter(Context context, String[] labels, Supplement[] supplements,
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
			return !disabledSupplements.contains(supplements[position]);
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

	}
}
