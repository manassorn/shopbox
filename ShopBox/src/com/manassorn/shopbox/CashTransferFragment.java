package com.manassorn.shopbox;

import com.manassorn.shopbox.value.CashTransactionReport.TransactionType;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class CashTransferFragment extends Fragment implements OnEditorActionListener,
		OnClickListener {
	protected double currentCash;
	protected int checkedId;
	protected double transferAmount;
	protected double cashRemain;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_cash_transfer, null);
		
		EditText transferAmount = (EditText) root.findViewById(R.id.transfer_amount);
		transferAmount.setOnEditorActionListener(this);

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

	protected void updateCurrentCashAmount() {
		currentCash = CashManager.getCurrentCash(getActivity());
		TextView currentCashAmountText = (TextView) getView().findViewById(R.id.current_cash);
		currentCashAmountText.setText(String.format("ß%,.2f", currentCash));
	}

	protected void ok() {
		if (validateInput()) {
			startConfirmCashReport();
		}
	}
	
	protected void startConfirmCashReport() {
		cashRemain = currentCash + transferAmount;
		Intent intent = new Intent(getActivity(), ConfirmCashReportActivity.class);
		intent.putExtra("TRANSACTION_TYPE", TransactionType.TRANSFER.name());
		intent.putExtra("CURRENT_CASH", currentCash);
		intent.putExtra("AMOUNT", transferAmount);
		intent.putExtra("CASH_REMAIN", cashRemain);
		startActivity(intent);
	}

	protected boolean validateInput() {
		return checkTransactionType() && checkTransferAmount();
	}

	protected boolean checkTransactionType() {
		RadioGroup cashChoice = (RadioGroup) getView().findViewById(R.id.cash_choice);
		checkedId = cashChoice.getCheckedRadioButtonId();
		if (checkedId == -1) {
			Toast.makeText(getActivity(), "½Ò¡ËÃ×Í¶Í¹?", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	protected boolean checkTransferAmount() {
		EditText transferAmountText = (EditText) getView().findViewById(R.id.transfer_amount);
		String strTransferAmount = transferAmountText.getText().toString();
		if (strTransferAmount.length() == 0) {
			Toast.makeText(getActivity(), R.string.empty_receive_money_input, Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		transferAmount = Double.parseDouble(strTransferAmount);
		if(transferAmount == 0) {
			// TODO - change message
			Toast.makeText(getActivity(), R.string.empty_receive_money_input,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkedId == R.id.radio_withdraw) {
			if (transferAmount > currentCash) {
				Toast.makeText(getActivity(), R.string.receive_money_less_than_total,
						Toast.LENGTH_SHORT).show();
				return false;
			}
			transferAmount = -transferAmount;
		}
		return true;
	}
}
