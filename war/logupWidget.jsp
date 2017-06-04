<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
			<form id="signupform" class="form-horizontal" role="form"
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
						<input type="text" class="form-control" name="user_phone"
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