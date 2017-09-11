package com.xingyanping.web.stat;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xingyanping.util.DateUtil;

public class MonthVo {
	private Date monthDate;
	private String pattern;
	public MonthVo(Date monthDate, String pattern) {
		this.monthDate = monthDate;
		this.pattern = pattern;
	}
	public String getMonthDisp() {
		return new SimpleDateFormat(pattern).format(monthDate);
	}
	public String getMonthParam() {
		return new SimpleDateFormat("yyyyMM").format(monthDate);
	}
	public int getMonthNumber() {
		return DateUtil.getMonth(monthDate);
	}
}
