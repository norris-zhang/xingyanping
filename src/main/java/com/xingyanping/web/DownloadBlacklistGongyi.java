package com.xingyanping.web;

import static com.xingyanping.util.DateUtil.formatDate;
import static java.net.URLEncoder.encode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.ReportNumberDao;

/**
 * Servlet implementation class DownloadBlacklistGongyi
 */
@WebServlet("/dl/blacklist/gongyi")
public class DownloadBlacklistGongyi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadBlacklistGongyi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long upfiId = Long.valueOf(request.getParameter("id"));
			ReportNumberDao reportNumberDao = new ReportNumberDao();
			List<String> reportPhoneNumbers = reportNumberDao.getBlacklistGongyi(upfiId);

			String joinedNumbers = reportPhoneNumbers.stream().collect(Collectors.joining("\r\n"));
			byte[] data = joinedNumbers.getBytes();

			response.setContentType("text/plain");
			String downloadFileName = "公益通道黑名单" + formatDate(reportNumberDao.getFileUploadedForDate(), "yyyyMMdd") + ".txt";
			downloadFileName = encode(downloadFileName, "UTF-8");
			response.setHeader("Content-disposition", "attachment; filename*='utf8''" + downloadFileName);
			
			ServletOutputStream out = response.getOutputStream();
			out.write(data);
			out.flush();
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

}
