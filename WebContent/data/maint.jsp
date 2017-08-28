<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<table border="1">
<thead>
	<tr>
		<th>A</th>
		<th>B</th>
		<th>C</th>
		<th>D</th>
		<th>E</th>
		<th>F</th>
		<th>G</th>
		<th>H</th>
		<th>I</th>
		<th>J</th>
		<th>K</th>
		<th>L</th>
		<th>M</th>
		<th>N</th>
		<th>O</th>
	</tr>
</thead>
<tbody>
	<tr>
		<td>服务请求标识</td>
		<td>举报手机号码</td>
		<td style="width: 50px;">举报受理省</td>
		<td>用户举报时间</td>
		<td>被举报号码/网站地址</td>
		<td style="width: 50px;">被举报号码归属省</td>
		<td style="width: 50px;">归属地市</td>
		<td>服务请求类别</td>
		<td>业务平台名称</td>
		<td>举报对象类型</td>
		<td>举报内容</td>
		<td>下发内容</td>
		<td>投诉类别</td>
		<td>所属客户</td>
		<td>简称</td>
	</tr>
	<c:forEach items="${orreList }" var="orre" varStatus="vs">
	<tr>
		<td><c:out value="${orre.serverRequestIdentifier }"></c:out></td>
		<td><c:out value="${orre.reportMobileNumber }"></c:out></td>
		<td><c:out value="${orre.reportProvince }"></c:out></td>
		<td><fmt:formatDate value="${orre.reportDate }" pattern="yyyy-MM-dd"/></td>
		<td><c:out value="${orre.reportedNumber }"></c:out></td>
		<td><c:out value="${orre.reportedProvince }"></c:out></td>
		<td><c:out value="${orre.reportedCity }"></c:out></td>
		<td><c:out value="${orre.serverRequestType }"></c:out></td>
		<td><p style="max-width: 250px;"><c:out value="${orre.bizPlatform }"></c:out></p></td>
		<td><c:out value="${orre.reportObjectType }"></c:out></td>
		<td><p style="max-width: 350px; overflow: hidden;"><c:out value="${orre.reportContent }"></c:out></p></td>
		<td>
			<c:choose>
				<c:when test="${empty orre.distContent }">未填写</c:when>
				<c:otherwise><c:out value="${orre.distContent }"></c:out></c:otherwise>
			</c:choose>
			<br/><a href="#">修改</a>
		</td>
		<td><c:out value="${orre.complaintType }"></c:out><br/><a href="#">修改</a></td>
		<td><p style="max-width: 100px; overflow: hidden;"><c:out value="${orre.matchesClientPortRelationship.companyName }"></c:out></p></td>
		<td><c:out value="${orre.matchesClientPortRelationship.companyShortName }"></c:out></td>
	</tr>
	</c:forEach>
</tbody>
</table>

</body>
</html>