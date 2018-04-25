package com.xingyanping.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class OriginalReport extends BaseDatamodel {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long fromFileId;
	private String serverRequestIdentifier;
	private String reportMobileNumber;
	private String reportProvince;
	private Date reportDate;
	private String reportedNumber;
	private String reportedProvince;
	private String reportedCity;
	private String serverRequestType;
	private String bizPlatform;
	private String reportObjectType;
	private String reportContent;
	private String yearMonth;
	private Date updated;
	private String distContent;
	private String complaintType;
	private String reportSource;
	
	private UploadedFile fromFile;
	
	private ClientPortRelationship matchesClientPortRelationship;
	
	public OriginalReport populate(ResultSet rs) throws SQLException {
		this.setId(rs.getLong("orre_id"));
		this.setFromFileId(rs.getLong("orre_from_file_id"));
		this.setServerRequestIdentifier(rs.getString("orre_server_request_identifier"));
		this.setReportMobileNumber(rs.getString("orre_report_mobile_number"));
		this.setReportProvince(rs.getString("orre_report_province"));
		Timestamp reportDate = rs.getTimestamp("orre_report_date");
		if (reportDate != null) {
			this.setReportDate(new Date(reportDate.getTime()));
		}
		this.setReportedNumber(rs.getString("orre_reported_number"));
		this.setReportedProvince(rs.getString("orre_reported_province"));
		this.setReportedCity(rs.getString("orre_reported_city"));
		this.setServerRequestType(rs.getString("orre_server_request_type"));
		this.setBizPlatform(rs.getString("orre_biz_platform"));
		this.setReportObjectType(rs.getString("orre_report_object_type"));
		this.setReportContent(rs.getString("orre_report_content"));
		this.setYearMonth(rs.getString("orre_year_month"));
		this.setUpdated(new Date(rs.getTimestamp("orre_updated").getTime()));
		this.setDistContent(rs.getString("orre_dist_content"));
		this.setComplaintType(rs.getString("orre_complaint_type"));
		this.setReportSource(rs.getString("orre_report_source"));

		return this;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFromFileId() {
		return fromFileId;
	}
	public void setFromFileId(Long fromFileId) {
		this.fromFileId = fromFileId;
	}
	public String getServerRequestIdentifier() {
		return serverRequestIdentifier;
	}
	public void setServerRequestIdentifier(String serverRequestIdentifier) {
		this.serverRequestIdentifier = serverRequestIdentifier;
	}
	public String getReportMobileNumber() {
		return reportMobileNumber;
	}
	public void setReportMobileNumber(String reportMobileNumber) {
		this.reportMobileNumber = reportMobileNumber;
	}
	public String getReportProvince() {
		return reportProvince;
	}
	public void setReportProvince(String reportProvince) {
		this.reportProvince = reportProvince;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportedNumber() {
		return reportedNumber;
	}
	public void setReportedNumber(String reportedNumber) {
		this.reportedNumber = reportedNumber;
	}
	public String getReportedProvince() {
		return reportedProvince;
	}
	public void setReportedProvince(String reportedProvince) {
		this.reportedProvince = reportedProvince;
	}
	public String getReportedCity() {
		return reportedCity;
	}
	public void setReportedCity(String reportedCity) {
		this.reportedCity = reportedCity;
	}
	public String getServerRequestType() {
		return serverRequestType;
	}
	public void setServerRequestType(String serverRequestType) {
		this.serverRequestType = serverRequestType;
	}
	public String getBizPlatform() {
		return bizPlatform;
	}
	public void setBizPlatform(String bizPlatform) {
		this.bizPlatform = bizPlatform;
	}
	public String getReportObjectType() {
		return reportObjectType;
	}
	public void setReportObjectType(String reportObjectType) {
		this.reportObjectType = reportObjectType;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public UploadedFile getFromFile() {
		return fromFile;
	}
	public void setFromFile(UploadedFile fromFile) {
		this.fromFile = fromFile;
	}
	public ClientPortRelationship getMatchesClientPortRelationship() {
		return matchesClientPortRelationship;
	}
	public void setMatchesClientPortRelationship(ClientPortRelationship matchesClientPortRelationship) {
		this.matchesClientPortRelationship = matchesClientPortRelationship;
	}
	public String getDistContent() {
		return distContent;
	}
	public void setDistContent(String distContent) {
		this.distContent = distContent;
	}
	public String getComplaintType() {
		return complaintType;
	}
	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}
	public String getReportSource() {
		return reportSource;
	}
	public void setReportSource(String reportSource) {
		this.reportSource = reportSource;
	}

}
