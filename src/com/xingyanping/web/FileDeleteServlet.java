package com.xingyanping.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xingyanping.dao.UploadedFileDao;

/**
 * Servlet implementation class FileDeleteServlet
 */
@WebServlet("/file/delete")
public class FileDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final UploadedFileDao uploadedFileDao = new UploadedFileDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileDeleteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Long fileId = Long.valueOf(request.getParameter("id"));
			uploadedFileDao.delete(fileId);
			
			response.sendRedirect(request.getContextPath() + "/");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
