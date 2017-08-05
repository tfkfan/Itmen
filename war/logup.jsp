<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.itmencompany.datastore.entities.AppUser"%>
<%@ page import="com.itmencompany.helpers.AppUserHelper"%>
<%
	//Checking current user
	AppUser appUser = AppUserHelper.getUserFromRequest(request);
	if (appUser != null) {
		response.sendRedirect("/");
		return;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="/css/styles.css" />
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<script src="/js/jquery.validate.min.js"></script>
<script src="/js/additional-methods.min.js"></script>
<script type="text/javascript" src="/js/jquery.inputmask.js"></script>
<title>Регистрация</title>
</head>
<body id="black-container">
	<div class="container">
		<div id="loginbox" style="margin-top: 25%"
			class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">Регистрация</div>
					<div
						style="float: right; font-size: 85%; position: relative; top: -10px">
						<a id="signinlink" href="/login">Войти</a>
					</div>
				</div>
				<div class="panel-body">
					<form id="signupForm" class="form-horizontal" role="form"
						action="/logup" method="post">
						<div id="signuperrors" style="display: none"
							class="alert alert-danger">
							<p>Ошибки:</p>
							<span></span>
						</div>

						<div class="form-group">
							<label for="user_email" class="col-md-3 control-label">Почта</label>
							<div class="col-md-9">
								<input type="text" class="form-control" name="user_email"
									placeholder="Введите почту">
							</div>
						</div>

						<div class="form-group">
							<label for="user_name" class="col-md-3 control-label">Имя</label>
							<div class="col-md-9">
								<input type="text" class="form-control" name="user_name"
									placeholder="Введите имя">
							</div>
						</div>

						<div class="form-group">
							<label for="user_phone" class="col-md-3 control-label">Номер
								телефона</label>
							<div class="col-md-9">
								<input id="user_phone" type="text" class="form-control" name="user_phone"
									placeholder="Введите номер телефона">
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-offset-3 col-md-9">
								<button id="btn-signup" type="submit" class="btn btn-info">
									<i class="icon-hand-right"></i> &nbsp Зарегистрироваться
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script>
	$("#user_phone").inputmask("+7(999)999-99-99");
		$("#signupForm").validate({
			rules : {
				user_email : {
					required : true,
					email : true
				},
				user_name : {
					required : true
				},
				user_phone : {
					required: true
				}
			},
			messages : {
				user_phone : "Введите правильный телефон",
				user_name : "Введите имя",
				user_email : "Введите электронную почту"
			},
			submitHandler : function(form) {
				form.submit();
			}
		});
	</script>
</body>
</html>