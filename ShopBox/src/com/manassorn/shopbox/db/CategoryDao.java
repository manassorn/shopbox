package com.manassorn.shopbox.db;

import com.manassorn.shopbox.value.Category;

public class CategoryDao extends Dao<Category, Integer> {
	public static final int ROOT_CATEGORY_ID = 0;
	public static final String ID = "Id";
	public static final String PARENT_ID = "ParentId";
	public static final String NAME = "Name";
	static CategoryDao instance;
	
	public static CategoryDao getInstance(DbHelper dbHelper) {
		if(instance == null) {
			instance = new CategoryDao(dbHelper, Category.class);
		}
		return instance;
	}
	
	public CategoryDao(DbHelper dbHelper, Class<Category> clazz) {
		super(dbHelper, clazz);
		// TODO Auto-generated constructor stub
	}

}
