package com.xingyanping.util;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date parseDate(String dateString) throws ParseException {
		if (dateString == null || dateString.trim().length() == 0) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(dateString);
	}

	public static boolean isGT(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return date1.after(date2);
	}
	
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}

	public static Date parseDateToEnd(String dateString) throws ParseException {
		Date date = parseDate(dateString);
		if (date == null) {
			return date;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getMonthStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(DATE, 1);
		c.set(HOUR_OF_DAY, 0);
		c.set(MINUTE, 0);
		c.set(SECOND, 0);
		c.set(MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date getMonthEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(DATE, 1);
		c.set(HOUR_OF_DAY, 0);
		c.set(MINUTE, 0);
		c.set(SECOND, 0);
		c.set(MILLISECOND, 0);
		c.add(MONTH, 1);
		c.add(SECOND, -1);
		return c.getTime();
	}

	public static int getDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(DATE);
	}

	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(MONTH) + 1;
	}
}
