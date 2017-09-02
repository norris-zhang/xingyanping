package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.getDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.DateUtil;

public class StatByTypeDto {
	private int month;
	private int minDate;
	private int maxDate;
	private List<Integer> nonCodeComplaint;
	private List<Integer> typeAComplaint;
	private List<Integer> typeBComplaint;
	private List<Integer> typeCComplaint;
	private List<Integer> typeDComplaint;
	private List<Integer> typeEComplaint;
	
	public int getMonth() {
		return month;
	}

	public int getMinDate() {
		return minDate;
	}

	public int getMaxDate() {
		return maxDate;
	}

	public List<Integer> getNonCodeComplaint() {
		return nonCodeComplaint;
	}

	public List<Integer> getTypeAComplaint() {
		return typeAComplaint;
	}

	public List<Integer> getTypeBComplaint() {
		return typeBComplaint;
	}

	public List<Integer> getTypeCComplaint() {
		return typeCComplaint;
	}

	public List<Integer> getTypeDComplaint() {
		return typeDComplaint;
	}

	public List<Integer> getTypeEComplaint() {
		return typeEComplaint;
	}

	public StatByTypeDto init(List<OriginalReport> list) {
		Map<String, Map<Integer, Integer>> reportCountMap = new HashMap<>();
		minDate = Integer.MAX_VALUE;
		maxDate = Integer.MIN_VALUE;
		for (OriginalReport orre : list) {
			String complaintType = orre.getComplaintType();
			Map<Integer, Integer> dateCountMap = reportCountMap.get(complaintType);
			if (dateCountMap == null) {
				dateCountMap = new HashMap<>();
				reportCountMap.put(complaintType, dateCountMap);
			}
			Date reportDate = orre.getReportDate();
			if (reportDate == null) {
				continue;
			}
			int date = getDate(reportDate);
			if (date < minDate) {
				minDate = date;
			}
			if (date > maxDate) {
				maxDate = date;
			}
			month = DateUtil.getMonth(reportDate);
			Integer count = dateCountMap.get(date);
			if (count == null) {
				dateCountMap.put(date, 1);
			} else {
				dateCountMap.put(date, count + 1);
			}
		}
		
		typeAComplaint = calculateComplaintByType(reportCountMap, "A");
		typeBComplaint = calculateComplaintByType(reportCountMap, "B");
		typeCComplaint = calculateComplaintByType(reportCountMap, "C");
		typeDComplaint = calculateComplaintByType(reportCountMap, "D");
		typeEComplaint = calculateComplaintByType(reportCountMap, "E");
		nonCodeComplaint = calculateNonCodeComplaint(typeBComplaint,
													 typeCComplaint,
													 typeDComplaint,
													 typeEComplaint);
		
		return this;
	}
	private static List<Integer> calculateNonCodeComplaint(List<Integer> bList,
														   List<Integer> cList,
														   List<Integer> dList,
														   List<Integer> eList) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < bList.size(); i++) {
			list.add(bList.get(i) + cList.get(i) + dList.get(i) + eList.get(i));
		}
		return list;
	}
	private List<Integer> calculateComplaintByType(Map<String, Map<Integer, Integer>> reportCountMap, String complaintType) {
		Map<Integer, Integer> dateCountMap = reportCountMap.get(complaintType);
		List<Integer> list = new ArrayList<>();
		list.add(0);
		int total = 0;
		for (int i = maxDate; i >= minDate; i--) {
			Integer count = null;
			if (dateCountMap != null) {
				count = dateCountMap.get(i);
			}
			if (count == null) {
				count = 0;
			}
			total += count;
			list.add(count);
		}
		list.set(0, total);
		return list;
	}
	
}
