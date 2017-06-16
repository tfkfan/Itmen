<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="com.itmencompany.beans.CompanyAnswersBean" %>
<%@ tag import="com.itmencompany.datastore.entities.IncomingInfo" %>
<%@ tag import="com.itmencompany.datastore.entities.AppUser" %>
<%@ tag import="com.itmencompany.datastore.dao.AppUserDao" %>
<%@ tag import="com.itmencompany.datastore.dao.IncomingInfoDao" %>
<%@ tag import="java.util.logging.Logger"%>
<%@ tag import="java.util.List"%>
<%@ tag import="java.text.SimpleDateFormat"%>
<%@ attribute name="chosenUserId" type="java.lang.Long" required="true" %>
<%@ attribute name="answerPageNum" type="java.lang.Integer" required="true" %>
<%@ attribute name="limit" type="java.lang.Integer" required="true" %>
<%
	Logger log = Logger.getLogger("CompanyAnswers.tag");
	CompanyAnswersBean answersBean = new CompanyAnswersBean();

	AppUserDao usersDao = new AppUserDao(AppUser.class);
	IncomingInfoDao answerDao = new IncomingInfoDao();
	
	if(limit.equals(0))
		limit = 5;
	
	List<IncomingInfo> answers = answersBean.getAnswers(chosenUserId, limit, answerPageNum, answerDao);
	
	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yy HH:mm");
%>
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
				<th>Удалить</th>
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
				<td><button class="btn btn-danger"
						onclick="deleteAnswer(<%=answer.getId()%>)">
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
			Integer answersCount = ((Double) Math.ceil((answerDao.getCount()) / limit)).intValue();
			for (Integer pageValue = 1; pageValue <= answersCount; pageValue++) {
		%>
		<li <%if (pageValue.equals(answerPageNum)) {%> class="active" <%}%>><a
			href="" onclick="answersPaginationClick(<%=pageValue%>)"><%=pageValue%></a></li>

		<%
			}
		%>
	</ul>
</nav>

