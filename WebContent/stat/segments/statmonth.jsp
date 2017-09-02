<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
request.setAttribute("ctx", request.getContextPath());
request.setAttribute("statType", request.getParameter("statType"));
%>
<div>
<c:forEach items="${monthListVo.montVoList}" var="monthVo">
<a href="${ctx}/stat/${statType}?m=${monthVo.monthParam}">${monthVo.monthDisp }</a>
&nbsp;&nbsp;
</c:forEach>
</div>
