package com.xingyanping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.util.ConnectionFactory;

public class StatMonthListDao extends BaseDao {

	public List<Date> getMonthList() throws SQLException {
		List<Date> list = new ArrayList<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select distinct extract(year_month from orre_report_date) yearmonth from original_report order by yearmonth desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int yearMonth = rs.getInt(1);
				list.add(df.parse("" + yearMonth));
				
			}
			return list;
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

}
