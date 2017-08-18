package com.xingyanping.web;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

public class SessionUser {
	private static final String SESSION_USER_KEY = "sessionUser";
	private HttpSession session;
	public SessionUser(HttpSession session) {
		this.session = session;
	}

	public boolean isLoggedIn() {
		Object sessionUser = session.getAttribute(SESSION_USER_KEY);
		return sessionUser != null && sessionUser instanceof User;
	}

	public void save(String username) {
		User user = new User();
		user.setUsername(username);
		this.session.setAttribute(SESSION_USER_KEY, user);
	}

	public static class User implements Serializable {
		private static final long serialVersionUID = 1L;
		private String username;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
	}
}
