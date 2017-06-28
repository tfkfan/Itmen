<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>
<%@ page import="com.itmencompany.datastore.entities.AppUser"%>
<%@ page import="com.itmencompany.datastore.entities.UserOrder"%>
<%@ page import="com.itmencompany.datastore.entities.IncomingInfo"%>
<%@ page import="com.itmencompany.datastore.dao.AppUserDao"%>
<%@ page import="com.itmencompany.datastore.dao.UserOrderDao"%>
<%@ page import="com.itmencompany.datastore.dao.IncomingInfoDao"%>
<%@ page import="com.itmencompany.helpers.AppUserHelper"%>
<%@ page import="com.itmencompany.common.UserInfo"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="java.util.Date"%>
<%
	Logger log = Logger.getLogger("admin.jsp");
%>
<%@ include file="header.jsp"%>
<%
	Double limit = 5.0d;

	String userPageObj = request.getParameter("userPage");
	String orderPageObj = request.getParameter("orderPage");
	String chosenUserIdObj = request.getParameter("chosenUser");

	Integer userPageNum = 1;
	Integer orderPageNum = 1;
	Long chosenUserId = null;
	try {
		userPageNum = Integer.parseInt(userPageObj);
	} catch (Exception e) {

	}
	try {
		orderPageNum = Integer.parseInt(orderPageObj);
	} catch (Exception e) {

	}
	try {
		chosenUserId = Long.parseLong(chosenUserIdObj);
	} catch (Exception e) {

	}

	AppUserDao userDao = new AppUserDao(AppUser.class);
	UserOrderDao orderDao = new UserOrderDao(UserOrder.class);
	IncomingInfoDao answerDao = new IncomingInfoDao();

	List<AppUser> users = userDao.getWithOffset(userPageNum, limit.intValue());
	List<UserOrder> orders = null;
	List<IncomingInfo> answers = null;
	Integer intLimit = limit.intValue();
	
	AppUser chosenUser = null;
	if (chosenUserId == null)
		orders = orderDao.getWithOffset(orderPageNum, limit.intValue());
	else {
		chosenUser = userDao.get(chosenUserId);
		orders = orderDao.getWithOffsetAndProperty(orderPageNum, limit.intValue(), "userId", chosenUserId);
	}
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
	df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
%>

