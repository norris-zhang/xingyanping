package com.xingyanping.util;

import java.util.List;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;

public class MatchClientUtil {
	public static final String NOT_MATCH = "NotMatch";
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
		return clientName;
	}

}
