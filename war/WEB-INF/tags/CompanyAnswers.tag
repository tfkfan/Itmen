<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="com.itmencompany.beans.CompanyAnswersBean"%>
<%@ tag import="com.itmencompany.datastore.entities.IncomingInfo"%>
<%@ tag import="com.itmencompany.datastore.entities.AppUser"%>
<%@ tag import="com.itmencompany.datastore.dao.AppUserDao"%>
<%@ tag import="com.itmencompany.datastore.dao.IncomingInfoDao"%>
<%@ tag import="java.util.logging.Logger"%>
<%@ tag import="java.util.List"%>
<%@ tag import="java.text.SimpleDateFormat"%>
<%@ attribute name="limit" type="java.lang.Integer" required="true"%>
<%@ attribute name="isAdminPage" type="java.lang.Boolean"
	required="false"%>
<%@ tag import="com.itmencompany.helpers.AppUserHelper"%>
<%
	AppUser appUser = AppUserHelper.getUserFromRequest(request);
	Logger log = Logger.getLogger("CompanyAnswers.tag");
	CompanyAnswersBean answersBean = new CompanyAnswersBean();
	AppUserDao usersDao = new AppUserDao(AppUser.class);
	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yy HH:mm");

	String chosenUserIdObj = request.getParameter("chosenUser");
	String answerPageObj = request.getParameter("answerPage");
	String showFavoritesObj = request.getParameter("showFavorites");

	if (isAdminPage == null)
		isAdminPage = true;
	
	Long chosenUserId = null;
	try {
		chosenUserId = Long.parseLong(chosenUserIdObj);
	} catch (Exception e) {
		
		log.info("Incorrect chosenUserId in CompanyAnswers.tag");
		if(!isAdminPage)
			chosenUserId = appUser.getId();
	}

	Integer answerPageNum = 1;
	try {
		answerPageNum = Integer.parseInt(answerPageObj);
	} catch (Exception e) {

	}
	Boolean showFavorites = false;
	try {
		showFavorites = Boolean.parseBoolean(showFavoritesObj);
	} catch (Exception e) {

	}
	
	List<IncomingInfo> answers = showFavorites && !isAdminPage
			? answersBean.getFavoriteUserAnswers(chosenUserId, limit, answerPageNum)
			: answersBean.getAnswers(chosenUserId, limit, answerPageNum);

	String opt_action = isAdminPage ? "Удалить" : "Избранное";
	if (!isAdminPage) {
%>
<div class="custom-form-container">
	<div class="row">
		<div class="col-xs-6 col-md-2">
			<div class="form-group">
				<button class="btn btn-default btn-sm" onclick="favoriteAnswers()">Показать
					только избранное</button>
			</div>
		</div>
		<div class="col-xs-6 col-md-2">
			<div class="form-group">
				<button class="btn btn-default btn-sm" onclick="allAnswers()">Показать
					все</button>
			</div>
		</div>
	</div>
	<%
		}
	%>
	<div class="row">
		<fieldset class="form-group">
			<legend>Ответы компаний</legend>
			<table class="table table-striped">

				<thead>
					<tr>
						<th>#</th>
						<th>Имя пользователя</th>
						<th>Имя кампании</th>
						<th>E-mail</th>
						<th>Дата</th>
						<th>Просмотреть</th>
						<th><%=opt_action%></th>
					</tr>
				</thead>
				<tbody id="answersTable">
					<%
						Integer count = 1;
						for (IncomingInfo answer : answers) {
							Long userId = answer.getUserId();
							if (userId == null) {
								log.info(answer.getId() + " IncomingInfo with this ID has not user ID or ID is incorrect");
								continue;
							}
							AppUser user = usersDao.get(userId);
							if (user == null) {
								log.info("user is null for IncomingInfo " + answer.getId());
								continue;
							}

							String date = dateformat.format(answer.getDoModified());
					%>
					<tr id="<%=answer.getId()%>">
						<th scope="row"><%=(count)%></th>
						<td><%=user.getUserName()%></td>
						<td><%=answer.getCompanyTitle()%></td>
						<td><%=answer.getCampaignEmail()%></td>
						<td><%=date%></td>
						<td><button class="btn btn-info"
								onclick="showAnswer(<%=answer.getId()%>)">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</button></td>
						<%
							if (isAdminPage) {
						%>
						<td><button class="btn btn-danger"
								onclick="deleteAnswer(<%=answer.getId()%>)">
								<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
							</button></td>
						<%
							} else {
									if (answer.getIsFavorite()) {
						%>
						<td id="favoriteAnswer<%=answer.getId()%>"><button
								class="btn btn-success"
								onclick="setFavorite(<%=answer.getId()%>, false)">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
							</button></td>
						<%
							} else {
						%>
						<td id="favoriteAnswer<%=answer.getId()%>"><button
								class="btn btn-success"
								onclick="setFavorite(<%=answer.getId()%>, true)">
								<span class="glyphicon glyphicon-star" aria-hidden="true"></span>
							</button></td>
						<%
							}
								}
						%>
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
				
					Integer answersCount = ((Double) Math.ceil(((double) answersBean.getAnswersCount()) / limit)).intValue();
					for (Integer pageValue = 1; pageValue <= answersCount; pageValue++) {
				%>
				<li <%if (pageValue.equals(answerPageNum)) {%> class="active" <%}%>><a
					href="" onclick="answersPaginationClick(<%=pageValue%>)"><%=pageValue%></a></li>
				<%
					}
				%>
			</ul>
		</nav>
	</div>
	<%
		if (!isAdminPage) {
	%>
</div>
<%
	}
%>
<div class="modal fade" id="answerShow" tabindex="-1" role="dialog"
	aria-labelledby="answerModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="orderModalLabel">Ответ компании
					пользователю</h4>
			</div>
			<div class="modal-body">
				<form>
					<div class="form-group">
						<label for="answer_order_id" class="control-label">Идентификатор
							Заявки</label> <input type="text" class="form-control"
							id="answer_order_id" readonly>
					</div>

					<div class="form-group">
						<label for="title" class="control-label">Наименование
							изделия</label> <input type="text" class="form-control" id="answer_title"
							readonly>
					</div>

					<div class="form-group">
						<label for="description" class="control-label">Описание </label>
						<textarea class="form-control" rows="5" id="answer_description"
							name="description" readonly></textarea>
					</div>

					<div class="form-group">
						<label for="height" class="control-label">Высота</label> <input
							readonly type="text" class="form-control" id="answer_height">
					</div>

					<div class="form-group">
						<label for="length" class="control-label">Длина</label> <input
							type="text" class="form-control" id="answer_length" readonly>
					</div>

					<div class="form-group">
						<label for="material" class="control-label">Материал</label> <input
							type="text" class="form-control" id="answer_material"
							name="material" readonly>
					</div>

					<div class="form-group">
						<label for="release" class="control-label">Срок
							изготовления</label> <input class="form-control" type="text"
							id="answer_release" name="release" readonly>
					</div>

					<div class="form-group">
						<label for="cost" class="control-label">Цена</label> <input
							class="form-control" type="text" id="answer_cost" name="cost"
							readonly>
					</div>

					<div class="form-group">
						<label for="additional_info" class="control-label">Дополнительная
							информация об изделии</label>
						<textarea class="form-control" rows="5"
							id="answer_additional_info" name="additional_info" readonly></textarea>
					</div>

					<div class="form-group">
						<label for="phone" class="control-label">Контактный
							телефон</label> <input type="text" class="form-control" id="answer_phone"
							name="phone" readonly>
					</div>

					<div class="form-group">
						<label for="email" class="control-label">Email компании</label> <input
							type="text" class="form-control" id="answer_email" name="email"
							readonly>
					</div>

					<div class="form-group">
						<label for="campaign_title" class="control-label">Название
							компании</label> <input type="text" class="form-control"
							id="answer_campaign_title" name="campaign_title" readonly>
					</div>

					<div class="form-group">
						<div class="row jumbotron" id="answer_images"></div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
			</div>
		</div>
	</div>
</div>


