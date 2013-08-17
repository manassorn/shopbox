package com.manassorn.shopbox;

import java.sql.SQLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.manassorn.shopbox.db.CashTransactionReportDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.value.CashTransactionReport;
import com.manassorn.shopbox.value.CashTransactionReport.TransactionType;

public class ConfirmCashReportActivity extends Activity implements OnClickListener {
	private static final String TAG = "ConfirmCashReportActivity";
	private TransactionType transactionType;
	private double currentCash;
	private double amount;
	private double cashRemain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_cash_report);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		initParameters();
		initViews();

		Button ok = (Button) findViewById(R.id.ok_button);
		ok.setOnClickListener(this);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		onBackPressed();
		return true;
	}

	@Override
	public void onClick(View v) {
		ok();
	}
	
	protected void initParameters() {
		transactionType = TransactionType.valueOf(getIntent().getStringExtra("TRANSACTION_TYPE"));
		currentCash = getIntent().getDoubleExtra("CURRENT_CASH", 0);
		amount = getIntent().getDoubleExtra("AMOUNT", 0);
		cashRemain = getIntent().getDoubleExtra("CASH_REMAIN", 0);
	}
	
	protected void initViews() {
		TextView footerCashRemainText = (TextView) findViewById(R.id.footer_cash_remain);
		TextView cashRemainText = (TextView) findViewById(R.id.cash_remain);
		String strCashRemain = String.format("฿%,.2f", cashRemain);
		footerCashRemainText.setText(strCashRemain);
		cashRemainText.setText(strCashRemain);
		
		TextView cashAmountText = (TextView) findViewById(R.id.amount);
		cashAmountText.setText(String.format("฿%,.2f", Math.abs(amount)));
		
		TextView currentCashText = (TextView) findViewById(R.id.current_cash);
		currentCashText.setText(String.format("฿%,.2f", currentCash));
		
		TextView transferTypeText = (TextView) findViewById(R.id.transfer_type);
		transferTypeText.setText(getTransactionLabel(amount));
		
		TextView transactionTypeText = (TextView) findViewById(R.id.transaction_type);
		transactionTypeText.setText(getTransactionTypeLabel(transactionType));
	}
	
	protected String getTransactionTypeLabel(TransactionType transactionType) {
		if(transactionType == TransactionType.TRANSFER) {
			return "ใบโอนเงิน";
		} else if(transactionType == TransactionType.MISSING) {
			return "ใบนับเงิน";
		}
		// shouldn't call this line
		return "";
	}
	
	protected String getTransactionLabel(double amount) {
		if(transactionType == TransactionType.TRANSFER) {
			if(amount < 0) {
				return "โอนออก";
			} else if(amount >0) {
				return "โอนเข้า";
			}
			// shouldn't call this line
			return "";
		} else {
			if(amount < 0) {
				return "เงินหาย";
			} else if(amount >0) {
				return "เงินเกิน";
			} else {
				return "เงินพอดี";
			}
		}
	}

	protected void ok() {
		try {
			insertCashReport();
			success();
		} catch (SQLException e) {
			Toast.makeText(this, "ไม่สำเร็จ", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Database Error", e);
		}
	}
	
	protected int insertCashReport() throws SQLException {
		CashTransactionReport cashTransactionReport = new CashTransactionReport(transactionType, amount);
		DbHelper dbHelper = DbHelper.getHelper(this);
		CashTransactionReportDao cashTransactionReportDao = CashTransactionReportDao.getInstance(dbHelper);
		return cashTransactionReportDao.insert(cashTransactionReport);
	}
	
	protected void success() {
		new AlertDialog.Builder(this).setMessage("บันทึกรายการเรียบร้อย")
		.setNeutralButton("ตกลง",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				startMainActivityClearTop();
			}
		}).show();
	}

	// TODO - move to parent class
	protected void startMainActivityClearTop() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
