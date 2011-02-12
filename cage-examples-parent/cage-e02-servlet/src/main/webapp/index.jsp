<%@page import="com.github.cage.examples.cage_e02_servlet.CaptchaServlet"%><%@
page contentType="text/html" pageEncoding="UTF-8"%><%
	boolean showGoodResult;
	boolean showBadResult;
	if ("POST".equals(request.getMethod())) {
		String sessionToken = CaptchaServlet.getToken(session);
		String requestToken = request.getParameter("captcha");
		showGoodResult = sessionToken != null && sessionToken.equals(requestToken);
		showBadResult = !showGoodResult;
	} else {
		showGoodResult = showBadResult = false;
	}

	CaptchaServlet.generateToken(session);
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta charset="UTF-8" />
		<title>Captcha Reader</title>
	</head>
	<body>
<%	if (showGoodResult) {%>
	<h1 style="color: green;">Your kung fu is good!</h1>
<%	} else if (showBadResult) {%>
	<h1 style="color: red;">This is not right. Try again!</h1>
<%	} %>
		<p>Type in the word seen on the picture</p>
		<form action="" method="post">
			<input name="captcha" type="text" autocomplete="off" />
			<input type="submit" />
		</form>
		<img alt="captcha image" src="captcha" />
	</body>
</html>
