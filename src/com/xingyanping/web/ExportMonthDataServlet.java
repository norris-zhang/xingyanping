package com.xingyanping.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.ClientPortRelationshipDao;
import com.xingyanping.dao.OriginalReportViewCondition;
import com.xingyanping.dao.OriginalReportViewDao;
import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.ClientFiller;
import com.xingyanping.util.ExportMonthDataExcelWriter;

/**
 * Servlet implementation class ExportMonthDataServlet
 */
@WebServlet("/dl/exportmonth")
public class ExportMonthDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExportMonthDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long upfiId = Long.valueOf(request.getParameter("id"));
			List<OriginalReport> orreList = new OriginalReportViewDao().retrieve(new OriginalReportViewCondition(upfiId, "m"));
			List<ClientPortRelationship> cprsList = new ClientPortRelationshipDao().retrieveAll();
			new ClientFiller(orreList, cprsList, true).execute();
			
			ByteArrayOutputStream baout = new ByteArrayOutputStream();
			new ExportMonthDataExcelWriter(orreList).write(baout);
			byte[] data = baout.toByteArray();

			String filename = "monthlycomplaint.xlsx";
			if (orreList != null && orreList.size() > 0) {
				Date reportDate = orreList.get(0).getReportDate();
				filename = new SimpleDateFormat("yyyy-MM").format(reportDate) + "-complaint.xlsx";
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-disposition", "attachment; filename=" + filename);
			
			ServletOutputStream out = response.getOutputStream();
			out.write(data);
			out.flush();
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
