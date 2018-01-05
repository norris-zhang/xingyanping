package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.getDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.DateUtil;

public class StatByChannelDto {
	private int month;
	private int minDate;
	private int maxDate;
	private Map<String, List<Integer>> clientStatMap;
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMinDate() {
		return minDate;
	}

	public void setMinDate(int minDate) {
		this.minDate = minDate;
	}

	public int getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(int maxDate) {
		this.maxDate = maxDate;
	}

	public Map<String, List<Integer>> getClientStatMap() {
		return clientStatMap;
	}

	public void setClientStatMap(Map<String, List<Integer>> clientStatMap) {
		this.clientStatMap = clientStatMap;
	}

	public StatByChannelDto init(List<OriginalReport> list, List<ClientPortRelationship> cprsList) {
		Map<Long, Map<Integer, Integer>> reportCountMap = new HashMap<>();
		minDate = Integer.MAX_VALUE;
		maxDate = Integer.MIN_VALUE;
		for (OriginalReport orre : list) {
			ClientPortRelationship orreCprs = orre.getMatchesClientPortRelationship();
			if (orreCprs == null) {
				continue;
			}
			Long cprsId = orreCprs.getId();
			Map<Integer, Integer> dateCountMap = reportCountMap.get(cprsId);
			if (dateCountMap == null) {
				dateCountMap = new HashMap<>();
				reportCountMap.put(cprsId, dateCountMap);
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
		
		clientStatMap = new TreeMap<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int order1 = Integer.parseInt(o1.split(":")[1]);
				int order2 = Integer.parseInt(o2.split(":")[1]);
				if (order1 == order2) {
					return o1.split(":")[0].compareTo(o2.split(":")[0]);
				} else {
					return order1 - order2;
				}
			}
		});
		for (ClientPortRelationship cprs : cprsList) {
			clientStatMap.put(generateChannelKey(cprs), calculateChannelStat(clientStatMap.get(generateChannelKey(cprs)), cprs, reportCountMap));
		}
		
		return this;
	}

	private String generateChannelKey(ClientPortRelationship cprs) {
		return cprs.getCompanyShortName() + ":" + cprs.getOrder();
	}

	private List<Integer> calculateChannelStat(List<Integer> existingList, ClientPortRelationship cprs,
			Map<Long, Map<Integer, Integer>> reportCountMap) {
		Map<Integer, Integer> dateCountMap = reportCountMap.get(cprs.getId());
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
		return sumLists(existingList, list);
	}

	private List<Integer> sumLists(List<Integer> existingList, List<Integer> list) {
		if (existingList == null || list == null || existingList.size() != list.size()) {
			return list;
		}
		for (int i = 0; i < list.size(); i++) {
			list.set(i, existingList.get(i) + list.get(i));
		}
		return list;
	}

}
