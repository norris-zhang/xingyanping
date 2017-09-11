package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.getDate;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.DateUtil;

public class StatByClientDto {
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

	public StatByClientDto init(List<OriginalReport> list, List<ClientPortRelationship> cprsList) {
		Map<String, Map<Integer, Integer>> reportCountMap = new HashMap<>();
		minDate = Integer.MAX_VALUE;
		maxDate = Integer.MIN_VALUE;
		for (OriginalReport orre : list) {
			String client = orre.getMatchesClientPortRelationship().getClient();
			Map<Integer, Integer> dateCountMap = reportCountMap.get(client);
			if (dateCountMap == null) {
				dateCountMap = new HashMap<>();
				reportCountMap.put(client, dateCountMap);
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
			private Collator collator = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
			@Override
			public int compare(String o1, String o2) {
				CollationKey key1 = collator.getCollationKey(o1);
				CollationKey key2 = collator.getCollationKey(o2);
				return key1.compareTo(key2);
			}
		});
		for (ClientPortRelationship cprs : cprsList) {
			List<Integer> clientCount = calculateClientStat(cprs, reportCountMap);
			if (clientCount.get(0) > 0) {
				clientStatMap.put(cprs.getClient(), clientCount);
			}
		}
		
		return this;
	}

	private List<Integer> calculateClientStat(ClientPortRelationship cprs,
			Map<String, Map<Integer, Integer>> reportCountMap) {
		Map<Integer, Integer> dateCountMap = reportCountMap.get(cprs.getClient());
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
