package com.xingyanping.dao;

import static com.xingyanping.util.DateUtil.isGT;
import static com.xingyanping.util.MatchClientUtil.NOT_MATCH;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.xingyanping.dao.helper.OriginalReportDaoRetrieveMonthHelper;
import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.datamodel.UploadedFile;
import com.xingyanping.util.Config;
import com.xingyanping.util.ConnectionFactory;
import com.xingyanping.util.ExcelUtil;
import com.xingyanping.util.MatchClientUtil;
import com.xingyanping.util.ZipFileContent;
import com.xingyanping.util.ZipFileEntry;

public class OriginalReportDao extends BaseDao {
	private static final UploadedFileDao uploadedFileDao = new UploadedFileDao();
	private static Set<String> blacklistClients = new HashSet<>();
	static {
		String[] blacklistClientsString = Config.get("blacklist.clients").split(",");
		for (String client : blacklistClientsString) {
			blacklistClients.add(client);
		}
	}
	private static final ClientPortRelationshipDao clientPortRelationshipDao = new ClientPortRelationshipDao();
	@SuppressWarnings("resource")
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
			String sql = "insert into original_report (`orre_from_file_id`, `orre_server_request_identifier`, `orre_report_mobile_number`, `orre_report_province`, `orre_report_date`, `orre_reported_number`, `orre_reported_province`, `orre_reported_city`, `orre_server_request_type`, `orre_biz_platform`, `orre_report_object_type`, `orre_report_content`, `orre_year_month`, `orre_dist_content`, `orre_complaint_type`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
				//14. `orre_dist_content`
				pstmt.setString(14, null);
				//15. `orre_complaint_type`
				if (orre.getReportContent() != null && orre.getReportContent().contains("验证")) {
					pstmt.setString(15, "A");
				} else {
					pstmt.setString(15, "未分类");
				}

				try {
					pstmt.executeUpdate();
					if (latestFileDate == null) {
						latestFileDate = orre.getReportDate();
					} else if (isGT(orre.getReportDate(), latestFileDate)) {
						latestFileDate = orre.getReportDate();
					}
				} catch (MySQLIntegrityConstraintViolationException e) {
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
			if (blacklistClients.contains(key)) {
				byte[] blacklistData = generateBlacklistData(clientMap.get(key));
				entryList.add(new ZipFileEntry(date + "-blacklist/" + date + "黑名单-" + key + ".txt", blacklistData));
			}
		}
		entryList.add(new ZipFileEntry(date + "-blacklist/" + date + "黑名单-all.txt", generateBlacklistData(reportList)));
		ZipFileContent zipFileContent = new ZipFileContent();
		zipFileContent.setEntryList(entryList);
		return zipFileContent;
	}
	public ZipFileContent getMonthlyComplaint(Long upfiId) throws SQLException, IOException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnectionFactory.getConnection();
			
			OriginalReportDaoRetrieveMonthHelper monthHelper = new OriginalReportDaoRetrieveMonthHelper(conn);
			List<OriginalReport> reportList = monthHelper.retrieve(upfiId);
			
			Collections.sort(reportList, new OriginalReportComparator(upfiId));
			List<ClientPortRelationship> cprsList = clientPortRelationshipDao.retrieveAll();
			Map<String, List<OriginalReport>> clientMap = new HashMap<>();
			
			for (OriginalReport orre : reportList) {
				putOriginalReportIntoClientMap(orre, clientMap, cprsList);
			}
			
			List<OriginalReport> allButNotMatch = new ArrayList<>();
			List<ZipFileEntry> entryList = new ArrayList<>();
			String yearMonth = new SimpleDateFormat("yyyyMM").format(monthHelper.getProcessedMinMaxDates()[0]);
			for (String key : clientMap.keySet()) {
				if (key.equals(NOT_MATCH)) {
					continue;
				}
				List<OriginalReport> clientData = clientMap.get(key);
				allButNotMatch.addAll(clientData);
				byte[] complaintData = generateComplaintData(clientData, upfiId);
				if (complaintData != null) {
					entryList.add(new ZipFileEntry(yearMonth + "-complaint/" + yearMonth + "投诉数据-" + key + ".xlsx", complaintData));
				}
			}
			
			removeHistoryData(allButNotMatch, upfiId);
			Collections.sort(allButNotMatch, new OriginalReportComparator(upfiId));
			entryList.add(new ZipFileEntry(yearMonth + "-complaint/" + yearMonth + "投诉数据-matched-new.xlsx", generateComplaintDataWithExtraColumns(allButNotMatch, upfiId)));
			
			ZipFileContent zipFileContent = new ZipFileContent();
			zipFileContent.setEntryList(entryList);
			return zipFileContent;
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(rs, pstmt, conn);
		}
	}

	private byte[] generateComplaintDataWithExtraColumns(List<OriginalReport> list, Long lastFileId) throws IOException {
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		ExcelUtil.writeOriginalReportToExcelWithExtraColumns(list, baout);
		return baout.toByteArray();
	}
	private void removeHistoryData(List<OriginalReport> orreList, Long upfiId) {
		for (Iterator<OriginalReport> iter = orreList.iterator(); iter.hasNext(); ) {
			OriginalReport orre = iter.next();
			if (!orre.getFromFileId().equals(upfiId)) {
				iter.remove();
			}
		}
	}
	private byte[] generateComplaintData(List<OriginalReport> list, Long lastFileId) throws IOException {
		boolean anyMatch = list.stream().anyMatch((e)->e.getFromFileId() == lastFileId);
		if (!anyMatch) {
			return null;
		}
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		ExcelUtil.writeOriginalReportToExcel(list, baout, lastFileId);
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
	private class OriginalReportComparator implements Comparator<OriginalReport> {
		private Long upfiId;
		OriginalReportComparator(Long upfiId) {
			this.upfiId = upfiId;
		}
		@Override
		public int compare(OriginalReport o1, OriginalReport o2) {
			Long fileId1 = o1.getFromFileId();
			Long fileId2 = o2.getFromFileId();
			if (fileId1 == upfiId && fileId2 != upfiId) {
				return 1;
			} else if (fileId1 != upfiId && fileId2 == upfiId) {
				return -1;
			} else {
				return o1.getReportDate().compareTo(o2.getReportDate());
			}
		}
		
	}
	public void updateDistContent(Long id, String distContent) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "update original_report set orre_dist_content=? where orre_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, distContent);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(null, pstmt, conn);
		}
	}
	public void updateComplaintType(Long id, String complaintType) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ConnectionFactory.getConnection();
			String sql = "update original_report set orre_complaint_type=? where orre_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, complaintType);
			pstmt.setLong(2, id);
			pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		} finally {
			ConnectionFactory.close(null, pstmt, conn);
		}
	}

}
