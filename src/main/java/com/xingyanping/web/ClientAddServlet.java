package com.xingyanping.web;

import static com.xingyanping.util.DateUtil.parseDate;
import static com.xingyanping.util.DateUtil.parseDateToEnd;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.ClientPortRelationshipDao;
import com.xingyanping.datamodel.ClientPortRelationship;
import com.xingyanping.util.ClientGroupValidationHelper;
import com.xingyanping.util.ValidationError;

/**
 * Servlet implementation class ClientAddServlet
 */
@WebServlet("/client/add")
public class ClientAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ClientPortRelationshipDao dao = new ClientPortRelationshipDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String port = request.getParameter("port");
			Date effectiveDate = parseDate(request.getParameter("effectiveDate"));
			Date expiringDate = parseDateToEnd(request.getParameter("expiringDate"));
			String companyName = request.getParameter("companyName");
			String companyShortName = request.getParameter("companyShortName");
			String client = request.getParameter("client");
			
			List<ClientPortRelationship> clientGroup = dao.getPortGroup(port);

			ClientPortRelationship theClient = new ClientPortRelationship();
			theClient.setPort(port);
			theClient.setEffectiveDate(effectiveDate);
			theClient.setExpiringDate(expiringDate);
			theClient.setCompanyName(companyName);
			theClient.setCompanyShortName(companyShortName);
			theClient.setClient(client);
			
			clientGroup.add(theClient);
			
			ValidationError error = new ClientGroupValidationHelper(clientGroup).validate();
			if (!error.result) {
				request.setAttribute("error", error);
				request.getRequestDispatcher("/error.jsp").forward(request, response);
				return;
			}
			List<ClientPortRelationship> existingShortNameCprsList = dao.retrieveByCompanyShortName(theClient.getCompanyShortName());
			if (existingShortNameCprsList != null && existingShortNameCprsList.size() > 0) {
				theClient.setOrder(existingShortNameCprsList.get(0).getOrder());
			}
			dao.insert(theClient);
			
			response.sendRedirect(request.getContextPath() + "/clients");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
