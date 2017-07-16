<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h"%>
<%@ page import="com.itmencompany.datastore.entities.AppUser"%>
<%@ page import="com.itmencompany.common.UserInfo"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.List"%>
<%
	Logger log = Logger.getLogger("privateOffice.jsp");
%>
<%@ include file="header.jsp"%>
<%
	Long userId = appUser != null ? appUser.getId() : null;

	String answerPageObj = request.getParameter("answerPage");
	Integer answerPageNum = 1;
	Integer limit = 5;
	try {
		answerPageNum = Integer.parseInt(answerPageObj);
	} catch (Exception e) {

	}
%>
<div class="custom-container">
	<div class="page-header">
		<h2>Личный кабинет</h2>
	</div>
	<%
		if (appUser != null) {
	%>
	
	<div class="row">
		<div class="col-xs-6 col-md-offset-2 col-md-8 ">
			<h:CompanyAnswers limit="<%=limit%>" isAdminPage="false" />
		</div>
	</div>
	<%
		}
	%>
	<div class="row">
		<div class="col-xs-6 col-md-offset-2 col-md-8">
			<div class="custom-form-container">
				<form id="privateParams" class="form-horizontal" action="/private" method="post" role="form">
					<fieldset>
						<legend>
							<h3>Заявка компаниям</h3>
						</legend>
						<div id="errors" style="display: none" class="alert alert-danger">
						</div>

						<div class="form-group">
							<div class="row jumbotron" id="images" style="display: none;">
							</div>
						</div>

						<div class="form-group">
							<label for="photo" class="col-md-6 control-label">Прикрепите
								фотографию или эскиз</label>
							<div class="col-md-6">
								<input id="photo" name="photo" type="file">
							</div>

						</div>

						<div class="form-group">
							<label for="photo" class="col-md-6 control-label"></label>
							<div class="col-md-6">
								<button id="clearFiles" class="btn btn-default">Удалить фотографии</button>
							</div>
						</div>

						<div class="form-group">
							<label for="length" class="col-md-6 control-label">Укажите
								длину гарнитура. Описание: Измерьте рулеткой длину стены, где
								будет стоять гарнитур в сантиметрах.</label>
							<div class="col-md-6">
								<input type="text" class="form-control" id="length"
									name="length" placeholder="Укажите длину">
							</div>
						</div>
						
						<div class="form-group">
							<label for="height" class="col-md-6 control-label">Укажите
								высоту в сантиметрах если нестандартная.</label>
							<div class="col-md-6">
								<input type="text" class="form-control" id="height"
									name="height" placeholder="Укажите высоту">
							</div>
						</div>

						<div class="form-group">
							<label for="fasade_material" class="col-md-6 control-label">Укажите
								материал фасадов. Описание: укажите какой материал вы хотели бы
								использовать. ЛДСП, МДФ в Пленке, Пластиковые фасады</label>
							<div class="col-md-6">
								<input type="text" class="form-control" id="fasade_material"
									name="fasade_material" placeholder="Укажите материал">
							</div>
						</div>

						<div class="form-group">
							<label for="is_parlor" class="col-md-6 control-label">Укажите
								необходим ли пристенок</label>
							<div class="col-md-6">
								<input type="checkbox" id="is_parlor" name="is_parlor"
									aria-label="...">
							</div>
						</div>

						<div class="form-group">
							<label for="wishes" class="col-md-6 control-label">Укажите
								пожелания по фурнитуре если есть. (Петли, доводчики, системы
								открывания дверей)</label>
							<div class="col-md-6">
								<textarea class="form-control" rows="5" id="wishes"
									name="wishes" style="resize:vertical;"></textarea>
							</div>
						</div>

						<div class="form-group">
							<label for="additional_wishes" class="col-md-6 control-label">Дополнительные
								пожелания к изделию. Описание: укажите ваши пожелания в этом
								поле для более точного подсчета изготовления. </label>
							<div class="col-md-6">
								<textarea class="form-control" rows="5" id="additional_wishes"
									name="additional_wishes" style="resize:vertical;"></textarea>
							</div>
						</div>
						<%
							if (appUser == null) {
						%>
						<hr>
						<div class="form-group">
							<label for="user_email" class="col-md-6 control-label">Ваша
								Почта</label>
							<div class="col-md-6">
								<input type="email" class="form-control" id="user_email"
									name="user_email" placeholder="Введите почту">
							</div>
						</div>

						<div class="form-group">
							<label for="user_name" class="col-md-6 control-label">Ваше
								Имя</label>
							<div class="col-md-6">
								<input type="text" class="form-control" id="user_name"
									name="user_name" placeholder="Введите имя">
							</div>
						</div>

						<div class="form-group">
							<label for="user_phone" class="col-md-6 control-label">Ваш
								Номер телефона</label>
							<div class="col-md-6">
								<input type="text" class="form-control" id="user_phone"
									name="user_phone" placeholder="Введите номер телефона">
							</div>
						</div>
						<%
							}
						%>
						<div class="form-group">
							 <div class="g-recaptcha col-md-offset-5" data-sitekey="6LevjCgUAAAAAAZnfSs3Jv8ufDvgcswpJ9-IFAXS"></div>
						</div>
						<div class="form-group">
							<label for="sendInfo" class="col-md-6 control-label"></label>
							<div class="col-md-offset-5 col-md-8">
								<button id="btn-post-info" type="submit" name="sendInfo"
									class="btn btn-default btn-lg">
									<i class="icon-hand-right"></i> &nbsp Отправить заявку
								</button>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<%
		if (appUser != null) {
	%>
	<div class="">
		<div class="row">
			<div class="col-xs-8 col-md-offset-2 col-md-8">
				<div class="custom-form-container">
					<form id="privateUserInfo" class="form-horizontal" role="form">
						<fieldset>
							<legend>
								<h3>Личная информация</h3>
							</legend>

							<div class="form-group">
								<label for="p_user_name" class="col-md-2 control-label">Ваше
									имя</label>
								<div class="col-md-6">
									<p id="p_user_name" class="form-control-static"><%=appUser.getUserName()%></p>
								</div>
							</div>

							<div class="form-group">
								<label for="p_user_phone" class="col-md-2 control-label">Ваш
									телефон</label>
								<div class="col-md-6">
									<p id="p_user_phone" class="form-control-static"><%=appUser.getPhone()%>
									</p>
								</div>
							</div>

							<div class="form-group">
								<label for="p_user_email" class="col-md-2 control-label">Ваша
									почта</label>
								<div class="col-md-6">
									<p id="p_user_email" class="form-control-static"><%=appUser.getEmail()%>
									</p>
								</div>
							</div>
							
							<div class="form-group">
								<label for="p_user_password" class="col-md-2 control-label">Сменить
									пароль</label>
								<div class="col-md-6">
									<button id="p_user_password_change" class="btn btn-default">Сменить</button>
								</div>
							</div>
							
							<div class="form-group">
								<label for="p_user_notifications" class="col-md-2 control-label">
									Уведомления по почте</label>
									<div class="col-md-6">
										<%if(appUser.getIsNtfsEnabled()){%>
										<button id="userNotifications" class="btn btn-danger">Отключить</button>
										<%}else{ %>
										<button id="userNotifications" class="btn btn-success">Включить</button>
										<%} %>
									</div>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%
		}
	%>
