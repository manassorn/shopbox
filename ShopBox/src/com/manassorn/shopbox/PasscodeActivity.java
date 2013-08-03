package com.manassorn.shopbox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

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

	public static class InterceptBackPressedLayout extends FrameLayout {

		public InterceptBackPressedLayout(Context context) {
			super(context);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			setLayoutParams(lp);
		}

		@Override
		public boolean dispatchKeyEventPreIme(KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				KeyEvent.DispatcherState state = getKeyDispatcherState();
				if(state != null) {
					if(event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
						state.startTracking(event, this);
						return true;
					} else if(event.getAction() == KeyEvent.ACTION_UP && !event.isCanceled() && state.isTracking(event)) {
						((Activity) getContext()).onBackPressed();
						return true;
					}
				}
			}
			return super.dispatchKeyEventPreIme(event);
		}
		
		
	}
}
