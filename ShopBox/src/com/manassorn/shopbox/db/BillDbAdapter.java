package com.manassorn.shopbox.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.manassorn.shopbox.value.Bill;
import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSubTotalItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Supplement.SupplementType;

public class BillDbAdapter {
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String TYPE = "Type";
	public static final String PERCENT = "Percent";
	public static final String CONSTANT = "Constant";
	public static final String PRIORITY = "Priority";
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;
	static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public BillDbAdapter(Context context) {
		this.context = context;
	}

	public BillDbAdapter open() throws SQLException {
		dbHelper = new DbHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public long insertBill(Bill bill) {
		ContentValues values = new ContentValues();
		values.put("ReceiveMoney", bill.getReceiveMoney());
		values.put("CreatedTime", dateTimeFormat.format(bill.getCreatedTime()));
		values.put("Total", bill.getTotal());
		long billId = db.insert("Bill", null, values);
		for (BillItem billItem : bill.getBillItems()) {
			if (billItem instanceof BillProductItem) {
				insertBillProductItem(billId, (BillProductItem) billItem);
			} else if (billItem instanceof BillSupplementItem) {
				insertBillSupplementItem(billId, (BillSupplementItem) billItem);
			} else if (billItem instanceof BillSubTotalItem) {
				insertBillSubTotalItem(billId, (BillSubTotalItem) billItem);
			}
		}
		return billId;
	}

	protected void insertBillProductItem(long billId, BillProductItem item) {
		ContentValues values = new ContentValues();
		values.put("BillId", billId);
		values.put("Sequence", item.getSequence());
		values.put("ProductId", item.getProductId());
		values.put("ProductName", item.getProductName());
		values.put("ProductPrice", item.getProductPrice());
		values.put("Amount", item.getAmount());
		values.put("Total", item.getTotal());
		db.insert("BillProductItem", null, values);
	}

	protected void insertBillSupplementItem(long billId, BillSupplementItem item) {
		ContentValues values = new ContentValues();
		values.put("BillId", billId);
		values.put("Sequence", item.getSequence());
		values.put("SupplementId", item.getSupplementId());
		values.put("SupplementName", item.getSupplementName());
		values.put("SupplementType", item.getSupplementType().name());
		values.put("SupplementPercent", item.getSupplementPercent());
		values.put("SupplementConstant", item.getSupplementConstant());
		values.put("Total", item.getTotal());
		db.insert("BillSupplementItem", null, values);
	}

	protected void insertBillSubTotalItem(long billId, BillSubTotalItem item) {
		ContentValues values = new ContentValues();
		values.put("BillId", billId);
		values.put("Sequence", item.getSequence());
		values.put("SubTotal", item.getSubTotal());
		db.insert("BillSubTotalItem", null, values);
	}
	
	public Bill getBill(int billId) {
		Bill bill = oneBill(queryBill(billId));
		bill.setBillItems(getBillItemList(billId));
		return bill;
	}
	
	public ArrayList<BillItem> getBillItemList(int billId) {
		ArrayList<BillItem> billItems = new ArrayList<BillItem>();
		billItems.addAll(cursorToBillProductItem(queryBillProductItem(billId)));
		billItems.addAll(cursorToBillSupplementItem(queryBillSupplementItem(billId)));
		billItems.addAll(cursorToBillSubTotalItem(queryBillSubTotalItem(billId)));
		Collections.sort(billItems, BillItem.comparator());
		return billItems;
	}
	
	public Cursor queryTodayBill() {
		String query = "select Id as _id, Id, CreatedTime, ReceiveMoney, Total " +
				"from Bill where CreatedTime like '" + dateFormat.format(new Date()) + "%' " +
				"order by CreatedTime desc";
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryBill(int billId) {
		String query = "select Id, CreatedTime, ReceiveMoney, Total " +
				"from Bill where Id=" + billId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryBillProductItem(int billId) {
		String query = "select BillId, Sequence, ProductId, ProductName, ProductPrice, " +
				"Amount, Total from BillProductItem where BillId=" + billId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryBillSupplementItem(int billId) {
		String query = "select BillId, Sequence, SupplementId, SupplementName, " +
				"SupplementType, SupplementPercent, SupplementConstant, Total " +
				"from BillSupplementItem where BillId=" + billId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public Cursor queryBillSubTotalItem(int billId) {
		String query = "select BillId, Sequence, SubTotal from BillSubTotalItem "
				+ "where BillId=" + billId;
		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	public static Bill oneBill(Cursor cursor) {
		if(cursor == null) return null;
		if(cursor.moveToFirst()) {
			Bill bill = new Bill();
			bill.setId(cursor.getInt(0));
			try {
				bill.setCreatedTime(dateTimeFormat.parse(cursor.getString(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			bill.setReceiveMoney(cursor.getDouble(2));
			bill.setTotal(cursor.getDouble(3));
			return bill;
		}
		return null;
		
	}
	
	public static ArrayList<BillProductItem> cursorToBillProductItem(Cursor cursor) {
		if(cursor == null) return null;
		ArrayList<BillProductItem> list = new ArrayList<BillProductItem>(cursor.getCount());
		if(cursor.moveToFirst()) {
			do {
				BillProductItem item = new BillProductItem();
				item.setBillId(cursor.getInt(0));
				item.setSequence(cursor.getInt(1));
				item.setProductId(cursor.getInt(2));
				item.setProductName(cursor.getString(3));
				item.setProductPrice(cursor.getDouble(4));
				item.setAmount(cursor.getInt(5));
				list.add(item);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static ArrayList<BillSupplementItem> cursorToBillSupplementItem(Cursor cursor) {
		if(cursor == null) return null;
		ArrayList<BillSupplementItem> list = new ArrayList<BillSupplementItem>(cursor.getCount());
		if(cursor.moveToFirst()) {
			do {
				BillSupplementItem item = new BillSupplementItem();
				item.setBillId(cursor.getInt(0));
				item.setSequence(cursor.getInt(1));
				item.setSupplementId(cursor.getInt(2));
				item.setSupplementName(cursor.getString(3));
				item.setSupplementType(SupplementType.valueOf(cursor.getString(4)));
				item.setSupplementPercent(cursor.getDouble(5));
				item.setSupplementConstant(cursor.getDouble(6));
				item.setTotal(cursor.getDouble(7));
				list.add(item);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static ArrayList<BillSubTotalItem> cursorToBillSubTotalItem(Cursor cursor) {
		if(cursor == null) return null;
		ArrayList<BillSubTotalItem> list = new ArrayList<BillSubTotalItem>(cursor.getCount());
		if(cursor.moveToFirst()) {
			do {
				BillSubTotalItem item = new BillSubTotalItem();
				item.setBillId(cursor.getInt(0));
				item.setSequence(cursor.getInt(1));
				item.setSubTotal(cursor.getDouble(2));
				list.add(item);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public static SimpleDateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
}
