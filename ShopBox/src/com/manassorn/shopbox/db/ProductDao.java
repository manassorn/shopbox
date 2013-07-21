package com.manassorn.shopbox.db;

import android.database.Cursor;

import com.manassorn.shopbox.value.Product;

public class ProductDao extends Dao<Product, Integer> {
	public static final String ID = "Id";
	public static final String PARENT_ID = "ParentId";
	public static final String NAME = "Name";
	public static final String AMOUNT = "Amount";
	public static final String PRICE = "Price";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String BARCODE = "Barcode";
	static ProductDao instance;
	
	public static ProductDao getInstance(DbHelper dbHelper) {
		if(instance == null) {
			instance = new ProductDao(dbHelper, Product.class);
		}
		return instance;
	}

	public ProductDao(DbHelper dbHelper, Class<Product> clazz) {
		super(dbHelper, clazz);
	}
	
	public Cursor queryForBarcodeStartsWith(String str) {
		return queryBuilder().selectCursorId(ID).like(BARCODE, str + "%").query();
	}

}