<div class="custom-container">
	<div class="page-header">
		<h2>Панель администратора</h2>
	</div>

	<div class="row">
		<div class="col-xs-12 col-md-12">
			<fieldset class="form-group">
				<legend>Пользователи</legend>
				<table class="table table-striped">

					<thead>
						<tr>
							<th>#</th>
							<th>Выбрать</th>
							<th>Имя</th>
							<th>E-mail</th>
							<th>Администратор</th>
							<th>Редактировать</th>
							<th>Удалить</th>
						</tr>
					</thead>
					<tbody id="usersTable">
						<%
							for (int index = 0; index < users.size(); index++) {
								AppUser user = users.get(index);
						%>
						<tr id="<%=user.getId()%>">
							<th scope="row"><%=(index + 1)%></th>
							<td>
								<%
									if (user.getId().equals(chosenUserId)) {
								%> <a href="" onclick="choseUser(<%=user.getId()%>)"
								class="btn btn-success"> <span
									class=" glyphicon glyphicon-ok" aria-hidden="true"></span>
							</a> <%
								 	} else {
								 %> <a href="" onclick="choseUser(<%=user.getId()%>)"
								class="btn btn-info"> <span
									class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
									<%
										}
									%>
							</a>
							</td>
							<td><%=user.getUserName()%></td>
							<td><%=user.getEmail()%></td>
							<td><input type="checkbox" id="is_admin" name="is_admin"
								aria-label="..." <%=user.getIsAdmin() ? "checked" : ""%>
								onclick="isAdminChange(<%=user.getId()%>, this)"></td>
							<td><button class="btn btn-info"
									onclick="editUser(<%=user.getId()%>)">
									<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
								</button></td>
							<td><button class="btn btn-danger"
									onclick="deleteUser(<%=user.getId()%>)">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
								</button></td>
						</tr>
						<%
							}
						%>
					</tbody>

				</table>
			</fieldset>
			<nav aria-label="Page navigation">
				<ul class="pagination">
					<%
						Integer usersCount = ((Double) Math.ceil((userDao.getCount()) / limit)).intValue();
						for (Integer pageValue = 1; pageValue <= usersCount; pageValue++) {
					%>
					<li <%if (pageValue.equals(userPageNum)) {%> class="active" <%}%>><a
						href="" onclick="usersPaginationClick(<%=pageValue%>)"><%=pageValue%></a></li>
					<%
						}
					%>
				</ul>
			</nav>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-6 col-md-6">
			<fieldset class="form-group">
				<legend>Заявки пользователей</legend>
				<table class="table table-striped">

					<thead>
						<tr>
							<th>#</th>
							<th>Имя</th>
							<th>E-mail</th>
							<th>Дата</th>
							<th>Просмотреть</th>
							<th>Удалить</th>
						</tr>
					</thead>
					<tbody id="ordersTable">
						<%
							int count = 1;
							for (UserOrder order : orders) {

								Long userId = order.getUserId();

								AppUser user = userDao.get(userId);
								String date = df.format(new Date(order.getDate()));
						%>
						<tr id="<%=order.getId()%>">
							<th scope="row"><%=(count)%></th>
							<td><%=user.getUserName()%></td>
							<td><%=user.getEmail()%></td>
							<td><%=date%></td>
							<td><button class="btn btn-info"
									onclick="showOrder(<%=order.getId()%>)">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</button></td>
							<td><button class="btn btn-danger"
									onclick="deleteOrder(<%=order.getId()%>)">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
								</button></td>
						</tr>
						<%
							count++;
							}
						%>
					</tbody>

				</table>
			</fieldset>
			<nav aria-label="Page navigation">
				<ul class="pagination">
					<%
						Integer ordersCount = ((Double) Math.ceil((orderDao.getCount()) / limit)).intValue();
						for (Integer pageValue = 1; pageValue <= ordersCount; pageValue++) {
					%>
					<li <%if (pageValue.equals(orderPageNum)) {%> class="active" <%}%>><a
						href="" onclick="ordersPaginationClick(<%=pageValue%>)"><%=pageValue%></a></li>

					<%
						}
					%>
				</ul>
			</nav>
		</div>
		<div class="col-xs-6 col-md-6">
			<h:CompanyAnswers limit="<%=intLimit%>"/>
		</div>
	</div>

	<!-- Modal -->
	<div class="modal fade" id="userEdit" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Пользователь</h4>
				</div>
				<div class="modal-body">
					<form>
						<!-- hidden -->
						<input style="display: none" type="text" class="form-control"
							id="id">
						<!--  -->
						<div class="form-group">
							<label for="user_name" class="control-label">Имя</label> <input
								type="text" class="form-control" id="user_name">
						</div>

						<div class="form-group">
							<label for="user_phone" class="control-label">Номер
								телефона</label> <input type="text" class="form-control" id="user_phone">
						</div>

						<div class="form-group">
							<label for="user_email" class="control-label">Email</label> <input
								type="text" class="form-control" id="user_email">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="btn-save-user">Сохранить</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Modal -->
	<div class="modal fade" id="orderShow" tabindex="-1" role="dialog"
		aria-labelledby="orderModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="orderModalLabel">Заявка
						пользователя компаниям</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<label for="order_id" class="control-label">Идентификатор</label> 
							<input type="text" class="form-control"
								id="order_id" disabled>
						</div>
						
						<div class="form-group">
							<label for="user_name" class="control-label">Имя
								пользователя</label> <input type="text" class="form-control"
								id="order_user_name" disabled>
						</div>

						<div class="form-group">
							<label for="user_email" class="control-label">Email
								пользователя</label> <input type="text" class="form-control"
								id="order_user_email" disabled>
						</div>

						<div class="form-group">
							<label for="date" class="control-label">Дата заявки</label> <input
								disabled type="text" class="form-control" id="date">
						</div>


						<div class="form-group">
							<label for="length" class="control-label">Длина гарнитуры</label>
							<input type="text" class="form-control" id="length" name="length"
								disabled>
						</div>

						<div class="form-group">
							<label for="fasade_material" class="control-label">Материал
								фасадов</label> <input type="text" class="form-control"
								id="fasade_material" name="fasade_material" disabled>
						</div>

						<div class="form-group">
							<label for="is_parlor" class="control-label">Пристенок</label> <input
								type="checkbox" id="is_parlor" name="is_parlor" aria-label="..."
								disabled>
						</div>

						<div class="form-group">
							<label for="wishes" class="control-label">Пожелания по
								фурнитуре</label>
							<textarea class="form-control" rows="5" id="wishes" name="wishes"
								disabled></textarea>
						</div>

						<div class="form-group">
							<label for="height" class="control-label">Высота</label> <input
								type="text" class="form-control" id="height" name="height"
								disabled>
						</div>

						<div class="form-group">
							<label for="additional_wishes" class="control-label">Дополнительные
								пожелания к изделию</label>
							<textarea class="form-control" rows="5" id="additional_wishes"
								name="additional_wishes" disabled></textarea>
						</div>

						<div class="form-group">

							<div class="row jumbotron" id="images"></div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
				</div>
			</div>
		</div>
	</div>	
