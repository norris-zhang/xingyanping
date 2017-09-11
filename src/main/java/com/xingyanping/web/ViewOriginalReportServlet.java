package com.xingyanping.web;

import static com.xingyanping.util.DateUtil.isSameDate;

import java.io.IOException;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import com.xingyanping.util.ClientFiller;

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
			new ClientFiller(orreList, cprsList, true).execute();
			
			sortByDateClient(orreList);

			request.setAttribute("orreList", orreList);
			request.getRequestDispatcher("/data/maint.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void sortByDateClient(List<OriginalReport> orreList) {
		final Collator collator = Collator.getInstance(Locale.SIMPLIFIED_CHINESE);
		orreList.sort((o1, o2) -> {
			Date reportDate1 = o1.getReportDate();
			Date reportDate2 = o2.getReportDate();
			if (!isSameDate(reportDate1, reportDate2)) {
				return reportDate1.compareTo(reportDate2);
			}
			
			ClientPortRelationship cprs1 = o1.getMatchesClientPortRelationship();
			ClientPortRelationship cprs2 = o2.getMatchesClientPortRelationship();
			if (cprs1 == null) {
				return 1;
			}
			if (cprs2 == null) {
				return -1;
			}
			CollationKey key1 = collator.getCollationKey(cprs1.getClient());
			CollationKey key2 = collator.getCollationKey(cprs2.getClient());
			return key1.compareTo(key2);
		});
	}

}
