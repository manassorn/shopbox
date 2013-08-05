package com.manassorn.shopbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.TextView;

import com.manassorn.shopbox.utils.KeyboardUtil;

public class ChangePasscodeActivity extends Activity implements OnClickListener {
	EditText hiddenPasscode;
	EditText hiddenNewPasscode;
	EditText hiddenConfirmPasscode;
	TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InterceptBackPressedLayout root = new InterceptBackPressedLayout(this);
		getLayoutInflater().inflate(R.layout.activity_change_passcode, root, true);
		setContentView(root);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		int managerViolet = getResources().getColor(R.color.manager_violet);
		getActionBar().setBackgroundDrawable(new ColorDrawable(managerViolet));

		status = (TextView) findViewById(R.id.status);
		status.bringToFront();
		hiddenPasscode = (EditText) findViewById(R.id.hidden_passcode);
		hiddenNewPasscode = (EditText) findViewById(R.id.hidden_new_passcode);
		hiddenConfirmPasscode = (EditText) findViewById(R.id.hidden_confirm_passcode);

		initDummyText(hiddenPasscode, R.id.passcode_dummy);
		initDummyText(hiddenNewPasscode, R.id.new_passcode_dummy);
		initDummyText(hiddenConfirmPasscode, R.id.confirm_passcode_dummy);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				KeyboardUtil.showSoftInput(ChangePasscodeActivity.this, getCurrentFocus());
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
//		// passcode.requestFocus(); doesn't work
		KeyboardUtil.showSoftInput(this, getCurrentFocus());
	}
	
	void initDummyText(EditText hiddenText, int dummyResId) {
		ViewGroup dummyGroup = (ViewGroup) findViewById(dummyResId);
		EditText[] dummyTexts = findTextDummy(dummyGroup);
		hiddenText.addTextChangedListener(new PasscodeWatcher(dummyTexts));
		
		for(EditText editText : dummyTexts) {
			editText.setOnClickListener(this);
		}
	}
	
	EditText[] findTextDummy(ViewGroup viewGroup) {
		EditText[] editTexts = new EditText[4];
		editTexts[0] = (EditText) viewGroup.findViewById(R.id.first);
		editTexts[1] = (EditText) viewGroup.findViewById(R.id.second);
		editTexts[2] = (EditText) viewGroup.findViewById(R.id.third);
		editTexts[3] = (EditText) viewGroup.findViewById(R.id.forth);
		return editTexts;
	}
	
	void onPasscodeComplete(Editable editable) {
		if(editable == hiddenPasscode.getText()) {
			checkPasscode();
		} else if(editable == hiddenNewPasscode.getText()) {
			hiddenConfirmPasscode.requestFocus();
			scrollTo(R.id.confirm_passcode_label);
		} else if(editable == hiddenConfirmPasscode.getText()) {
			checkConfirmPasscode();
		}
	}

	void checkPasscode() {
		if (verifyPasscode(hiddenPasscode.getText().toString())) {
			status.setVisibility(View.GONE);
			hiddenNewPasscode.requestFocus();
			scrollTo(R.id.new_passcode_label);
		} else {
			status.setVisibility(View.VISIBLE);
			status.setText("รหัสไม่ถูกต้อง");
			
			hiddenPasscode.setText("");
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);
		}
	}

	boolean verifyPasscode(String text) {
		//TODO - 
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		String passcode = prefs.getString("PASSCODE", "A");
//		return text.equals(passcode);
		
		return text.equals("1234");
	}

	void checkConfirmPasscode() {
		String newPasscode = hiddenNewPasscode.getText().toString();
		String confirmPasscode = hiddenConfirmPasscode.getText().toString();
		if(newPasscode.equals(confirmPasscode)) {
			changePasscode(newPasscode);
			
			new AlertDialog.Builder(this).setMessage("เปลี่ยนรหัสผ่านเรียบร้อย")
			.setNeutralButton("ตกลง",  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					onBackPressed();
				}
			}).setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					onBackPressed();
				}
			}).show();
		} else {
			status.setVisibility(View.VISIBLE);
			status.setText("รหัสไม่ตรงกัน ลองใหม่อีกครั้ง");
			

			hiddenNewPasscode.setText("");
			hiddenConfirmPasscode.setText("");
			hiddenNewPasscode.requestFocus();
			scrollTo(R.id.new_passcode_label);
			
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(1000);
		}
	}
	
	void changePasscode(String newPasscode) {
		//TODO - change passcode
	}
	
	void scrollTo(int targetResId) {
		View targetView = findViewById(targetResId);
		View container = findViewById(R.id.container);
		container.animate().translationY(- targetView.getY());
	}

	class PasscodeWatcher implements TextWatcher {
		TextView[] dummyTexts;
		
		public PasscodeWatcher(TextView[] dummyTexts) {
			this.dummyTexts = dummyTexts;
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable editable) {
			String input = editable.toString();
			for (int i = 0; i < dummyTexts.length; i++) {
				String star = i < input.length() ? "*" : "";
				dummyTexts[i].setText(star);
			}
			if (input.length() > 0 && input.length() == dummyTexts.length) {
				onPasscodeComplete(editable);
			}
		}
	}
}
