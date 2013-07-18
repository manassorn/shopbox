package com.manassorn.shopbox.db;


public class DaoManager {

	public static <D, T> D createDao(DbHelper dbHelper, Class<T> clazz) {
		return (D) new Dao(dbHelper, clazz);
	}
}
