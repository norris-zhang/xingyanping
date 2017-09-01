<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
request.setAttribute("ctx", request.getContextPath());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet/less" type="text/css" href="${ctx}/css/fixed-table-header.less" />
<script src="//cdnjs.cloudflare.com/ajax/libs/less.js/2.7.2/less.min.js"></script>
</head>
<body>
<jsp:include page="/segments/header.jsp"></jsp:include>
<hr/>
<table class="fixed_headers" border="1">
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
	<tr>
		<th>服务请求标识</th>
		<th>举报手机号码</th>
		<th>举报受理省</th>
		<th>用户举报时间</th>
		<th>被举报号码/网站地址</th>
		<th>被举报号码归属省</th>
		<th>归属地市</th>
		<th>服务请求类别</th>
		<th>业务平台名称</th>
		<th>举报对象类型</th>
		<th>举报内容</th>
		<th>下发内容</th>
		<th>投诉类别</th>
		<th>所属客户</th>
		<th>简称</th>
	</tr>
</thead>
<tbody>
	<c:forEach items="${orreList }" var="orre" varStatus="vs">
	<tr>
		<td><div><c:out value="${orre.serverRequestIdentifier }"></c:out></div></td>
		<td><div><c:out value="${orre.reportMobileNumber }"></c:out></div></td>
		<td><div><c:out value="${orre.reportProvince }"></c:out></div></td>
		<td><div><fmt:formatDate value="${orre.reportDate }" pattern="yyyy-MM-dd"/></div></td>
		<td><div><c:out value="${orre.reportedNumber }"></c:out></div></td>
		<td><div><c:out value="${orre.reportedProvince }"></c:out></div></td>
		<td><div><c:out value="${orre.reportedCity }"></c:out></div></td>
		<td><div><c:out value="${orre.serverRequestType }"></c:out></div></td>
		<td><div><c:out value="${orre.bizPlatform }"></c:out></div></td>
		<td><div><c:out value="${orre.reportObjectType }"></c:out></div></td>
		<td><div><c:out value="${orre.reportContent }"></c:out></div></td>
		<td>
			<div>
			<div id="editDistContent${orre.id}">
				<div id="distContentDisp${orre.id}">
					<c:choose>
						<c:when test="${empty fn:trim(orre.distContent) }">未填写</c:when>
						<c:otherwise><c:out value="${orre.distContent }"></c:out></c:otherwise>
					</c:choose>
				</div>
				<br/><a href="#" onclick="return editDistContent(${orre.id})">修改</a>
			</div>
			<div id="textarea${orre.id}" style="display: none;">
				<textarea id="distContent${orre.id}" style="width: 98%;" rows="2">${orre.distContent }</textarea>
				<br/>
				<a href="#" onclick="return saveDistContent(${orre.id})">保存</a>
			</div>
			</div>
		</td>
		<td>
			<div>
			<div id="editComplaintType${orre.id}">
				<div id="complaintTypeDisp${orre.id}"><c:out value="${orre.complaintType }"></c:out></div>
				<br/><a href="#" onclick="return editComplaintType(${orre.id})">修改</a>
			</div>
			<div id="select${orre.id}" style="display: none;">
				<select id="complaintType${orre.id}">
					<option value="未分类">未分类</option>
					<option value="A">A</option>
					<option value="B">B</option>
					<option value="C">C</option>
					<option value="D">D</option>
					<option value="E">E</option>
				</select>
				<script type="text/javascript">
					document.getElementById("complaintType${orre.id}").value='${orre.complaintType}';
				</script>
				<br/>
				<a href="#" onclick="return saveComplaintType(${orre.id})">保存</a>
			</div>
			</div>
		</td>
		<td><div><c:out value="${orre.matchesClientPortRelationship.client }"></c:out></div></td>
		<td><div><c:out value="${orre.matchesClientPortRelationship.companyShortName }"></c:out></div></td>
	</tr>
	</c:forEach>
</tbody>
</table>
<script type="text/javascript" src="${ctx}/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
function editDistContent(orreId) {
	$("#editDistContent" + orreId).hide();
	$("#textarea" + orreId).show();
	return false;
}
function saveDistContent(orreId) {
	$.post(
		"${ctx}/orre/savedc",
		{
			distContent: $("#distContent" + orreId).val(),
			id: orreId,
			v: new Date().getTime()
		},
		function(json) {
			if (json.code !== "OK") {
				alert("Failed to save.")
			} else {
				$("#editDistContent" + orreId).show();
				$("#textarea" + orreId).hide();
				var textContent = $("#distContent" + orreId).val();
				if (/^\s*$/.test(textContent)) {
					textContent = "未填写";
				}
				$("#distContentDisp" + orreId).text(textContent);
			}
		}
	)
	.fail(function(){
		alert("Failed to save");
	});
	return false;
}
function editComplaintType(orreId) {
	$("#editComplaintType" + orreId).hide();
	$("#select" + orreId).show();
	return false;
}
function saveComplaintType(orreId) {
	$.post(
		"${ctx}/orre/savect",
		{
			complaintType: $("#complaintType" + orreId).val(),
			id: orreId,
			v: new Date().getTime()
		},
		function(json) {
			if (json.code !== "OK") {
				alert("Failed to save.")
			} else {
				$("#editComplaintType" + orreId).show();
				$("#select" + orreId).hide();
				var textContent = $("#complaintType" + orreId).val();
				$("#complaintTypeDisp" + orreId).text(textContent);
			}
		}
	)
	.fail(function(){
		alert("Failed to save");
	});
	return false;
}
</script>
</body>
</html>