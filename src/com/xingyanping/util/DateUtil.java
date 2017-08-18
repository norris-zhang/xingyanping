package com.xingyanping.util;

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

}
