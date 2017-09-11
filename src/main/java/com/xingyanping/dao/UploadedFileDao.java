package com.xingyanping.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xingyanping.datamodel.UploadedFile;
import com.xingyanping.util.ConnectionFactory;

public class UploadedFileDao extends BaseDao {

	public void save(UploadedFile upfi) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet generatedKeys = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			String sql = "insert into uploaded_file (`upfi_name`, `upfi_file_upload_for_date`, `upfi_file_date`) values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, upfi.getName());
			pstmt.setTimestamp(2, new Timestamp(upfi.getFileUploadForDate().getTime()));
			pstmt.setTimestamp(3, upfi.getFileDate() == null ? null : new Timestamp(upfi.getFileDate().getTime()));
			pstmt.executeUpdate();
			generatedKeys = pstmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				upfi.setId(generatedKeys.getLong(1));
			}
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			conn.rollback();
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(generatedKeys, pstmt, conn);
		}
	}

	public List<UploadedFile> retrieve() throws SQLException {
		List<UploadedFile> list = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select upfi_id, upfi_name, upfi_file_upload_for_date, upfi_file_date, upfi_updated, (select count(orre_id) from original_report where orre_from_file_id=upfi_id) as orre_count from uploaded_file order by upfi_file_upload_for_date desc, upfi_updated desc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UploadedFile upfi = new UploadedFile();
				upfi.setId(rs.getLong("upfi_id"));
				upfi.setName(rs.getString("upfi_name"));
				upfi.setFileUploadForDate(new Date(rs.getTimestamp("upfi_file_upload_for_date").getTime()));
				Timestamp fileDateTimestamp = rs.getTimestamp("upfi_file_date");
				if (fileDateTimestamp != null) {
					upfi.setFileDate(new Date(fileDateTimestamp.getTime()));
				}
				upfi.setUpdated(new Date(rs.getTimestamp("upfi_updated").getTime()));
				upfi.setOriginalReportCount(rs.getInt("orre_count"));
				list.add(upfi);
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(null, pstmt, conn);
		}
		return list;
	}

	public UploadedFile get(Long upfiId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from uploaded_file where upfi_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				UploadedFile upfi = new UploadedFile();
				upfi.setId(rs.getLong("upfi_id"));
				upfi.setName(rs.getString("upfi_name"));
				upfi.setFileUploadForDate(new Date(rs.getTimestamp("upfi_file_upload_for_date").getTime()));
				Timestamp fileDateTimestamp = rs.getTimestamp("upfi_file_date");
				if (fileDateTimestamp != null) {
					upfi.setFileDate(new Date(fileDateTimestamp.getTime()));
				}
				upfi.setUpdated(new Date(rs.getTimestamp("upfi_updated").getTime()));
				return upfi;
			}
			return null;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(null, pstmt, conn);
		}
	}

	public void delete(Long upfiId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			
			String sql = "delete from uploaded_file where upfi_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			pstmt.executeUpdate();
			
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			conn.rollback();
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(null, pstmt, conn);
		}
	}
}
