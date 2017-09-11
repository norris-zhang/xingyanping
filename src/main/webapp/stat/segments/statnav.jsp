<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
request.setAttribute("ctx", request.getContextPath());
request.setAttribute("activeLink", request.getParameter("activeLink"));
%>
<div>
<a href="${ctx}/stat/bytype" <c:if test="${activeLink == 'bytype'}">class="active-link"</c:if>>按类别统计</a>&nbsp;&nbsp;
<a href="${ctx}/stat/byclient" <c:if test="${activeLink == 'byclient'}">class="active-link"</c:if>>按客户统计</a>&nbsp;&nbsp;
<a href="${ctx}/stat/bychannel" <c:if test="${activeLink == 'bychannel'}">class="active-link"</c:if>>按通道统计</a>
</div>
