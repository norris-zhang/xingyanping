package com.xingyanping.dao.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.ConnectionFactory;

public class OriginalReportDaoHelper {
	private Connection conn;
	public OriginalReportDaoHelper(Connection conn) {
		this.conn = conn;
	}

	public List<OriginalReport> getByFileId(Long upfiId) throws SQLException {
		List<OriginalReport> orreList = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from original_report where orre_from_file_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orreList.add(new OriginalReport().populate(rs));
			}
			return orreList;
		} finally {
			ConnectionFactory.close(rs, pstmt, null);
		}
	}
}
