package com.xingyanping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.util.ConnectionFactory;

public class ClientPortRelationshipDao extends BaseDao {

	public List<ClientPortRelationship> retrieveAll() throws SQLException {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from client_port_relationship";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ClientPortRelationship cprs = new ClientPortRelationship();
				
				cprs.setId(rs.getLong("cprs_id"));
				cprs.setPort(rs.getString("cprs_port"));
				cprs.setCompanyName(rs.getString("cprs_company_name"));
				cprs.setCompanyShortName(rs.getString("cprs_company_short_name"));
				cprs.setClient(rs.getString("cprs_client"));
				cprs.setUpdated(new Date(rs.getTimestamp("cprs_updated").getTime()));
				
				cprsList.add(cprs);
			}
			
			return cprsList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

}
