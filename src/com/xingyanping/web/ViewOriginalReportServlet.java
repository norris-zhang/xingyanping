package com.xingyanping.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.ClientPortRelationshipDao;
import com.xingyanping.dao.OriginalReportViewCondition;
import com.xingyanping.dao.OriginalReportViewDao;
import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.datamodel.OriginalReport;
import com.xingyanping.util.MatchClientUtil;

/**
 * Servlet implementation class ViewOriginalReportServlet
 */
@WebServlet("/orre/view")
public class ViewOriginalReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOriginalReportServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long id = Long.valueOf(request.getParameter("id"));
			String range = request.getParameter("r");
			List<OriginalReport> orreList = new OriginalReportViewDao().retrieve(new OriginalReportViewCondition(id, range));
			List<ClientPortRelationship> cprsList = new ClientPortRelationshipDao().retrieveAll();
			for (OriginalReport orre : orreList) {
				MatchClientUtil.matchClient(orre, cprsList);
			}
			request.setAttribute("orreList", orreList);
			request.getRequestDispatcher("/data/maint.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
