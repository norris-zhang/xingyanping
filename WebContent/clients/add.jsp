<%@page import="java.util.Date"%>
<%@page import="com.xingyanping.util.DateUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
String port = request.getParameter("port");
if (port == null || port.trim().length() == 0){
	port = "";
}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div>Edit</div>
<hr/>
<div>
<form action="${ctx}/client/add" method="post">
<label for="port">端口号</label><input type="text" name="port" id="port" value="<%=port%>"/><br/>
<label for="effectiveDate">起始日期</label><input type="date" name="effectiveDate" id="effectiveDate" value="<%=DateUtil.formatDate(new Date())%>"/><br/>
<label for="expiringDate">结束日期</label><input type="date" name="expiringDate" id="expiringDate" value=""/><br/>
<label for="companyName">所属公司</label><input type="text" name="companyName" id="companyName" value=""/><br/>
<label for="companyShortName">简称</label><input type="text" name="companyShortName" id="companyShortName" value=""/><br/>
<label for="client">所属客户</label><input type="text" name="client" id="client" value=""/><br/>
<button type="submit">确定</button>
</form>
</div>
</body>
</html>