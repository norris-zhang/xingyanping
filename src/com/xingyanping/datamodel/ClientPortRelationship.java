package com.xingyanping.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ClientPortRelationship extends BaseDatamodel {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String port;
	private String companyName;
	private String companyShortName;
	private String client;
	private Date updated;
	private Date effectiveDate;
	private Date expiringDate;
	private int order;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyShortName() {
		return companyShortName;
	}
	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getExpiringDate() {
		return expiringDate;
	}
	public void setExpiringDate(Date expiringDate) {
		this.expiringDate = expiringDate;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public ClientPortRelationship populate(ResultSet rs) throws SQLException {
		setId(rs.getLong("cprs_id"));
		setPort(rs.getString("cprs_port"));
		setCompanyName(rs.getString("cprs_company_name"));
		setCompanyShortName(rs.getString("cprs_company_short_name"));
		setClient(rs.getString("cprs_client"));
		setUpdated(new Date(rs.getTimestamp("cprs_updated").getTime()));
		setEffectiveDate(new Date(rs.getTimestamp("cprs_effective_date").getTime()));
		Timestamp expiringDate = rs.getTimestamp("cprs_expiring_date");
		if (expiringDate != null) {
			setExpiringDate(new Date(expiringDate.getTime()));
		}
		setOrder(rs.getInt("cprs_order"));
		return this;
	}
	
}
