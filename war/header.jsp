<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.itmencompany.mvc.datastore.entities.AppUser"%>
<%@ page import="com.itmencompany.common.AppUserHelper"%>
<%@ page import="com.itmencompany.common.ServerUtils"%>
<%
	String url = request.getRequestURI();
	AppUser appUser = AppUserHelper.getUserFromRequest(request);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="/css/styles.css" />
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<script src="/js/jquery.validate.min.js"></script>
<script src="/js/additional-methods.min.js"></script>
<script type="text/javascript" src="/js/jquery.inputmask.js"></script>
<script src="/js/scripts.js"></script>
<title>
<%if (url.contains("/private")) { %> Личный кабинет 
<%}else if(url.contains("/admin")){ %> Панель администратора
<%}else if(url.contains("/campaigns")){ %> Кампании
<%}else{ %>
ITMEN Company
<%} %>
</title>
</head>
<body>
		<nav class="navbar navbar-inverse"
			style="border-radius:0px !important;">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/"><%=ServerUtils.SERVICE_NAME %></a>
			</div>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul id="links" class="nav navbar-nav">
					<li <%if (url.contains("/private")) {%> class="active" <%}%>><a
						href="/private">Личный кабинет</a></li>
					<%if(appUser != null && appUser.getIsAdmin()){ %>	
					<li <%if (url.contains("/campaigns")) {%> class="active" <%}%>><a
						href="/campaigns">Кампании</a></li>
					<li <%if (url.contains("/admin")) {%> class="active" <%}%>><a
						href="/admin">Панель Администратора</a></li>
					<%} %>
				</ul>
		     
				<ul class="nav navbar-nav navbar-right">
					<li> <% if(appUser != null) {%>
						<a href="/logout" id="logout">Выйти</a>
						<%}else{ %>
							<a href="/login" id="login">Войти</a>
						<%} %>
					</li>
				</ul>
				
			</div>
			<!-- /.navbar-collapse -->
		</div>
			<!-- /.container-fluid -->
		</nav>
<div class="wrapper">
	<div class="content">