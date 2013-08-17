package com.manassorn.shopbox;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.manassorn.shopbox.value.CashTransactionReport.TransactionType;

public class CashMissingFragment extends Fragment implements OnEditorActionListener,
		OnClickListener, TextWatcher {
	protected static final int COUNT_EMPTY = -1;
	protected double currentCash;
	protected double countAmount;
	protected double missingAmount;
	protected double cashRemain;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_cash_missing, null);

		EditText missingAmount = (EditText) root.findViewById(R.id.count_amount);
		missingAmount.setOnEditorActionListener(this);
		missingAmount.addTextChangedListener(this);

		Button ok = (Button) root.findViewById(R.id.ok_button);
		ok.setOnClickListener(this);
		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateCurrentCashAmount();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				|| (actionId == EditorInfo.IME_ACTION_DONE)) {
			ok();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		ok();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable countAmountEditable) {
		String strCountAmount = countAmountEditable.toString();
		countAmount = strCountAmount.length() > 0 ? Double.parseDouble(strCountAmount)
				: COUNT_EMPTY;

		updateMissingAmount();
	}

	protected void updateMissingAmount() {
		TextView missingAmountText = (TextView) getView().findViewById(R.id.missing_amount);
		TextView missingAmountLabelText = (TextView) getView().findViewById(
				R.id.missing_amount_label);
		if (countAmount == COUNT_EMPTY) {
			missingAmountText.setVisibility(View.INVISIBLE);
			missingAmountLabelText.setVisibility(View.INVISIBLE);
		} else {
			missingAmountText.setVisibility(View.VISIBLE);
			missingAmountLabelText.setVisibility(View.VISIBLE);
			missingAmount = countAmount - currentCash;
			missingAmountLabelText.setText(getMissingLabel(missingAmount));
			missingAmountText.setText(String.format(" ฿%,.2f", Math.abs(missingAmount)));
			if(missingAmount == 0) {
				missingAmountText.setVisibility(View.INVISIBLE);
			}
		}
	}

	protected String getMissingLabel(double missingAmount) {
		if(missingAmount < 0) {
			return "หายไป";
		} else if(missingAmount > 0) {
			return "เกินมา";
		}
		return "พอดี";
	}

	protected void updateCurrentCashAmount() {
		currentCash = CashManager.getCurrentCash(getActivity());
		TextView currentCashAmountText = (TextView) getView().findViewById(R.id.current_cash);
		currentCashAmountText.setText(String.format("฿%,.2f", currentCash));
	}

	protected void ok() {
		if (validateInput()) {
			startConfirmCashReport();
		}
	}

	protected void startConfirmCashReport() {
		cashRemain = countAmount;
		Intent intent = new Intent(getActivity(), ConfirmCashReportActivity.class);
		intent.putExtra("TRANSACTION_TYPE", TransactionType.MISSING.name());
		intent.putExtra("CURRENT_CASH", currentCash);
		intent.putExtra("AMOUNT", missingAmount);
		intent.putExtra("CASH_REMAIN", cashRemain);
		startActivity(intent);
	}

	protected boolean validateInput() {
		return checkCountAmount();
	}

	protected boolean checkCountAmount() {
		EditText countAmountText = (EditText) getView().findViewById(R.id.count_amount);
		String strCountAmount = countAmountText.getText().toString();
		if (strCountAmount.length() == 0) {
			Toast.makeText(getActivity(), R.string.empty_receive_money_input, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}
}
