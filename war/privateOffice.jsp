<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.itmencompany.datastore.entities.AppUser"%>
<%@ page import="com.itmencompany.helpers.AppUserHelper"%>
<%@ page import="com.itmencompany.common.UserInfo"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.List"%>
<%
	Logger log = Logger.getLogger("privateOffice.jsp");
%>
<%@ include file="header.jsp"%>
<div class="custom-container">
	<div class="page-header">
		<h2>Личный кабинет</h2>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-2">
			<%if(appUser != null){ %>
			<div>
				Здавствуйте, <span id="user_name"> <span
					class="label label-info"><%=appUser.getUserName()%></span>

				</span>
			</div>
			<div>
				Ваш телефон: <span id="user_phone"> <span
					class="label label-info"><%=appUser.getPhone()%></span>
				</span>
			</div>
			<div>

				Ваш email: <span id="user_email"> <span
					class="label label-info"><%=appUser.getEmail()%></span>
				</span>
			</div>
			<%}%>
		</div>
		<div class="col-xs-6 col-md-8">
			<form id="privateParams" class="form-horizontal" role="form">
				<div id="errors" style="display: none"
							class="alert alert-danger">
				</div>
			
				<div class="form-group">
					<div class="row jumbotron" id="images" style="display:none;" >
					</div>
				</div>
			
				<div class="form-group">
					<label for="photo" class="col-md-3 control-label">Прикрепите
							фотографию или эскиз</label>
					<div class="col-md-9">
						<input id="photo" name="photo" type="file">
					</div>
					
				</div>
				
				<div class="form-group">
					<label for="photo" class="col-md-3 control-label"></label>
					<div class="col-md-9">
						<button id="clearFiles" class="btn btn-info">Очистить</button>
					</div>
				</div>
				
				<div class="form-group">
					<label for="length" class="col-md-3 control-label">Укажите
						длину гарнитура. Описание: Измерьте рулеткой длину стены, где
						будет стоять гарнитур в сантиметрах.</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="length" name="length"
							placeholder="Введите длину гарнитура">
					</div>
				</div>

				<div class="form-group">
					<label for="fasade_material" class="col-md-3 control-label">Укажите
						материал фасадов. Описание: укажите какой материал вы хотели бы
						использовать. ЛДСП, МДФ в Пленке, Пластиковые фасады</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="fasade_material"
							name="fasade_material" placeholder="Укажите материал">
					</div>
				</div>

				<div class="form-group">
					<label for="is_parlor" class="col-md-3 control-label">Укажите
						необходим ли пристенок</label>
					<div class="col-md-9">
						<input type="checkbox" id="is_parlor" name="is_parlor"
							aria-label="...">
					</div>
				</div>

				<div class="form-group">
					<label for="wishes" class="col-md-3 control-label">Укажите
						пожелания по фурнитуре если есть. (Петли, доводчики, системы
						открывания дверей)</label>
					<div class="col-md-9">
						<textarea class="form-control" rows="5" id="wishes" name="wishes"></textarea>
					</div>
				</div>

				<div class="form-group">
					<label for="height" class="col-md-3 control-label">Укажите
						высоту в сантиметрах если нестандартная.</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="height" name="height"
							placeholder="Укажите высоту">
					</div>
				</div>

				<div class="form-group">
					<label for="additional_wishes" class="col-md-3 control-label">Дополнительные
						пожелания к изделию. Описание: укажите ваши пожелания в этом поле
						для более точного подсчета изготовления. </label>
					<div class="col-md-9">
						<textarea class="form-control" rows="5" id="additional_wishes"
							name="additional_wishes"></textarea>
					</div>
				</div>
				<%if(appUser == null){ %>
				<hr>
				<div class="form-group">
					<label for="user_email" class="col-md-3 control-label">Ваша Почта</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="user_email" name="user_email"
							placeholder="Введите почту">
					</div>
				</div>

				<div class="form-group">
					<label for="user_name" class="col-md-3 control-label">Ваше Имя</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="user_name" name="user_name"
							placeholder="Введите имя">
					</div>
				</div>

				<div class="form-group">
					<label for="user_phone" class="col-md-3 control-label">Ваш Номер
						телефона</label>
					<div class="col-md-9">
						<input type="text" class="form-control" id="user_phone" name="user_phone"
							placeholder="Введите номер телефона">
					</div>
				</div>
				<%} %>
				<div class="form-group">
				<label for="sendInfo" class="col-md-3 control-label"></label>
					<div class="col-md-offset-3 col-md-9">
						<button id="btn-post-info" name="sendInfo" class="btn btn-info">
							<i class="icon-hand-right"></i> &nbsp Отправить информацию компаниям
						</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
