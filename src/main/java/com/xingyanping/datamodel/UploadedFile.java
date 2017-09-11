package com.xingyanping.datamodel;

import java.util.Date;
import java.util.List;

public class UploadedFile extends BaseDatamodel {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Date fileUploadForDate;
	private Date fileDate;
	private Date updated;
	private int originalReportCount;
	
	private List<OriginalReport> originalReportList;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getFileDate() {
		return fileDate;
	}
	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public List<OriginalReport> getOriginalReportList() {
		return originalReportList;
	}
	public void setOriginalReportList(List<OriginalReport> originalReportList) {
		this.originalReportList = originalReportList;
	}
	public Date getFileUploadForDate() {
		return fileUploadForDate;
	}
	public void setFileUploadForDate(Date fileUploadForDate) {
		this.fileUploadForDate = fileUploadForDate;
	}
	public int getOriginalReportCount() {
		return originalReportCount;
	}
	public void setOriginalReportCount(int originalReportCount) {
		this.originalReportCount = originalReportCount;
	}
	
}
