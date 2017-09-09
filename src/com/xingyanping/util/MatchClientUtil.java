package com.xingyanping.util;

import static com.xingyanping.util.DateUtil.getDateStart;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;

public class MatchClientUtil {
	public static final String NOT_MATCH = "NotMatch";
	private static final Map<String, String> clientNameFilter = new LinkedHashMap<>();
	static {
		clientNameFilter.put("国都003627", "国都");
		clientNameFilter.put("国都1095", "国都");
		clientNameFilter.put("国都1100", "国都");
		clientNameFilter.put("国都1106", "国都");
		clientNameFilter.put("国都003617", "国都");
		clientNameFilter.put("国都.*", "国都");
	}
	public static String matchClient(OriginalReport orre, List<ClientPortRelationship> cprsList) {
		List<ClientPortRelationship> effectiveCprsList = getEffectiveCprsList(cprsList, orre.getReportDate());
		ClientPortRelationship matchedCprs = null;
		String matchedPort = "";
		String clientName = "";
		String reportedNumber = orre.getReportedNumber();
		for (ClientPortRelationship cprs : effectiveCprsList) {
			String port = cprs.getPort();
			if (reportedNumber.startsWith(port)) {
				if (port.length() > matchedPort.length()) {
					matchedPort = port;
					clientName = cprs.getClient();
					matchedCprs = cprs;
				}
			}
		}
		if (clientName.trim().length() == 0) {
			return NOT_MATCH;
		}
		orre.setMatchesClientPortRelationship(matchedCprs);
		return filterClientName(clientName);
	}
	private static List<ClientPortRelationship> getEffectiveCprsList(List<ClientPortRelationship> cprsList,
			Date reportDate) {
		reportDate = getDateStart(reportDate);
		List<ClientPortRelationship> list = new ArrayList<>();
		for (ClientPortRelationship cprs : cprsList) {
			if (reportDate.before(getDateStart(cprs.getEffectiveDate()))) {
				continue;
			}
			if (cprs.getExpiringDate() == null
					|| getDateStart(cprs.getExpiringDate()).after(reportDate)
					|| getDateStart(cprs.getExpiringDate()).equals(reportDate)) {
				list.add(cprs);
				continue;
			}
		}
		return list;
	}
	private static String filterClientName(String clientName) {
		for (Entry<String, String> entry : clientNameFilter.entrySet()) {
			if (clientName.matches(entry.getKey())) {
				return entry.getValue();
			}
		}
		return clientName;
	}
}
