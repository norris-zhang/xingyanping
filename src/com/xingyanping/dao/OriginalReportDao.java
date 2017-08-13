package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.isGT;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.datamodel.UploadedFile;
import com.xingyanping.util.ConnectionFactory;
import com.xingyanping.util.ExcelUtil;
import com.xingyanping.util.MatchClientUtil;
import com.xingyanping.util.ZipFileContent;
import com.xingyanping.util.ZipFileEntry;

public class OriginalReportDao extends BaseDao {
	private static final UploadedFileDao uploadedFileDao = new UploadedFileDao();
	private static final ClientPortRelationshipDao clientPortRelationshipDao = new ClientPortRelationshipDao();
	public void importFileData(Long upfiId) throws Exception {
		UploadedFile upfi = uploadedFileDao.get(upfiId);
		List<OriginalReport> orreList = ExcelUtil.readOriginalReportFromExcel(upfiId, upfi.getName());
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Date latestFileDate = null;
			conn = ConnectionFactory.getConnection();
			conn.setAutoCommit(false);
			String sql = "insert into original_report (`orre_from_file_id`, `orre_server_request_identifier`, `orre_report_mobile_number`, `orre_report_province`, `orre_report_date`, `orre_reported_number`, `orre_reported_province`, `orre_reported_city`, `orre_server_request_type`, `orre_biz_platform`, `orre_report_object_type`, `orre_report_content`, `orre_year_month`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			for (OriginalReport orre : orreList) {
				//1. `orre_from_file_id`,
				pstmt.setLong(1, orre.getFromFileId());
				//2. `orre_server_request_identifier`,
				pstmt.setString(2, orre.getServerRequestIdentifier());
				//3. `orre_report_mobile_number`,
				pstmt.setString(3, orre.getReportMobileNumber());
				//4. `orre_report_province`,
				pstmt.setString(4, orre.getReportProvince());
				//5. `orre_report_date`,
				pstmt.setTimestamp(5, orre.getReportDate() == null ? null : new Timestamp(orre.getReportDate().getTime()));
				//6. `orre_reported_number`,
				pstmt.setString(6, orre.getReportedNumber());
				//7. `orre_reported_province`,
				pstmt.setString(7, orre.getReportedProvince());
				//8. `orre_reported_city`,
				pstmt.setString(8, orre.getReportedCity());
				//9. `orre_server_request_type`,
				pstmt.setString(9, orre.getServerRequestType());
				//10. `orre_biz_platform`,
				pstmt.setString(10, orre.getBizPlatform());
				//11. `orre_report_object_type`,
				pstmt.setString(11, orre.getReportObjectType());
				//12. `orre_report_content`,
				pstmt.setString(12, orre.getReportContent());
				//13. `orre_year_month`
				pstmt.setString(13, orre.getYearMonth());

				try {
					pstmt.executeUpdate();
					if (latestFileDate == null) {
						latestFileDate = orre.getReportDate();
					} else if (isGT(orre.getReportDate(), latestFileDate)) {
						latestFileDate = orre.getReportDate();
					}
				} catch (MySQLIntegrityConstraintViolationException e) {
					System.err.println(e.getMessage());
					if (!e.getMessage().contains("orre_server_request_identifier_UNIQUE")) {
						throw e;
					}
				}
				
			}
			pstmt.close();
			pstmt = null;
			
			if (latestFileDate != null) {
				sql = "update uploaded_file set upfi_file_date=? where upfi_id=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setTimestamp(1, new Timestamp(latestFileDate.getTime()));
				pstmt.setLong(2, upfiId);
				pstmt.executeUpdate();
				
			}
			
