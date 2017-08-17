package com.xingyanping.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;

public class MatchClientUtil {
	public static final String NOT_MATCH = "NotMatch";
	private static final Map<String, String> clientNameFilter = new HashMap<>();
	static {
		clientNameFilter.put("国都003627", "国都");
		clientNameFilter.put("国都1095", "国都");
		clientNameFilter.put("国都1100", "国都");
		clientNameFilter.put("国都1106", "国都");
		clientNameFilter.put("国都003617", "国都");
	}
	public static String matchClient(OriginalReport orre, List<ClientPortRelationship> cprsList) {
		ClientPortRelationship matchedCprs = null;
		String matchedPort = "";
		String clientName = "";
		String reportedNumber = orre.getReportedNumber();
		for (ClientPortRelationship cprs : cprsList) {
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
	private static String filterClientName(String clientName) {
		return clientNameFilter.containsKey(clientName) ? clientNameFilter.get(clientName) : clientName;
	}

}
