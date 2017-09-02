package com.xingyanping.web.stat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatMonthListVo {
	private List<MonthVo> monthVoList;
	public StatMonthListVo(List<Date> dateList) {
		monthVoList = new ArrayList<>();
		fillMonthVoList(dateList);
	}
	public List<MonthVo> getMontVoList() {
		return monthVoList;
	}
	private void fillMonthVoList(List<Date> dateList) {
		for (Date date : dateList) {
			monthVoList.add(new MonthVo(date, "yyyy年MM月"));
		}
	}
	
}
