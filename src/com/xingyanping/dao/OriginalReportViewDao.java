package com.xingyanping.dao;

import static com.xingyanping.dao.OriginalReportViewCondition.RangeType.DAILY;
import static com.xingyanping.dao.OriginalReportViewCondition.RangeType.MONTHLY;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.xingyanping.dao.helper.OriginalReportDaoHelper;
import com.xingyanping.dao.helper.OriginalReportDaoRetrieveMonthHelper;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.ConnectionFactory;

public class OriginalReportViewDao extends BaseDao {
	private Connection conn;
	private OriginalReportDaoHelper helper;
	private OriginalReportDaoRetrieveMonthHelper monthHelper;
	public OriginalReportViewDao() throws SQLException {
		try {
			this.conn = ConnectionFactory.getConnection();
			this.helper = new OriginalReportDaoHelper(conn);
			this.monthHelper = new OriginalReportDaoRetrieveMonthHelper(conn);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}
	public List<OriginalReport> retrieve(OriginalReportViewCondition condition) throws SQLException {
		try {
			if (condition.getRangeType() == DAILY) {
				return retrieveFilewide(condition);
			} else if (condition.getRangeType() == MONTHLY) {
				return retrieveMonthwide(condition);
			} else {
				return new ArrayList<>();
			}
		} finally {
			ConnectionFactory.close(null, null, conn);
		}
	}
	private List<OriginalReport> retrieveFilewide(OriginalReportViewCondition condition) throws SQLException {
		return helper.getByFileId(condition.getUpfiId());
	}
	private List<OriginalReport> retrieveMonthwide(OriginalReportViewCondition condition) throws SQLException {
		return monthHelper.retrieve(condition.getUpfiId());
	}
	
}
