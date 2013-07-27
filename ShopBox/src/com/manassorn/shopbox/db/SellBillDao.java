package com.manassorn.shopbox.db;

import com.manassorn.shopbox.value.SellBill;

public class SellBillDao extends BillDao<SellBill> {
	public static final String SELL_BILL_ID = "SellBIllId";
	static SellBillDao instance;

	public static SellBillDao getInstance(DbHelper dbHelper) {
		if (instance == null) {
			instance = new SellBillDao(dbHelper, SellBill.class);
		}
		return instance;
	}

	public SellBillDao(DbHelper dbHelper, Class<SellBill> clazz) {
		super(dbHelper, clazz);
	}
}
