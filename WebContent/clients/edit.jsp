<%@page import="com.xingyanping.util.DateUtil"%>
<%@page import="com.xingyanping.dao.ClientPortRelationshipDao"%>
<%@page import="com.xingyanping.datamodel.ClientPortRelationship"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
Long id = Long.valueOf(request.getParameter("id"));
ClientPortRelationship client = new ClientPortRelationshipDao().get(id);
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
<form action="${ctx}/client/edit" method="post">
<input type="hidden" name="id" value="<%=id%>"/>
<label for="port">端口号</label><input type="text" name="port" id="port" value="<%=client.getPort()%>"/><br/>
<label for="effectiveDate">起始日期</label><input type="date" name="effectiveDate" id="effectiveDate" value="<%=DateUtil.formatDate(client.getEffectiveDate())%>"/><br/>
<label for="expiringDate">结束日期</label><input type="date" name="expiringDate" id="expiringDate" value="<%=DateUtil.formatDate(client.getExpiringDate())%>"/><br/>
<label for="companyName">所属公司</label><input type="text" name="companyName" id="companyName" value="<%=client.getCompanyName()%>"/><br/>
<label for="companyShortName">简称</label><input type="text" name="companyShortName" id="companyShortName" value="<%=client.getCompanyShortName()%>"/><br/>
<label for="client">所属客户</label><input type="text" name="client" id="client" value="<%=client.getClient()%>"/><br/>
<button type="submit">确定</button>
</form>
</div>
</body>
</html>