</div>

<script>
function choseUser(id){
    event.preventDefault();
	setGetParameter("chosenUser", id);	
}

function isAdminChange(id, elem){
	$.post("/save_user", {
		user_id : id,
		isAdmin : elem.checked
	}, function(data) {
		window.reload();
	});
}
function editUser(id){
	$.post("/get_user", {
		user_id : id
	}, function(json) {
		$("#id").val(id);
		$("#user_name").val(json["username"]);
		$("#user_phone").val(json["phone"]);
		$("#user_email").val(json["email"]);
		$("#userEdit").modal('show');
	});
}

function deleteUser(id){
	$.post("/delete_user", {
		user_id : id
	}, function(data) {
		location.reload();
	});
}

function saveUser(){
	var id = $("#id").val();
	$.post("/save_user", {
		user_id : id,
		username : $("#user_name").val(),
		phone : $("#user_phone").val(),
		email : $("#user_email").val()
	}, function(data) {
		//TODO change table row
		$("#userEdit").modal('hide');
	});
}

function deleteOrder(id){
	$.post("/delete_order", {
		order_id : id
	}, function(data) {
		location.reload();
	});
}

function showOrder(id){
	$.post("/get_order", {
		order_id : id
	}, function(json1) {
		$.post("/get_user", {
			user_id : json1["userId"]
		}, function(json2) {
			$("#order_user_name").val(json2["username"]);
			$("#order_user_email").val(json2["email"]);
			
			$("#order_id").val(json1["Id"]);
			
			$("#date").val(json1["date"]);
			
			var campaignsElem = $("#campaigns");
			campaignsElem.empty();
			var campaignsList = json1["campaigns"];
			for(var key in campaignsList){
				var campaign = campaignsList[key];
				campaignsElem.append('<li class="list-group-item">' + campaign + '</li>');
			}
	
			var info = JSON.parse(json1["userInfo"]);
			$("#length").val(info["length"]);
			$("#height").val(info["height"]);
			$("#wishes").val(info["wishes"]);
			$("#additional_wishes").val(info["additional_wishes"]);
			var is_parlor = info["is_parlor"] !== "" && info["is_parlor"] != undefined ? info["is_parlor"] : false;
			$("#is_parlor").attr("checked", is_parlor);
			$("#fasade_material").val(info["fasade_material"]);
			
			var images = info["images"];
			
			var imgJUMB = $("#images");
			imgJUMB.empty();
        	for(var key in images){
        		var src = images[key];
        		imgJUMB.append('<div><a  class="thumbnail">'
				   +'<img class="savedImages" src="' + src + '" alt="...">'
				   +'</a></div>');
        	}
        	
			$("#orderShow").modal('show');
		}).fail( function(jqXHR, textStatus, errorThrown) {
		    alert("#2" + textStatus);
		});

	}).fail( function(jqXHR, textStatus, errorThrown) {
	    alert("#1" + textStatus);
	});
}


$(document).ready(function() {
	$("#btn-save-user").click(function(){
		saveUser();
	});
	
	
});
</script>
<%@ include file="footer.jsp"%>