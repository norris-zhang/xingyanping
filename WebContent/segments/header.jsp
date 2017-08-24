<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
%>
<div>
<a href="${ctx}/">首页</a>&nbsp;&nbsp;
<a href="${ctx}/clients">客户关系维护</a>&nbsp;&nbsp;
<a>数据统计</a>
</div>