</div>
<script>
	function removeIMG(elem) {
		if (elem === undefined)
			return;
		elem.remove();
	}
	$(document).ready(function() {
		$('#privateUserInfo').submit(false);
		$('#photo').on('change',function() {
			var file = this.files[0];
			var savedFiles = $(".savedImages").length;
			
			if (savedFiles > 3)
				return;
			
			$.get("/get_upload",function(upload_url) {
				if (upload_url === undefined) {
					console.log("Some error ocured during getting upload url");
					return;
				}
				$.ajax({
					url : upload_url,
					type : 'POST',
					data : new FormData($('#privateParams')[0]),
					cache : false,
					contentType : false,
					processData : false,
					async : false,
					success : function(src) {
						var imgJUMB = $("#images");
						imgJUMB.css("display", "block");
						imgJUMB.append('<div class="removable col-xs-6 col-md-3" onclick="removeIMG(this)">'
							+ '<a  class="thumbnail">'
							+ '<img class="savedImages" src="' + src + '" alt="...">'
							+ '</a></div>');
					},
					error : function(msg) {
						alert(msg);
					}
				});
			
			});
		});

		var btnNtfs = $("#userNotifications");
		btnNtfs.click(function(){
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_notifications"
			}, function(data) {
				var isEnabled = data["value"];
				var removedClass = "btn-success";
				var addedClass = "btn-danger";
				var txt = "Отключить";
				if(isEnabled == "false"){
					removedClass = "btn-danger";
					addedClass = "btn-success";
					txt = "Включить";
				}
				btnNtfs.removeClass(removedClass);
				btnNtfs.addClass(addedClass);
				btnNtfs.text(txt);
			}).fail(function() {
			    console.log( "error changing user notifications settings" );
			});
		});
		
		$("#p_user_password_change").click(function(){
			$.post("/forgot_password", {
			}, function(data) {
				alert("Новый пароль выслан по почте, указанной при регистрации");
			}).fail(function() {
			    console.log( "error changing user password");
			});
		});
		
		$("#user_email").on('input', function() {
			$.post("/verify", {
				user_email : $("#user_email").val()
			}, function(data) {
				if (data.message !== undefined) {
					setButtonEnabled(true);
					printErrorMsg(data.message);
				} else {
					clearErrorMsg();
					setButtonEnabled(false);
				}
			});
		});

		editableElemClickHandler($("#p_user_name"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_name",
				value : val
			}, function(data) {
				if (data.error != undefined)
					alert(data.message);
			});
		});
		editableElemClickHandler($("#p_user_phone"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_phone",
				value : val
			}, function(data) {
				if (data.error != undefined)
					alert(data.message);
			});
		});
		editableElemClickHandler($("#p_user_email"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_email",
				value : val
			}, function(data) {
				if (data.error != undefined)
					alert(data.message);
			});
		});
		
		$("#privateParams").validate({
			rules: {
				length: {
					number: true
				},
				height:{
					number:true
				},
			
				user_phone: "required",
				user_name: {
					required: true,
					minlength: 3
				},
				
				user_email: {
					required: true,
					email: true
				}
			},
			messages: {
				length: "Введите число",
				height: "Введите число",
				user_phone: "Введите правильный телефон",
				user_name: {
					required: "Введите имя",
					minlength: "Ваше имя слишком короткое"
				},
				user_email: "Введите электронную почту"
			},
			submitHandler: function(){
				try {
					if (grecaptcha.getResponse() == ''){
						  alert("Подвердите что вы не бот.");
						  return;
					}
						  
					var submitBtn = $("#btn-post-info");
					submitBtn.prop('disabled', true);
					var files = getImages();
					var length = $("#length").val();
					var fasade_material = $(
							"#fasade_material")
							.val();
					var is_parlor = $("#is_parlor")
							.is(":checked");
					var wishes = $("#wishes").val();
					var height = $("#height").val();
					var additional_wishes = $(
							"#additional_wishes")
							.val();
		
					var user_email = $(
							"#user_email").val();
					var user_phone = $(
							"#user_phone").val();
					var user_name = $("#user_name")
							.val();

					$.post("/private",{
							mode : "post_user_info",
							images : files,
							length : length,
							fasade_material : fasade_material,
							is_parlor : is_parlor,
							wishes : wishes,
							height : height,
							additional_wishes : additional_wishes,

							user_email : user_email,
							user_phone : user_phone,
							user_name : user_name
						},
						function(data) {
							if (data.error != undefined) {
								printErrorMsg(data.message);
							} else {
								alert(data.message);
								location
										.reload();
							}
						}).fail(function(
									jqXHR,
									textStatus,
									errorThrown) {
								alert("ERROR "
										+ textStatus);
							});
						} catch (e) {
							console
									.log("Some error occured gathering info parameters");
						}
			}
		});


		$("#clearFiles").click(function() {
			$("#images").empty();
			$("#photo").val("");
		});

		$(".removable").click(function() {
			removeIMG($(this));
		});

	});
</script>
<%@ include file="footer.jsp"%>