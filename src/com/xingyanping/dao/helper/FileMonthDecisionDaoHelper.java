package com.xingyanping.dao.helper;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.xingyanping.util.ConnectionFactory;

public class FileMonthDecisionDaoHelper {
	private Connection conn;
	public FileMonthDecisionDaoHelper(Connection conn) {
		this.conn = conn;
	}
	public Date[] minMaxDate(Long upfiId) throws SQLException {
		Date[] minMaxDates = retrieveMinMaxDate(upfiId);
		if (minMaxDates[0] == null || minMaxDates[1] == null) {
			return null;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(minMaxDates[0]);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(minMaxDates[1]);
		
		int minMonth = c1.get(Calendar.MONTH);
		int maxMonth = c2.get(Calendar.MONTH);
		if (minMonth != maxMonth) {
			return wholeMonth(c1);
		} else {
			return monthTill(c2);
		}
	}
	private Date[] retrieveMinMaxDate(Long upfiId) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select min(orre_report_date), max(orre_report_date) from original_report where orre_from_file_id=? and orre_report_date is not null";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			
			rs = pstmt.executeQuery();
			Date[] minMaxDate = new Date[2];
			while (rs.next()) {
				Timestamp minDate = rs.getTimestamp(1);
				Timestamp maxDate = rs.getTimestamp(2);
				if (minDate != null) {
					minMaxDate[0] = new Date(minDate.getTime());
				}
				if (maxDate != null) {
					minMaxDate[1] = new Date(maxDate.getTime());
				}
			}

			return minMaxDate;
		} finally {
			ConnectionFactory.close(rs, pstmt, null);
		}
	}
	private Date[] wholeMonth(Calendar c) {
		c.set(DATE, 1);
		c.set(HOUR_OF_DAY, 0);
		c.set(MINUTE, 0);
		c.set(SECOND, 0);
		c.set(MILLISECOND, 0);
		Date min = c.getTime();
		
		c.add(MONTH, 1);
		c.add(SECOND, -1);
		Date max = c.getTime();
		
		return new Date[]{min, max};
	}
	private Date[] monthTill(Calendar c) {
		Date max = c.getTime();
		
		c.set(DATE, 1);
		c.set(HOUR_OF_DAY, 0);
		c.set(MINUTE, 0);
		c.set(SECOND, 0);
		c.set(SECOND, 0);
		Date min = c.getTime();
		
		return new Date[]{min, max};
	}
}
