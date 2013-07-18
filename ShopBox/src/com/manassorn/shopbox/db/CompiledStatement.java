package com.manassorn.shopbox.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class CompiledStatement {

	public CompiledStatement(String sql, SQLiteDatabase db) {

	}

	public void runExecute() {

	}

	static int execSql(SQLiteDatabase db, String label, String finalSql, Object[] argArray)
			throws SQLException {
		try {
			db.execSQL(finalSql, argArray);
		} catch (android.database.SQLException e) {
			throw e;
		}
		int result;
		SQLiteStatement stmt = null;
		try {
			// ask sqlite how many rows were just changed
			stmt = db.compileStatement("SELECT CHANGES()");
			result = (int) stmt.simpleQueryForLong();
		} catch (android.database.SQLException e) {
			// ignore the exception and just return 1 if it failed
			result = 1;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result;
	}
}
