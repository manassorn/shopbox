package com.manassorn.shopbox.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.manassorn.shopbox.value.Supplement;
import com.manassorn.shopbox.value.Supplement.SupplementType;

public class SupplementDbAdapter {
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String TYPE = "Type";
	public static final String PERCENT = "Percent";
	public static final String CONSTANT = "Constant";
	public static final String PRIORITY = "Priority";
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public SupplementDbAdapter(Context context) {
		this.context = context;
	}

	public SupplementDbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	public Cursor querySupplement() {
		String query = "select Id as _id, Name, Type, Percent, Constant, Priority " +
				"from supplement";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryDiscount() {
		String query = "select Id as _id, Name, Type, Percent, Constant, Priority " +
				"from supplement where Percent<0 or Constant<0";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryMarkUp() {
		String query = "select Id as _id, Name, Type, Percent, Constant, Priority " +
				"from supplement where Percent>0 or Constant>0";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public static Supplement[] cursorToSupplement(Cursor cursor) {
		if (cursor != null) {
			Supplement[] supplementList = new Supplement[cursor.getCount()];
			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					Supplement supplement = new Supplement();
					supplement.setId(cursor.getInt(0));
					supplement.setName(cursor.getString(1));
					supplement.setType(SupplementType.valueOf(cursor.getString(2)));
					supplement.setPercent(cursor.getDouble(3));
					supplement.setConstant(cursor.getDouble(4));
					supplement.setPriority(cursor.getInt(5));
					supplementList[i++] = supplement;
				} while (cursor.moveToNext());
			}
			return supplementList;
		}
		return null;
	}
}
