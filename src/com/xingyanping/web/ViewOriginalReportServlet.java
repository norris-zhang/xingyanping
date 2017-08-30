package com.xingyanping.web;

import static com.xingyanping.util.MatchClientUtil.NOT_MATCH;
import static com.xingyanping.util.MatchClientUtil.matchClient;

import java.io.IOException;
import java.util.Iterator;
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
			for (Iterator<OriginalReport> iter = orreList.iterator(); iter.hasNext();) {
				OriginalReport orre = iter.next();
				String matchClient = matchClient(orre, cprsList);
				if (NOT_MATCH.equals(matchClient)) {
					iter.remove();
				}
			}
			request.setAttribute("orreList", orreList);
			request.getRequestDispatcher("/data/maint.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
