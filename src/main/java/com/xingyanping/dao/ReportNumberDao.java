package com.xingyanping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.util.ConnectionFactory;

public class ReportNumberDao {
	private Date fileUploadedForDate;
	public List<String> getBlacklistGongyi(Long upfiId) throws SQLException {
		List<String> orreList = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select distinct orre.orre_report_mobile_number, upfi.upfi_file_upload_for_date from original_report orre join uploaded_file upfi on orre.orre_from_file_id=upfi.upfi_id where orre.orre_from_file_id=? and orre.orre_reported_number=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			pstmt.setString(2, "1065732010086");

			rs = pstmt.executeQuery();
			while (rs.next()) {
				orreList.add(rs.getString(1));
				Timestamp fileUploadFor = rs.getTimestamp(2);
				if (fileUploadFor != null) {
					this.fileUploadedForDate = new Date(fileUploadFor.getTime());
				}
			}

			return orreList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
	public Date getFileUploadedForDate() {
		return fileUploadedForDate;
	}
}
