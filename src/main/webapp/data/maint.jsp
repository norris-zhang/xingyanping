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
<meta charset="utf-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctx}/css/main.css?v=1" />
<link rel="stylesheet/less" type="text/css" href="${ctx}/css/fixed-table-header.less?v=2" />
<script src="//cdnjs.cloudflare.com/ajax/libs/less.js/2.7.2/less.min.js"></script>

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
				if (textContent === '无下发记录' || textContent === '重复投诉') {
					$("#distContentDisp" + orreId).addClass("red-text");
				} else {
					$("#distContentDisp" + orreId).removeClass("red-text");
				}
			}
		}
	)
	.fail(function(){
		alert("Failed to save");
	});
	return false;
}

function editComplaintType(e, orreId) {
	var eventObj = e.target || e.srcElement;
	var pos = $(eventObj).offset();
	var top = pos.top - 30;
	if (top < 0) {
		top = 0;
	}
	var left = pos.left - 68;
	if (left < 0) {
		left = 0;
	}
	$("#typeOptions").css({top: top, left: left});
	$("#typeOptions").data("orreid", orreId);
	$("#typeOptions").show();

	setActiveButton(orreId);

	e.stopPropagation();
	return false;
}
function setActiveButton(orreId) {
	var ctDisp = $("#complaintTypeDisp" + orreId).text();
	$("#typeOptions button").each(function(index, element) {
		if (ctDisp === $(element).text()) {
			$(element).removeClass("btn-primary");
			$(element).addClass("btn-danger");
		} else {
			$(element).removeClass("btn-danger");
			$(element).addClass("btn-primary");
		}
	});
}
function saveComplaintType(orreId, complaintType) {
	$.post(
		"${ctx}/orre/savect",
		{
			complaintType: complaintType,
			id: orreId,
			v: new Date().getTime()
		},
		function(json) {
			if (json.code !== "OK") {
				alert("Failed to save.")
			} else {
				$("#complaintTypeDisp" + orreId).text(complaintType);
				$("#typeOptions").fadeOut(300);
			}
		}
	)
	.fail(function(){
		alert("Failed to save");
	});
	return false;
}
function setDistContent(orreId, content) {
	$("#distContent" + orreId).val(content);
	saveDistContent(orreId);
}
function copyDistContent(orreId) {
	var reportContent = $("#reportContent" + orreId).text();
	$("#distContent" + orreId).val(reportContent);
	saveDistContent(orreId);
	return false;
}

$(document).ready(function(){
	$(document).click(function(){
		$("#typeOptions").fadeOut(300);
	});
	$("#typeOptions button").click(function(event){
		var orreId = $("#typeOptions").data("orreid");
		var complaintType = $(event.target).data("ctype");
		return saveComplaintType(orreId, complaintType);
	});
});

</script>
</head>
<body>
<jsp:include page="/segments/header.jsp"></jsp:include>
<hr/>
<div id="typeOptions" data-orreid="" style="position: absolute; z-index: 10; display: none;">
<button type="button" class="btn btn-primary" data-ctype="A">A</button>
<button type="button" class="btn btn-primary" data-ctype="B">B</button>
<button type="button" class="btn btn-primary" data-ctype="C">C</button>
<button type="button" class="btn btn-primary" data-ctype="D">D</button>
<button type="button" class="btn btn-primary" data-ctype="E">E</button>
</div>
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
		<th>P</th>
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
		<th>举报来源</th>
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
		<td><div id="reportContent${orre.id}"><c:out value="${orre.reportContent }"></c:out></div></td>
		<td><div><c:out value="${orre.reportSource }"></c:out></div></td>
		<td>
			<div>
			<div id="editDistContent${orre.id}">
				<div id="distContentDisp${orre.id}" <c:if test="${orre.distContent == '无下发记录' || orre.distContent == '重复投诉'}">class="red-text"</c:if>>
					<c:choose>
						<c:when test="${empty fn:trim(orre.distContent) }">未填写</c:when>
						<c:otherwise><c:out value="${orre.distContent }"></c:out></c:otherwise>
					</c:choose>
				</div>
				<br/><a href="#" onclick="return editDistContent(${orre.id})">修改</a>
				&nbsp;<a href="#" onclick="return setDistContent(${orre.id}, '无下发记录')">无下发记录</a>
				&nbsp;<a href="#" onclick="return setDistContent(${orre.id}, '重复投诉')">重复</a>
				&nbsp;<a href="#" onclick="return copyDistContent(${orre.id})">复制</a>
			</div>
			<div id="textarea${orre.id}" style="display: none;">
				<textarea id="distContent${orre.id}" style="width: 98%;" rows="6">${orre.distContent }</textarea>
				<br/>
				<a href="#" onclick="return saveDistContent(${orre.id})">保存</a>
			</div>
			</div>
		</td>
		<td>
			<div>
				<div id="complaintTypeDisp${orre.id}"><c:out value="${orre.complaintType }"></c:out></div>
				<br/><a href="#" onclick="return editComplaintType(event, ${orre.id})">修改</a>
			</div>
		</td>
		<td><div><c:out value="${orre.matchesClientPortRelationship.client }"></c:out></div></td>
		<td><div><c:out value="${orre.matchesClientPortRelationship.companyShortName }"></c:out></div></td>
	</tr>
	</c:forEach>
</tbody>
</table>

</body>
</html>