package com.manassorn.shopbox.db;

import com.manassorn.shopbox.value.ReturnBill;

public class ReturnBillDao extends BillDao<ReturnBill> {
	public static final String SELL_BILL_ID = "SellBIllId";
	static ReturnBillDao instance;

	public static ReturnBillDao getInstance(DbHelper dbHelper) {
		if (instance == null) {
			instance = new ReturnBillDao(dbHelper, ReturnBill.class);
		}
		return instance;
	}

	public ReturnBillDao(DbHelper dbHelper, Class<ReturnBill> clazz) {
		super(dbHelper, clazz);
	}
}
