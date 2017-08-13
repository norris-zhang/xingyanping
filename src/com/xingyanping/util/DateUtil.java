package com.xingyanping.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.parse(dateString);
	}

	public static boolean isGT(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return date1.after(date2);
	}
}
