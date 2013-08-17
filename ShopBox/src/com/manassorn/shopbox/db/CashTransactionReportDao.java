package com.manassorn.shopbox.db;

import android.database.Cursor;

import com.manassorn.shopbox.value.CashTransactionReport;

public class CashTransactionReportDao extends Dao<CashTransactionReport, Integer> {
	public static final String TYPE = "Type";
	public static final String AMOUNT = "Amount";
	static CashTransactionReportDao instance;

	public static CashTransactionReportDao getInstance(DbHelper dbHelper) {
		if (instance == null) {
			instance = new CashTransactionReportDao(dbHelper, CashTransactionReport.class);
		}
		return instance;
	}

	protected CashTransactionReportDao(DbHelper dbHelper, Class<CashTransactionReport> clazz) {
		super(dbHelper, clazz);
	}
	
	public double getSumAmount() {
		Cursor cursor = queryBuilder().select("SUM(" + AMOUNT + ")").query();
		if(cursor == null || !cursor.moveToFirst()) {
			return 0;
		}
		return cursor.getDouble(0);
	}
}