function removeIMG(elem){
	if(elem === undefined)
		return;
	elem.remove();
}
	$(document).ready(function() {
		$('#privateParams').submit(false);
		
		$('#photo').on('change', function() {
		    var file = this.files[0];
		    var savedFiles = $(".savedImages").length;

		    if(savedFiles > 3){
		    	return;
		    }
		    
		    $.get("/get_upload", function(upload_url) {
				if(upload_url === undefined){
					console.log("Some error ocured during getting upload url");
					return;
				}
			    
				$.ajax({
		            url: upload_url,
		            type: 'POST',
		            data: new FormData($('#privateParams')[0]),
		            cache: false,
		            contentType: false,
		            processData: false,
		            async: false,
		            success: function (src) {
		            	var imgJUMB = $("#images");
		            	imgJUMB.css("display", "block");
		            	imgJUMB.append('<div class="removable col-xs-6 col-md-3" onclick="removeIMG(this)">'
							   + '<a  class="thumbnail">'
							   +'<img class="savedImages" src="' + src + '" alt="...">'
							   +'</a></div>');
		            },
		            error: function(msg){
		            	alert(msg);
		            }
		        });
				
			});
		});

		$("#user_email").on('input', function() {
			$.post("/verify", {
				user_email: $("#user_email").val()
			}, function(data) {
				if(data.message !== undefined){
					setButtonEnabled(true);
					printErrorMsg(data.message);
				}else{
					clearErrorMsg();
					setButtonEnabled(false);
				}
			});
		});
		
		elementClickHandler($("#user_name"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_name",
				value : val
			}, function(data) {
				if(data.error != undefined)
					alert(data.message);
			});
		});
		elementClickHandler($("#user_phone"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_phone",
				value : val
			}, function(data) {
				if(data.error != undefined)
					alert(data.message);
			});
		});
		elementClickHandler($("#user_email"), function(val) {
			$.post("/private", {
				mode : "edit_private_info",
				property : "user_email",
				value : val
			}, function(data) {
				if(data.error != undefined)
					alert(data.message);
			});
		});

		$("#btn-post-info").click(function(){
			try {
				var files = getImages();
				var length = $("#length").val();
				var fasade_material = $("#fasade_material").val();
				var is_parlor = $("#is_parlor").is(":checked");
				var wishes = $("#wishes").val();
				var height = $("#height").val();
				var additional_wishes = $("#additional_wishes").val();
				
				var user_email = $("#user_email").val();
				var user_phone = $("#user_phone").val();
				var user_name = $("#user_name").val();

				$.post("/private", {
					mode : "post_user_info",
					images : files,
					length : length,
					fasade_material : fasade_material,
					is_parlor : is_parlor,
					wishes : wishes,
					height : height,
					additional_wishes : additional_wishes,
					
					user_email: user_email,
					user_phone: user_phone,
					user_name: user_name
				}, function(data){
					if(data.error != undefined){
						printErrorMsg(data.message);
					}
					else{
						alert(data.message);
						location.reload();
					}
				}).fail( function(jqXHR, textStatus, errorThrown) {
				    alert("ERROR " + textStatus);
				});
				
			} catch (e) {
				console.log("Some error occured gathering info parameters");
			}
		});
		
		$("#clearFiles").click(function(){
			$("#images").empty();
			$("#photo").val("");
		});
		
		$(".removable").click(function(){
			removeIMG($(this));
		});
		
	});
</script>
<%@ include file="footer.jsp"%>