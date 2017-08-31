<%@page import="com.xingyanping.dao.UploadedFileDao"%>
<%@page import="com.xingyanping.datamodel.UploadedFile"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

List<UploadedFile> uploadedFiles = new UploadedFileDao().retrieve();

%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:include page="/segments/header.jsp"></jsp:include>
<hr/>
<div>
	<form action="${ctx}/upload" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>日期：</td>
				<td><input type="date" name="fileUploadForDate" value="<%=today%>"/></td>
			</tr>
			<tr>
				<td>文件：</td>
				<td><input type="file" name="file" /></td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="submit">确认</button>
				</td>
			</tr>
		</table>
	</form>
</div>
<hr />
<table border="1">
<thead>
	<tr>
		<th>文件名</th>
		<th>文件日期</th>
		<th>上传时间</th>
		<th>有效数据</th>
		<th></th>
		<th></th>
		<th></th>
	</tr>
</thead>
<tbody>
	<% for (UploadedFile file : uploadedFiles) { %>
		<% SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); %>
		<% SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
	<tr>
		<td><%= file.getName() %></td>
		<td><%= dateFormatter.format(file.getFileUploadForDate()) %></td>
		<td><%= dateTimeFormatter.format(file.getUpdated()) %></td>
		<td><%= file.getOriginalReportCount() %></td>
		<td>
			<a href="${ctx}/dl/blacklist?id=<%= file.getId() %>">下载黑名单</a><br/>
			<a href="${ctx}/dl/monthlycomplaint?id=<%= file.getId() %>">下载当月投诉数据</a><br/>
			<a href="${ctx}/file/delete?id=<%= file.getId() %>" onclick="return confirmDelete()">删除文件</a>
		</td>
		<td>
			<a href="${ctx}/orre/view?id=<%= file.getId() %>&r=d">查看当日数据</a><br/>
			<a href="${ctx}/orre/view?id=<%= file.getId() %>&r=m">查看当月数据</a>
		</td>
		<td>
			<a href="${ctx}/dl/exportmonth?id=<%= file.getId() %>">导出当月数据</a>
		</td>
	</tr>
	<% } %>
</tbody>
</table>
<script type="text/javascript">
function confirmDelete() {
	return confirm("文件中的内容也将从数据库中删除");
}

</script>
</body>
</html>