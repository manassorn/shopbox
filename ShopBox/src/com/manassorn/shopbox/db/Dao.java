package com.manassorn.shopbox.db;

import java.util.List;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Dao<T, ID> {
	protected DbHelper dbHelper;
	protected Class<T> clazz;
	protected DatabaseTableConfig<T> tableConfig;

	public Dao(DbHelper dbHelper, Class<T> clazz) {
		this.dbHelper = dbHelper;
		this.clazz = clazz;
		this.tableConfig = new DatabaseTableConfig<T>(clazz);
	}
	
	public T queryForId(ID id) {
		return null;
	}
	
	public T queryForFirst(/*PreparedQuery<T> preparedQuery*/ String sql) {
		return null;
	}
	
	public List<T> queryForAll() {
		return null;
	}
	
//	public List<T> queryForEq(String fieldName, Object value) throws SQLException {
////		return queryBuilder().where().eq(fieldName, value).query();
//		String tableName = tableConfig.getTableName();
//		String sql = "select * from " + tableName + " where " + fieldName + "=" + value;
//		return query(sql);
//	}
	
//	public List<T> query(/*PrepareQuery<T> preparedQuery*/ String sql, String[] selectionArgs) {
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		Cursor cursor = db.rawQuery(sql, selectionArgs)
//		return null;
//	}
}
