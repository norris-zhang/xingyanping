<%@page import="com.xingyanping.dao.StatByClientDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
request.setAttribute("ctx", request.getContextPath());
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
<jsp:include page="/stat/segments/statnav.jsp"></jsp:include>
<hr/>
<jsp:include page="/stat/segments/statmonth.jsp">
	<jsp:param value="byclient" name="statType"/>
</jsp:include>
<br/>
<div>
<table border="1">
<thead>
	<tr>
		<th>${month}月</th>
		<th>累计</th>
		<% StatByClientDto dto = (StatByClientDto)request.getAttribute("dto"); %>
		<% for (int i = dto.getMaxDate(); i >= dto.getMinDate(); i--) { %>
		<th><%= i %>日</th>
		<% } %>
	</tr>
</thead>
<tbody>
	<c:forEach items="${dto.clientStatMap}" var="clientStat">
		<tr>
			<td>${clientStat.key }</td>
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