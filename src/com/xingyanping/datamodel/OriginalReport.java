package com.xingyanping.datamodel;

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
	
	private UploadedFile fromFile;
	
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
	
}
