package com.manassorn.shopbox.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.value.Bill;

public class BillDao extends Dao<Bill, Integer> {
	public static final String ID = "Id";
	public static final String CREATED_TIME = "CreatedTime";
	public static final String RECEIVE_MONEY = "ReceiveMoney";
	public static final String TOTAL = "Total";
	static BillDao instance;

	public static BillDao getInstance(DbHelper dbHelper) {
		if (instance == null) {
			instance = new BillDao(dbHelper, Bill.class);
		}
		return instance;
	}

	public BillDao(DbHelper dbHelper, Class<Bill> clazz) {
		super(dbHelper, clazz);
	}

	public Cursor queryForToday() {
		return queryBuilder().selectCursorId(ID)
				.where("CreatedTime between date('now') and date('now', '+1 day')", null).query();
	}

	public List<Bill> getForDate(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
		String strDate = fmt.format(date);
		String sqlDate = "date('" + strDate + "')";
		String sqlNextDate = "date('" + strDate + "', '+1 day')";
		return queryBuilder().where("CreatedTime between " + sqlDate + " and " + sqlNextDate, null)
				.get();
	}

	public List<BillFolder> getYearFolder() {
		Cursor cursor = queryBuilder().select("strftime('%Y', CreatedTime), count(*)")
				.groupBy("strftime('%Y', CreatedTime)").query();
		return getBillFolder(cursor, new YearFormatter());
	}

	public List<BillFolder> getMonthFolder(int year) {
		Cursor cursor = queryBuilder().select("strftime('%m', CreatedTime), count(*)")
				.groupBy("strftime('%m', CreatedTime)").query();
		return getBillFolder(cursor, new MonthFormatter());
	}

	public List<BillFolder> getDateFolder(int year, int month) {
		Cursor cursor = queryBuilder().select("strftime('%d', CreatedTime), count(*)")
				.groupBy("strftime('%d', CreatedTime)").query();
		return getBillFolder(cursor, new DateFormatter());
	}
	
	List<BillFolder> getBillFolder(Cursor cursor, Formatter formatter) {
		List<BillFolder> folders = new ArrayList<BillFolder>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				BillFolder folder = new BillFolder();
				int dmy = cursor.getInt(0);
				folder.setLabel(formatter.format(dmy));
				folder.setValue(dmy);
				folder.setCount(cursor.getInt(1));
				folders.add(folder);
			} while (cursor.moveToNext());
		}
		return folders;
	}
	
	interface Formatter {
		String format(int val);
	}
	class YearFormatter implements Formatter {
		public String format(int val) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, val);
			return String.format("%tY", cal);
		}
	}
	class MonthFormatter implements Formatter {
		public String format(int val) {
			val = val - 1;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, val);
			return String.format("%tB", cal);
		}
	}
	class DateFormatter implements Formatter {
		public String format(int val) {
			return "" + val;
		}
	}
}
