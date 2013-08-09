package com.manassorn.shopbox;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.manassorn.shopbox.utils.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class PasscodeActivity extends Activity implements TextWatcher, OnClickListener {
	EditText[] passcodeDigits = new EditText[4];
	EditText passcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InterceptBackPressedLayout root = new InterceptBackPressedLayout(this);
		getLayoutInflater().inflate(R.layout.activity_passcode, root, true);
		setContentView(root);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int managerViolet = getResources().getColor(R.color.manager_violet);
		getActionBar().setBackgroundDrawable(new ColorDrawable(managerViolet));

		passcodeDigits[0] = (EditText) findViewById(R.id.digit1);
		passcodeDigits[1] = (EditText) findViewById(R.id.digit2);
		passcodeDigits[2] = (EditText) findViewById(R.id.digit3);
		passcodeDigits[3] = (EditText) findViewById(R.id.digit4);

		passcode = (EditText) findViewById(R.id.passcode);
		passcode.addTextChangedListener(this);
		
		for(EditText passcodeDigit : passcodeDigits) {
			passcodeDigit.setOnClickListener(this);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// sometime show, sometime not
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(passcode, InputMethodManager.SHOW_IMPLICIT);
			}
		}, 200);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.stay_still, R.anim.slide_down_out);
	}

	@Override
	public void onClick(View v) {
		// passcode.requestFocus(); doesn't work
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(passcode, InputMethodManager.SHOW_IMPLICIT);
	}

	void checkPasscode(String text) {
		if (Utils.verifyPasscode(this, text)) {
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
