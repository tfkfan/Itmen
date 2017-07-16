<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/bootstrap/css/bootstrap-theme.min.css" />
<link rel="stylesheet" href="/css/styles.css" />
<script src="/js/jquery-3.2.1.min.js"></script>
<script src="/bootstrap/js/bootstrap.min.js"></script>
<title>Восстановление пароля</title>
</head>

<body id="black-container">
	<div class="container">
		<div id="loginbox" style="margin-top: 25%;"
			class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="panel-title">Восстановление пароля</div>
					<div
						style="float: right; font-size: 80%; position: relative; top: -10px">
						<a href="/login">Войти</a>
					</div>
				</div>

				<div style="padding-top: 30px" class="panel-body">
					<div style="display: none" id="login-alert"
						class="alert alert-danger col-sm-12"></div>
					<form id="loginform" class="form-horizontal" action="/forgot_password"
						method="post" role="form">

						<div style="margin-bottom: 25px" class="input-group">
							<span class="input-group-addon"><i
								class="glyphicon glyphicon-user"></i></span> <input id="login-username"
								type="text" class="form-control" name="user_email" value=""
								placeholder="Введите почту">
						</div>

						<div style="margin-top: 10px" class="form-group">
							<div class="col-sm-12 controls">
								<button id="btn-send" type="submit" class="btn btn-success">Отослать пароль по почте
								</button>
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-12 control">
								<div
									style="border-top: 1px solid #888; padding-top: 15px; font-size: 85%">
									У меня нет аккаунта! <a href="/logup"> Зарегистрироваться
										здесь </a>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>