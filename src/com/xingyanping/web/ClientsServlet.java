package com.xingyanping.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.ClientPortRelationshipDao;
import com.xingyanping.datamodel.ClientPortRelationship;

/**
 * Servlet implementation class ClientsServlet
 */
@WebServlet("/clients")
public class ClientsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final ClientPortRelationshipDao dao = new ClientPortRelationshipDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<ClientPortRelationship> clientList = dao.retrieveAll();
			Map<String, List<ClientPortRelationship>> clientMap = mapClientList(clientList);
			request.setAttribute("clientMap", clientMap);
			request.getRequestDispatcher("/clients/maint.jsp").forward(request, response);
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	private Map<String, List<ClientPortRelationship>> mapClientList(List<ClientPortRelationship> clientList) {
		Map<String, List<ClientPortRelationship>> map = new HashMap<>();
		clientList.stream().forEach((e) -> {
			List<ClientPortRelationship> list = map.get(e.getPort());
			if (list == null) {
				list = new ArrayList<>();
				map.put(e.getPort(), list);
			}
			list.add(e);
		});
		return map;
	}

}
