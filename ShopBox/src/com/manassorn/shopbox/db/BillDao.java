package com.manassorn.shopbox.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import android.util.Log;

import com.manassorn.shopbox.BillFolderListAdapter.BillFolder;
import com.manassorn.shopbox.value.Bill;

public abstract class BillDao<T extends Bill> extends Dao<T, Integer> {
	private static final String TAG = "BillDao";
	public static final String ID = "Id";
	public static final String CREATED_TIME = "CreatedTime";
	public static final String RECEIVE_MONEY = "ReceiveMoney";
	public static final String TOTAL = "Total";
	static final String BILL_YEAR_FORMAT = "yyyy";
	static final String BILL_MONTH_FORMAT = "MMM";
	static final String BILL_DATE_FORMAT = "d";
	static final String BILL_YEAR_MONTH_FORMAT = BILL_YEAR_FORMAT + " " + BILL_MONTH_FORMAT;

	// static BillDao instance;
	//
	// public static BillDao getInstance(DbHelper dbHelper) {
	// if (instance == null) {
	// instance = new BillDao(dbHelper, Bill.class);
	// }
	// return instance;
	// }
	//
	// public BillDao(DbHelper dbHelper, Class<T> clazz) {
	// super(dbHelper, clazz);
	// }

	protected BillDao(DbHelper dbHelper, Class<T> clazz) {
		super(dbHelper, clazz);
	}

	public Cursor queryForToday() {
		return queryBuilder().selectCursorId(ID)
				.where("CreatedTime between date('now') and date('now', '+1 day')", null).query();
	}

	public List<T> getForDate(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
		String strDate = fmt.format(date);
		String sqlDate = "date('" + strDate + "')";
		String sqlNextDate = "date('" + strDate + "', '+1 day')";
		return queryBuilder().where("CreatedTime between " + sqlDate + " and " + sqlNextDate, null)
				.get();
	}

	public List<BillFolder> getYearFolder() {
		SimpleDateFormat formatter = new SimpleDateFormat(BILL_DATE_FORMAT);
		Cursor cursor = queryBuilder().select("CreatedTime, count(*)")
				.groupBy("strftime('%Y', CreatedTime)").query();
		return getBillFolder(cursor, formatter);
	}

	public List<BillFolder> getMonthFolder(int year) {
		// TODO - condition by year
		SimpleDateFormat formatter = new SimpleDateFormat(BILL_MONTH_FORMAT);
		Cursor cursor = queryBuilder().select("CreatedTime, count(*)")
				.groupBy("strftime('%m', CreatedTime)").query();
		return getBillFolder(cursor, formatter);
	}

	public List<BillFolder> getDateFolder(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		String theMonth = new SimpleDateFormat(DATE_FORMAT).format(cal.getTime());
		theMonth = theMonth.substring(0, 7);
		SimpleDateFormat formatter = new SimpleDateFormat(BILL_DATE_FORMAT);
		Cursor cursor = queryBuilder().select("CreatedTime, count(*)")
				.like("CreatedTime", theMonth + "%")
				.groupBy("strftime('%d', CreatedTime)").query();
		return getBillFolder(cursor, formatter);
	}

	public List<BillFolder> getMonthsAgoFolder(int num) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -num);
		cal.set(Calendar.DATE, 1);
		String theDate = new SimpleDateFormat(DATE_FORMAT).format(cal.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat(BILL_YEAR_MONTH_FORMAT);
		Cursor cursor = queryBuilder().select("CreatedTime, count(*)")
				.where("CreatedTime >= ?", new String[] { theDate })
				.groupBy("strftime('%Y%m', CreatedTime)").orderBy("CreatedTime", "desc").query();
		return getBillFolder(cursor, formatter);
	}

	List<BillFolder> getBillFolder(Cursor cursor, SimpleDateFormat formatter) {
		SimpleDateFormat dbFmt = new SimpleDateFormat(DATETIME_FORMAT);
		List<BillFolder> folders = new ArrayList<BillFolder>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				try {
					BillFolder folder = new BillFolder();
					String dmy = cursor.getString(0);
					long time = dbFmt.parse(dmy).getTime();
					folder.setLabel(formatter.format(time));
					folder.setValue(time);
					folder.setCount(cursor.getInt(1));
					folders.add(folder);
				} catch (ParseException e) {
					throw new RuntimeException("BILL DATE cannot format", e);
				}
			} while (cursor.moveToNext());
		}
		return folders;
	}
}
