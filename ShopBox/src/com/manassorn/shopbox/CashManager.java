package com.manassorn.shopbox;

import android.content.Context;

import com.manassorn.shopbox.db.CashTransactionReportDao;
import com.manassorn.shopbox.db.DbHelper;
import com.manassorn.shopbox.db.SellBillDao;

public class CashManager {

	public static double getCurrentCash(Context context) {
		DbHelper dbHelper = DbHelper.getHelper(context);
		SellBillDao sellBillDao = SellBillDao.getInstance(dbHelper);
		CashTransactionReportDao cashTransactionReportDao = CashTransactionReportDao.getInstance(dbHelper);
		
		return sellBillDao.getSumTotal() + cashTransactionReportDao.getSumAmount();
	}
}
