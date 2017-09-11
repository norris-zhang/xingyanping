package com.xingyanping.web.stat;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;

public class BaseStatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Date parseMonth(String monthParam) {
		if (monthParam != null) {
			try {
				return new SimpleDateFormat("yyyyMM").parse(monthParam);
			} catch (Exception e) {}
		}
		return null;
	}
}
