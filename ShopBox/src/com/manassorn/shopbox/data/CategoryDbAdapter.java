package com.manassorn.shopbox.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.manassorn.shopbox.value.Category;

public class CategoryDbAdapter {
	public static final String ID = "Id";
	public static final String PARENT_ID = "ParentId";
	public static final String NAME = "Name";
	public static final int TOP_CATEGORY_ID = 0;
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public CategoryDbAdapter(Context context) {
		this.context = context;
	}

	public CategoryDbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	public Cursor queryById(int id) {
		String query = "select Id as _id, ParentId, Name " +
				"from category where Id=" + id;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryByParentId(long parentId) {
		String query = "select Id as _id, ParentId, Name " +
				"from category where ParentId=" + parentId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public static Category[] cursorToCategoryArray(Cursor cursor) {
		if (cursor != null) {
			Category[] categoryList = new Category[cursor.getCount()];
			if (cursor.moveToFirst()) {
				int i = 0;
				do {
					Category category = new Category();
					category.setId(cursor.getInt(0));
					category.setParentId(cursor.getInt(1));
					category.setName(cursor.getString(2));
					categoryList[i++] = category;
				} while (cursor.moveToNext());
			}
			return categoryList;
		}
		return null;
	}
	
	public static Category cursorToCategory(Cursor cursor) {
		return cursorToCategoryArray(cursor)[0];
	}
}
