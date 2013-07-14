package com.manassorn.shopbox.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.manassorn.shopbox.value.Product;

public class ProductDbAdapter {
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String AMOUNT = "Amount";
	public static final String PRICE = "Price";
	public static final String CATEGORY_ID = "CategoryId";
	public static final String BARCODE = "Barcode";
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public ProductDbAdapter(Context context) {
		this.context = context;
	}

	public ProductDbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public Cursor startsWithBarcode(String barcode) {
		String query = "select Id as _id, Name, CategoryId, Price, Barcode from product where Barcode like '"
				+ barcode + "%'";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryByCategoryId(long categoryId) {
		String query = "select Id as _id, Name, CategoryId, Price, Barcode " +
				"from product where CategoryId=" + categoryId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public static Product[] cursorToProduct(Cursor cursor) {
		if (cursor != null) {
			Product[] productList = new Product[cursor.getCount()];
			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					Product product = new Product();
					product.setId(cursor.getInt(0));
					product.setName(cursor.getString(1));
					product.setCategoryId(cursor.getInt(2));
					product.setPrice(cursor.getDouble(3));
					productList[i++] = product;
				} while (cursor.moveToNext());
			}
			return productList;
		}
		return null;
	}
}
