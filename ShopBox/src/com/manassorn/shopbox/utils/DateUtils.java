package com.manassorn.shopbox.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

public class DateUtils {
	public static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat dayMonthFormat = new SimpleDateFormat("dd MMM");

	public static String formatFriendly(Context context, Date date) {
		DateFormat timeFormat = android.text.format.DateFormat
				.getTimeFormat(context);
		DateFormat dateFormat = android.text.format.DateFormat
				.getDateFormat(context);
		String timeStr = timeFormat.format(date);
		String dateStr = "";
		if (isToday(date)) {
			dateStr = "วันนี้";
		} else if (isYesterday(date)) {
			dateStr = "เมื่อวานนี้";
//		} else if (isThisYear(date)) {
//			dateStr = dayMonthFormat.format(date);
		} else {
			dateStr = dateFormat.format(date);
		}
		return dateStr + " " + timeStr;
	}

	public static boolean isToday(Date date) {
		return dateFormat.format(date).equals(dateFormat.format(new Date()));
	}

	public static boolean isYesterday(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		return dateFormat.format(date).equals(dateFormat.format(yesterday.getTime()));
	}

	public static boolean isThisYear(Date date) {
		return yearFormat.format(date).equals(yearFormat.format(new Date()));
	}
}
