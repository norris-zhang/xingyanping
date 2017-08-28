package com.xingyanping.dao.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.ConnectionFactory;

public class OriginalReportDaoRetrieveMonthHelper {
	private Connection conn;
	private Date[] processedMinMaxDates;
	public OriginalReportDaoRetrieveMonthHelper(Connection conn) {
		this.conn = conn;
	}

	public List<OriginalReport> retrieve(Long upfiId) throws SQLException {
		FileMonthDecisionDaoHelper monthDecisionHelper = new FileMonthDecisionDaoHelper(conn);
		processedMinMaxDates = monthDecisionHelper.minMaxDate(upfiId);
		List<OriginalReport> orreList = new ArrayList<>();
		if (processedMinMaxDates == null) {
			return orreList;
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select orre.* from original_report orre join uploaded_file upfi on orre.orre_from_file_id=upfi.upfi_id"
					+ " where orre.orre_report_date>=? and orre.orre_report_date<=? and upfi.upfi_file_upload_for_date<=(select upfi_file_upload_for_date from uploaded_file where upfi_id=?) order by orre_report_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(processedMinMaxDates[0].getTime()));
			pstmt.setTimestamp(2, new Timestamp(processedMinMaxDates[1].getTime()));
			pstmt.setLong(3, upfiId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orreList.add(new OriginalReport().populate(rs));
			}
			
			return orreList;
		} finally {
			ConnectionFactory.close(rs, pstmt, null);
		}
	
	}

	public Date[] getProcessedMinMaxDates() {
		if (processedMinMaxDates == null) {
			throw new RuntimeException("Min Max Dates haven't been processed.");
		}
		return processedMinMaxDates;
	}

}
