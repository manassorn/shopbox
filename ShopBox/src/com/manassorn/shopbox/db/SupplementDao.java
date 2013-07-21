package com.manassorn.shopbox.db;

import java.util.List;

import com.manassorn.shopbox.value.Supplement;

public class SupplementDao extends Dao<Supplement, Integer> {
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String TYPE = "Type";
	public static final String PERCENT = "Percent";
	public static final String CONSTANT = "Constant";
	public static final String PRIORITY = "Priority";
	static SupplementDao instance;
	
	public static SupplementDao getInstance(DbHelper dbHelper) {
		if(instance == null) {
			instance = new SupplementDao(dbHelper, Supplement.class);
		}
		return instance;
	}
	
	public SupplementDao(DbHelper dbHelper, Class<Supplement> clazz) {
		super(dbHelper, clazz);
		// TODO Auto-generated constructor stub
	}

	public List<Supplement> getForMarkUp() {
		return queryBuilder().where("Percent > 0 OR Constant > 0", null).get();
	}

	public List<Supplement> getForDiscount() {
		return queryBuilder().where("Percent < 0 OR Constant < 0", null).get();
	}
}
