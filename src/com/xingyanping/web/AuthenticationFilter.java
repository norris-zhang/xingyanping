package com.xingyanping.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {
	private static final String[] exceptions = {
			"/login.jsp",
			"/login"
	};
    /**
     * Default constructor. 
     */
    public AuthenticationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// place your code here
		HttpServletRequest req = (HttpServletRequest)request;
		String servletPath = req.getServletPath();
		if (matchesExceptions(servletPath) || userLoggedIn(req)) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse res = (HttpServletResponse)response;
			res.sendRedirect(req.getContextPath() + "/login.jsp");
		}
	}
	
	private boolean matchesExceptions(String servletPath) {
		for (String exception : exceptions) {
			if (servletPath.matches(exception)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean userLoggedIn(HttpServletRequest request) {
		return new SessionUser(request.getSession()).isLoggedIn();
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
