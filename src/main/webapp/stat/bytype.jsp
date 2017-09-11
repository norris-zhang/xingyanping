<%@page import="com.xingyanping.dao.StatByTypeDto"%>
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
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css" />
</head>
<body>
<jsp:include page="/segments/header.jsp"></jsp:include>
<hr/>
<jsp:include page="/stat/segments/statnav.jsp">
	<jsp:param value="bytype" name="activeLink"/>
</jsp:include>
<hr/>
<jsp:include page="/stat/segments/statmonth.jsp">
	<jsp:param value="bytype" name="statType"/>
</jsp:include>
<br/>
<div>
<table border="1">
<thead>
	<tr>
		<th>${month}月</th>
		<th>累计</th>
		<% StatByTypeDto dto = (StatByTypeDto)request.getAttribute("dto"); %>
		<% for (int i = dto.getMaxDate(); i >= dto.getMinDate(); i--) { %>
		<th><%= i %>日</th>
		<% } %>
	</tr>
</thead>
<tbody>
	<tr>
		<td>非验证码投诉</td>
		<c:forEach items="${dto.nonCodeComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
	<tr>
		<td>A类投诉</td>
		<c:forEach items="${dto.typeAComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
	<tr>
		<td>B类投诉</td>
		<c:forEach items="${dto.typeBComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
	<tr>
		<td>C类投诉</td>
		<c:forEach items="${dto.typeCComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
	<tr>
		<td>D类投诉</td>
		<c:forEach items="${dto.typeDComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
	<tr>
		<td>E类投诉</td>
		<c:forEach items="${dto.typeEComplaint}" var="dateCount">
		<td>${dateCount}</td>
		</c:forEach>
	</tr>
</tbody>
</table>
</div>
</body>
</html>