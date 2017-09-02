<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setAttribute("ctx", request.getContextPath());
%>
<div>
<a href="${ctx}/stat/bytype">按类别统计</a>&nbsp;&nbsp;
<a href="${ctx}/stat/byclient">按客户统计</a>&nbsp;&nbsp;
<a href="${ctx}/stat/bychannel">按通道统计</a>
</div>
