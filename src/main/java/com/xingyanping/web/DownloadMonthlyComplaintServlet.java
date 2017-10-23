package com.xingyanping.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.OriginalReportDao;
import com.xingyanping.util.ZipFileContent;
import com.xingyanping.util.ZipFileEntry;
import com.xingyanping.util.ZipUtil;

/**
 * Servlet implementation class DownloadMonthlyComplaintServlet
 */
@WebServlet("/dl/monthlycomplaint")
public class DownloadMonthlyComplaintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final OriginalReportDao originalReportDao = new OriginalReportDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadMonthlyComplaintServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long upfiId = Long.valueOf(request.getParameter("id"));
			ZipFileContent zipFileContent = originalReportDao.getMonthlyComplaint(upfiId);
			if (isEmptyEntryList(zipFileContent)) {
				zipFileContent.getEntryList().add(new ZipFileEntry("monthlycomplaint/no-update", new byte[0]));
			}
			byte[] data = ZipUtil.zip(zipFileContent);

			response.setContentType("application/zip");
			response.setHeader("Content-disposition", "attachment; filename=monthlycomplaint.zip");
			
			ServletOutputStream out = response.getOutputStream();
			out.write(data);
			out.flush();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private boolean isEmptyEntryList(ZipFileContent zipFileContent) {
		return zipFileContent == null
				|| zipFileContent.getEntryList() == null
				|| zipFileContent.getEntryList().size() == 0;
	}

}
