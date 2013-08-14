package com.manassorn.shopbox.db;

import android.database.Cursor;

import com.manassorn.shopbox.value.ShopAttributes;

public class ShopAttributesDao extends Dao<ShopAttributes, Integer> {
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String BRANCH_NAME = "BranchName";
	public static final String TAX_ID = "TaxId";
	static ShopAttributesDao instance;

	public static ShopAttributesDao getInstance(DbHelper dbHelper) {
		if (instance == null) {
			instance = new ShopAttributesDao(dbHelper, ShopAttributes.class);
		}
		return instance;
	}

	public ShopAttributesDao(DbHelper dbHelper, Class<ShopAttributes> clazz) {
		super(dbHelper, clazz);
	}
	
	public ShopAttributes getForLast() {
		return mapRow(queryForLast());
	}
	
	public Cursor queryForLast() {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ").append(tableInfo.getTableName());
		sb.append(" order by ").append(ID);
		sb.append(" desc limit 1");
		return query(sb.toString(), null);
	}
}