			conn.commit();
		} catch (ClassNotFoundException | SQLException e) {
			conn.rollback();
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
	public ZipFileContent getBlacklist(Long upfiId) throws SQLException {
		UploadedFile upfi = uploadedFileDao.get(upfiId);
		List<OriginalReport> reportList = retrieve(upfiId);
		List<ClientPortRelationship> cprsList = clientPortRelationshipDao.retrieveAll();
		Map<String, List<OriginalReport>> clientMap = new HashMap<>();
		
		for (OriginalReport orre : reportList) {
			putOriginalReportIntoClientMap(orre, clientMap, cprsList);
		}
		
		List<ZipFileEntry> entryList = new ArrayList<>();
		String date = new SimpleDateFormat("yyyyMMdd").format(upfi.getFileUploadForDate());
		for (String key : clientMap.keySet()) {
			byte[] blacklistData = generateBlacklistData(clientMap.get(key));
			entryList.add(new ZipFileEntry(date + "-blacklist/" + date + "黑名单-" + key + ".txt", blacklistData));
		}
		entryList.add(new ZipFileEntry(date + "-blacklist/" + date + "黑名单-all.txt", generateBlacklistData(reportList)));
		ZipFileContent zipFileContent = new ZipFileContent();
		zipFileContent.setEntryList(entryList);
		return zipFileContent;
	}
	public ZipFileContent getMonthlyComplaint(Long upfiId) throws SQLException, IOException {
		Date[] minMaxDates = retrieveMinMaxDate(upfiId);
		Date[] processMinMaxDates = decideMonth(minMaxDates);
		List<OriginalReport> reportList = retrieve(processMinMaxDates);
		List<ClientPortRelationship> cprsList = clientPortRelationshipDao.retrieveAll();
		Map<String, List<OriginalReport>> clientMap = new HashMap<>();
		
		for (OriginalReport orre : reportList) {
			putOriginalReportIntoClientMap(orre, clientMap, cprsList);
		}
		
		List<ZipFileEntry> entryList = new ArrayList<>();
		for (String key : clientMap.keySet()) {
			byte[] complaintData = generateComplaintData(clientMap.get(key));
			String yearMonth = new SimpleDateFormat("yyyyMM").format(processMinMaxDates[0]);
			entryList.add(new ZipFileEntry(yearMonth + "-complaint/" + yearMonth + "投诉数据-" + key + ".xlsx", complaintData));
		}
		ZipFileContent zipFileContent = new ZipFileContent();
		zipFileContent.setEntryList(entryList);
		return zipFileContent;
	}
	private List<OriginalReport> retrieve(Date[] processMinMaxDates) throws SQLException {
		List<OriginalReport> orreList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from original_report where orre_report_date>=? and orre_report_date<=? order by orre_report_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(processMinMaxDates[0].getTime()));
			pstmt.setTimestamp(2, new Timestamp(processMinMaxDates[1].getTime()));
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OriginalReport orre = new OriginalReport();
				orre.setId(rs.getLong("orre_id"));
				orre.setFromFileId(rs.getLong("orre_from_file_id"));
				orre.setServerRequestIdentifier(rs.getString("orre_server_request_identifier"));
				orre.setReportMobileNumber(rs.getString("orre_report_mobile_number"));
				orre.setReportProvince(rs.getString("orre_report_province"));
				Timestamp reportDate = rs.getTimestamp("orre_report_date");
				if (reportDate != null) {
					orre.setReportDate(new Date(reportDate.getTime()));
				}
				orre.setReportedNumber(rs.getString("orre_reported_number"));
				orre.setReportedProvince(rs.getString("orre_reported_province"));
				orre.setReportedCity(rs.getString("orre_reported_city"));
				orre.setServerRequestType(rs.getString("orre_server_request_type"));
				orre.setBizPlatform(rs.getString("orre_biz_platform"));
				orre.setReportObjectType(rs.getString("orre_report_object_type"));
				orre.setReportContent(rs.getString("orre_report_content"));
				orre.setYearMonth(rs.getString("orre_year_month"));
				orre.setUpdated(new Date(rs.getTimestamp("orre_updated").getTime()));
				
				orreList.add(orre);
			}
			
			return orreList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
	private Date[] decideMonth(Date[] minMaxDates) {
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
	private Date[] monthTill(Calendar c) {
		Date max = c.getTime();
		
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date min = c.getTime();
		
		return new Date[]{min, max};
	}
	private Date[] wholeMonth(Calendar c) {
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date min = c.getTime();
		
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.MILLISECOND, -1);
		Date max = c.getTime();
		
		return new Date[]{min, max};
	}
	private Date[] retrieveMinMaxDate(Long upfiId) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
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
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
	private byte[] generateComplaintData(List<OriginalReport> list) throws IOException {
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		ExcelUtil.writeOriginalReportToExcel(list, baout);
		return baout.toByteArray();
	}
	private byte[] generateBlacklistData(List<OriginalReport> orreList) {
		Set<String> existingSet = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		for (OriginalReport orre : orreList) {
			String mobile = orre.getReportMobileNumber();
			if (existingSet.contains(mobile)) {
				continue;
			}
			sb.append(mobile);
			sb.append("\n");
			existingSet.add(mobile);
		}
		return sb.toString().getBytes();
	}
	private void putOriginalReportIntoClientMap(OriginalReport orre, Map<String, List<OriginalReport>> clientMap, List<ClientPortRelationship> cprsList) {
		String client = MatchClientUtil.matchClient(orre, cprsList);
		if (!clientMap.containsKey(client)) {
			clientMap.put(client, new ArrayList<>());
		}
		clientMap.get(client).add(orre);
	}
	private List<OriginalReport> retrieve(Long upfiId) throws SQLException {
		List<OriginalReport> orreList = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "select * from original_report where orre_from_file_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, upfiId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OriginalReport orre = new OriginalReport();
				orre.setId(rs.getLong("orre_id"));
				orre.setFromFileId(rs.getLong("orre_from_file_id"));
				orre.setServerRequestIdentifier(rs.getString("orre_server_request_identifier"));
				orre.setReportMobileNumber(rs.getString("orre_report_mobile_number"));
				orre.setReportProvince(rs.getString("orre_report_province"));
				Timestamp reportDate = rs.getTimestamp("orre_report_date");
				if (reportDate != null) {
					orre.setReportDate(new Date(reportDate.getTime()));
				}
				orre.setReportedNumber(rs.getString("orre_reported_number"));
				orre.setReportedProvince(rs.getString("orre_reported_province"));
				orre.setReportedCity(rs.getString("orre_reported_city"));
				orre.setServerRequestType(rs.getString("orre_server_request_type"));
				orre.setBizPlatform(rs.getString("orre_biz_platform"));
				orre.setReportObjectType(rs.getString("orre_report_object_type"));
				orre.setReportContent(rs.getString("orre_report_content"));
				orre.setYearMonth(rs.getString("orre_year_month"));
				orre.setUpdated(new Date(rs.getTimestamp("orre_updated").getTime()));
				
				orreList.add(orre);
			}
			
			return orreList;
		} catch (ClassNotFoundException | SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}
	
}
