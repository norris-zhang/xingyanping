<%@page import="com.xingyanping.dao.StatByChannelDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
request.setAttribute("ctx", request.getContextPath());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css" />
</head>
<body>
<jsp:include page="/segments/header.jsp"></jsp:include>
<hr/>
<jsp:include page="/stat/segments/statnav.jsp">
	<jsp:param value="bychannel" name="activeLink"/>
</jsp:include>
<hr/>
<jsp:include page="/stat/segments/statmonth.jsp">
	<jsp:param value="bychannel" name="statType"/>
</jsp:include>
<br/>
<div>
<table border="1">
<thead>
	<tr>
		<th>简称</th>
		<th>端口号</th>
		<th>累计</th>
		<% StatByChannelDto dto = (StatByChannelDto)request.getAttribute("dto"); %>
		<% for (int i = dto.getMaxDate(); i >= dto.getMinDate(); i--) { %>
		<th><%= i %>日</th>
		<% } %>
	</tr>
</thead>
<tbody>
	<c:forEach items="${dto.clientStatMap}" var="clientStat">
		<tr>
			<td>${fn:split(clientStat.key, ':')[0]}</td>
			<td>${fn:split(clientStat.key, ':')[1]}</td>
			<c:forEach items="${clientStat.value}" var="dateCount">
			<td>${dateCount}</td>
			</c:forEach>
		</tr>
	</c:forEach>
</tbody>
</table>
</div>
</body>
</html>