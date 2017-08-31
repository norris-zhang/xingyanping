package com.xingyanping.util;

import static com.xingyanping.util.MatchClientUtil.NOT_MATCH;
import static com.xingyanping.util.MatchClientUtil.matchClient;

import java.util.Iterator;
import java.util.List;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;

public class ClientFiller {
	private List<OriginalReport> orreList;
	private List<ClientPortRelationship> cprsList;
	private boolean removeNotMatch;
	public ClientFiller(List<OriginalReport> orreList, List<ClientPortRelationship> cprsList, boolean removeNotMatch) {
		this.orreList = orreList;
		this.cprsList = cprsList;
		this.removeNotMatch = removeNotMatch;
	}
	public void execute() {
		for (Iterator<OriginalReport> iter = orreList.iterator(); iter.hasNext();) {
			OriginalReport orre = iter.next();
			String matchClient = matchClient(orre, cprsList);
			if (removeNotMatch && NOT_MATCH.equals(matchClient)) {
				iter.remove();
			}
		}
	}
	
}
