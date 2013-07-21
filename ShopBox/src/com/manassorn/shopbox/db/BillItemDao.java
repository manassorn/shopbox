package com.manassorn.shopbox.db;

import java.sql.SQLException;

import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSubTotalItem;
import com.manassorn.shopbox.value.BillSupplementItem;

public class BillItemDao extends Dao<BillItem, Integer> {
	static BillItemDao instance;
	Dao<BillProductItem, Integer> productItemDao;
	Dao<BillSupplementItem, Integer> supplementItemDao;
	Dao<BillSubTotalItem, Integer> subtotalItemDao;
	
	public static BillItemDao getInstance(DbHelper dbHelper) {
		if(instance == null) {
			instance = new BillItemDao(dbHelper, BillItem.class);
		}
		return instance;
	}

	public BillItemDao(DbHelper dbHelper, Class<BillItem> clazz) {
		super(dbHelper, clazz);
		productItemDao = new Dao<BillProductItem, Integer>(dbHelper, BillProductItem.class);
		supplementItemDao = new Dao<BillSupplementItem, Integer>(dbHelper, BillSupplementItem.class);
		subtotalItemDao = new Dao<BillSubTotalItem, Integer>(dbHelper, BillSubTotalItem.class);
	}

	@Override
	public int insert(BillItem data) throws SQLException {
		if(data instanceof BillProductItem) {
			return productItemDao.insert((BillProductItem) data);
		} else if(data instanceof BillSupplementItem) {
			return supplementItemDao.insert((BillSupplementItem) data);
		} else if(data instanceof BillSubTotalItem) {
			return subtotalItemDao.insert((BillSubTotalItem) data);
		}
		return 0;
	}

}
