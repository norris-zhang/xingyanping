package com.xingyanping.web.stat;

import static com.xingyanping.util.DateUtil.getMonth;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.StatByChannelDao;
import com.xingyanping.dao.StatByChannelDto;
import com.xingyanping.dao.StatMonthListDao;

/**
 * Servlet implementation class StatByChannelServlet
 */
@WebServlet("/stat/bychannel")
public class StatByChannelServlet extends BaseStatServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StatByChannelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Date monthDate = parseMonth(request.getParameter("m"));
			
			StatMonthListDao monthDao = new StatMonthListDao();
			List<Date> monthList = monthDao.getMonthList();
			StatMonthListVo monthListVo = new StatMonthListVo(monthList);
			request.setAttribute("monthListVo", monthListVo);
			
			if (monthDate == null && monthList != null && monthList.size() > 0) {
				monthDate = monthList.get(0);
			}
			
			if (monthDate != null) {
				request.setAttribute("month", getMonth(monthDate));
				StatByChannelDto dto = new StatByChannelDao().execute(monthDate);
				request.setAttribute("dto", dto);
			}
			
			request.getRequestDispatcher("/stat/bychannel.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
