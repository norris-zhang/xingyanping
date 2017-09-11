<%@page import="com.xingyanping.datamodel.ClientPortRelationship"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
Map<String, List<ClientPortRelationship>> clientMap = (Map<String, List<ClientPortRelationship>>)request.getAttribute("clientMap");
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
<div>Client Maintenance</div>
<hr/>
<div>
<div><a href="${ctx}/clients/add.jsp">增加</a></div>
<table border="1">
	<thead>
		<tr>
			<th>端口号</th>
			<th>起始日期</th>
			<th>结束日期</th>
			<th>所属公司</th>
			<th>简称</th>
			<th>所属客户</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<% for (String port : clientMap.keySet()) { %>
			<% for (int i = 0; i < clientMap.get(port).size(); i++) { %>
				<% ClientPortRelationship client = clientMap.get(port).get(i); %>
				<tr>
				<% if (i == 0) { %>
					<td rowspan="<%=clientMap.get(port).size()%>"><%= port %> <a href="${ctx}/clients/add.jsp?port=<%=port%>">(+)</a></td>
				<% } %>
					<td><%= client.getEffectiveDate() %></td>
					<td><%= client.getExpiringDate() %></td>
					<td><%= client.getCompanyName() %></td>
					<td><%= client.getCompanyShortName() %></td>
					<td><%= client.getClient() %></td>
					<td>
						<a href="${ctx}/clients/edit.jsp?id=<%=client.getId()%>">修改</a>
					</td>
				</tr>
			<% } %>
		<% } %>
	</tbody>
</table>
</div>
</body>
</html>