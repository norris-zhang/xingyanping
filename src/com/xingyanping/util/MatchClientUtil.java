package com.xingyanping.util;

import java.util.List;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;

public class MatchClientUtil {

	public static String matchClient(OriginalReport orre, List<ClientPortRelationship> cprsList) {
		String matchedPort = "";
		String clientName = "";
		String reportedNumber = orre.getReportedNumber();
		for (ClientPortRelationship cprs : cprsList) {
			String port = cprs.getPort();
			if (reportedNumber.startsWith(port)) {
				if (port.length() > matchedPort.length()) {
					matchedPort = port;
					clientName = cprs.getClient();
				}
			}
		}
		if (clientName.trim().length() == 0) {
			return "NotMatch";
		}
		return clientName;
	}

}
