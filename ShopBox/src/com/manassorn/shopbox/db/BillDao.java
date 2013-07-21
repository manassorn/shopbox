package com.manassorn.shopbox.db;

import android.database.Cursor;

import com.manassorn.shopbox.value.Bill;

public class BillDao extends Dao<Bill, Integer> {
	public static final String ID = "Id";
	public static final String CREATED_TIME = "CreatedTime";
	public static final String RECEIVE_MONEY = "ReceiveMoney";
	public static final String TOTAL = "Total";
	static BillDao instance;
	
	public static BillDao getInstance(DbHelper dbHelper) {
		if(instance == null) {
			instance = new BillDao(dbHelper, Bill.class);
		}
		return instance;
	}

	public BillDao(DbHelper dbHelper, Class<Bill> clazz) {
		super(dbHelper, clazz);
	}
	
	public Cursor queryForToday() {
		//TODO - query for today
		return queryBuilder().selectCursorId(ID).query();
	}

}
