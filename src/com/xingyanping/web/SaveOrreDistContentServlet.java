package com.xingyanping.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.OriginalReportDao;

/**
 * Servlet implementation class SaveOrreDistContentServlet
 */
@WebServlet("/orre/savedc")
public class SaveOrreDistContentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveOrreDistContentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			String distContent = request.getParameter("distContent");
			Long id = Long.valueOf(request.getParameter("id"));
			OriginalReportDao dao = new OriginalReportDao();
			dao.updateDistContent(id, distContent);
			out.print("{\"code\":\"OK\"}");
		} catch (Exception e) {
			out.println("{\"code\":\"fail\", \"message\":"+e.getMessage()+"}");
		}
	}

}
