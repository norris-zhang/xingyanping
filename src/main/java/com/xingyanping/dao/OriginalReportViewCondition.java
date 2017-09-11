package com.xingyanping.dao;

import static com.xingyanping.dao.OriginalReportViewCondition.RangeType.DAILY;
import static com.xingyanping.dao.OriginalReportViewCondition.RangeType.MONTHLY;
import static com.xingyanping.dao.OriginalReportViewCondition.RangeType.UNKNOWN;

public class OriginalReportViewCondition {
	private Long upfiId;
	private RangeType rangeType;
	
	public OriginalReportViewCondition(Long upfiId, String range) {
		this.upfiId = upfiId;
		interpretRange(range);
	}

	private void interpretRange(String range) {
		if (range == null) {
			rangeType = UNKNOWN;
		} else if (range.trim().equalsIgnoreCase("d")) {
			rangeType = DAILY;
		} else if (range.trim().equalsIgnoreCase("m")) {
			rangeType = MONTHLY;
		} else {
			rangeType = UNKNOWN;
		}
	}

	public Long getUpfiId() {
		return upfiId;
	}

	public void setUpfiId(Long upfiId) {
		this.upfiId = upfiId;
	}

	public RangeType getRangeType() {
		return rangeType;
	}

	public void setRangeType(RangeType rangeType) {
		this.rangeType = rangeType;
	}

	public static enum RangeType {
		UNKNOWN, DAILY, MONTHLY
	}
}
