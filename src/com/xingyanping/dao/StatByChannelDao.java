package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.getMonthEnd;
import static com.xingyanping.util.DateUtil.getMonthStart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.ClientFiller;
import com.xingyanping.util.ConnectionFactory;

public class StatByChannelDao extends BaseDao {

	public StatByChannelDto execute(Date monthDate) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from original_report where upper(orre_complaint_type) in ('B', 'C', 'D', 'E') and orre_report_date >= ? and orre_report_date <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(getMonthStart(monthDate).getTime()));
			pstmt.setTimestamp(2, new Timestamp(getMonthEnd(monthDate).getTime()));
			rs = pstmt.executeQuery();
			List<OriginalReport> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new OriginalReport().populate(rs));
			}
			
			List<ClientPortRelationship> cprsList = new ClientPortRelationshipDao().retrieveAll();
			new ClientFiller(list, cprsList, true).execute();
			
			
			return new StatByChannelDto().init(list, cprsList);
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

}
