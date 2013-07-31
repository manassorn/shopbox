package com.manassorn.shopbox;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasscodeActivity extends Activity implements TextWatcher {
	EditText[] passcodeDigits = new EditText[4];
	EditText passcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passcode);

		passcodeDigits[0] = (EditText) findViewById(R.id.digit1);
		passcodeDigits[1] = (EditText) findViewById(R.id.digit2);
		passcodeDigits[2] = (EditText) findViewById(R.id.digit3);
		passcodeDigits[3] = (EditText) findViewById(R.id.digit4);

		passcode = (EditText) findViewById(R.id.passcode);
		passcode.addTextChangedListener(this);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.stay_still, R.anim.slide_down_out);
	}

	boolean verifyPasscode(String text) {
		//TODO - 
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		String passcode = prefs.getString("PASSCODE", "A");
//		return text.equals(passcode);
		
		return text.equals("1234");
	}

	void checkPasscode(String text) {
		if (verifyPasscode(text)) {
			findViewById(R.id.incorrect_passcode).setVisibility(View.GONE);
			findViewById(R.id.input_passcode).setVisibility(View.VISIBLE);
			setResult(RESULT_OK);
			finish();
			overridePendingTransition(R.anim.stay_still, R.anim.slide_down_out);
		} else {
			findViewById(R.id.incorrect_passcode).setVisibility(View.VISIBLE);
			findViewById(R.id.input_passcode).setVisibility(View.GONE);

			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);
		}
	}

	@Override
	public void afterTextChanged(Editable editable) {
		String text = editable.toString();
		for (int i = 0; i < passcodeDigits.length; i++) {
			String dummyText = i < text.length() ? "*" : "";
			passcodeDigits[i].setText(dummyText);
		}
		if (text.length() == passcodeDigits.length) {
			checkPasscode(text);
			passcode.setText("");
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

}
