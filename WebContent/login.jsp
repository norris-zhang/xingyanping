<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<form action="${ctx}/login" method="post">
	<label for="username">Username</label><input type="text" name="username" id="username"/><br/>
	<label for="password">Password</label><input type="password" name="password" id="password"/><br/>
	<button type="submit">Sign In</button>
</form>
</body>
</html>