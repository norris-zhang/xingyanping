package com.xingyanping.dao;

import static java.sql.Types.TIMESTAMP_WITH_TIMEZONE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
			String sql = "select * from client_port_relationship order by cprs_port, cprs_effective_date";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ClientPortRelationship cprs = new ClientPortRelationship();
				cprs.populate(rs);

				cprsList.add(cprs);
			}
			
			return cprsList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

	public ClientPortRelationship get(Long id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from client_port_relationship where cprs_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, id);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ClientPortRelationship cprs = new ClientPortRelationship();				
				
				cprs.setId(rs.getLong("cprs_id"));
				cprs.setPort(rs.getString("cprs_port"));
				cprs.setCompanyName(rs.getString("cprs_company_name"));
				cprs.setCompanyShortName(rs.getString("cprs_company_short_name"));
				cprs.setClient(rs.getString("cprs_client"));
				cprs.setUpdated(new Date(rs.getTimestamp("cprs_updated").getTime()));
				cprs.setEffectiveDate(new Date(rs.getTimestamp("cprs_effective_date").getTime()));
				Timestamp expiringDate = rs.getTimestamp("cprs_expiring_date");
				if (expiringDate != null) {
					cprs.setExpiringDate(new Date(expiringDate.getTime()));
				}
				return cprs;
			}
			throw new RuntimeException("No such client id = " + id);
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

	public List<ClientPortRelationship> getPortGroup(String port) throws SQLException {
		List<ClientPortRelationship> cprsList = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from client_port_relationship where cprs_port=? order by cprs_effective_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, port);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ClientPortRelationship cprs = new ClientPortRelationship();
				
				cprs.setId(rs.getLong("cprs_id"));
				cprs.setPort(rs.getString("cprs_port"));
				cprs.setCompanyName(rs.getString("cprs_company_name"));
				cprs.setCompanyShortName(rs.getString("cprs_company_short_name"));
				cprs.setClient(rs.getString("cprs_client"));
				cprs.setUpdated(new Date(rs.getTimestamp("cprs_updated").getTime()));
				cprs.setEffectiveDate(new Date(rs.getTimestamp("cprs_effective_date").getTime()));
				Timestamp expiringDate = rs.getTimestamp("cprs_expiring_date");
				if (expiringDate != null) {
					cprs.setExpiringDate(new Date(expiringDate.getTime()));
				}
				
				cprsList.add(cprs);
			}
			
			return cprsList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

	public void update(ClientPortRelationship theClient) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "update client_port_relationship set cprs_port=?, cprs_company_name=?, cprs_company_short_name=?, cprs_client=?, cprs_updated=?, cprs_effective_date=?, cprs_expiring_date=? where cprs_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, theClient.getPort());
			pstmt.setString(2, theClient.getCompanyName());
			pstmt.setString(3, theClient.getCompanyShortName());
			pstmt.setString(4, theClient.getClient());
			pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
			pstmt.setTimestamp(6, new Timestamp(theClient.getEffectiveDate().getTime()));
			Date expiringDate = theClient.getExpiringDate();
			if (expiringDate == null) {
				pstmt.setNull(7, TIMESTAMP_WITH_TIMEZONE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(theClient.getExpiringDate().getTime()));
			}
			pstmt.setLong(8, theClient.getId());
			
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

	public void insert(ClientPortRelationship theClient) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "insert into client_port_relationship (cprs_port, cprs_company_name, cprs_company_short_name, cprs_client, cprs_updated, cprs_effective_date, cprs_expiring_date) values (?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, theClient.getPort());
			pstmt.setString(2, theClient.getCompanyName());
			pstmt.setString(3, theClient.getCompanyShortName());
			pstmt.setString(4, theClient.getClient());
			pstmt.setTimestamp(5, new Timestamp(new Date().getTime()));
			pstmt.setTimestamp(6, new Timestamp(theClient.getEffectiveDate().getTime()));
			Date expiringDate = theClient.getExpiringDate();
			if (expiringDate == null) {
				pstmt.setNull(7, TIMESTAMP_WITH_TIMEZONE);
			} else {
				pstmt.setTimestamp(7, new Timestamp(theClient.getExpiringDate().getTime()));
			}
			
			pstmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
}
