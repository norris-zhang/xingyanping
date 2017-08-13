package com.xingyanping.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.xingyanping.dao.OriginalReportDao;
import com.xingyanping.dao.UploadedFileDao;
import com.xingyanping.datamodel.UploadedFile;
import com.xingyanping.util.DateUtil;
import com.xingyanping.util.PathUtil;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final DiskFileItemFactory factory = new DiskFileItemFactory();
	private static final UploadedFileDao uploadedFileDao = new UploadedFileDao();
	private static final OriginalReportDao originalReportDao = new OriginalReportDao();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			UploadedFile upfi = new UploadedFile();
			FileItem fileItem = null;
			List<FileItem> items = new ServletFileUpload(factory).parseRequest(request);
			Iterator<FileItem> iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem item = iterator.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString();
					if ("fileUploadForDate".equals(name)) {
						upfi.setFileUploadForDate(DateUtil.parseDate(value));
					}
				} else {
					fileItem = item;
					String fieldName = item.getFieldName();
					String fileName = item.getName();
//					String contentType = item.getContentType();
					if ("file".equals(fieldName)) {
						upfi.setName(fileName);
					}
				}
			}
			uploadedFileDao.save(upfi);
			fileItem.write(new File(PathUtil.uploadFilePath(upfi.getId(), upfi.getName())));
			originalReportDao.importFileData(upfi.getId());
			response.sendRedirect(request.getContextPath() + "/");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

}
