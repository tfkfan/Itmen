<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.itmencompany.datastore.entities.Campaign"%>
<%@ page import="com.itmencompany.datastore.dao.CampaignDao" %>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.List"%>
<%
	Logger log = Logger.getLogger("campaigns.jsp");
%>
<%@ include file="header.jsp"%>
<%
	if (appUser == null) {
		response.sendRedirect("/login");
		return;
	}
	String pageObj =  request.getParameter("page");
	Integer pageNum = 1;
	try{
		pageNum = Integer.parseInt(pageObj);
	}catch(Exception e){
		
	}
	Double limit = 5.0d;
	
	CampaignDao dao = new CampaignDao(Campaign.class);
	List<Campaign> campaigns = dao.getWithOffset(pageNum, limit.intValue());
%>
<div class="custom-container">
	<div class="page-header">
		<h2>Кампании</h2>
	</div>
	<div class="container">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>#</th>
					<th>Название кампании</th>
					<th>e-mail Кампании</th>
					<th>Редактировать</th>
					<th>Удалить</th>
				</tr>
			</thead>
			<tbody id="campaignsTable">
				<%
					for (int index = 0; index < campaigns.size(); index++) {
						Campaign campaign = campaigns.get(index);
				%>
				<tr id="<%=campaign.getId()%>">
					<th scope="row"><%=(index + 1)%></th>
					<td><%=campaign.getTitle()%></td>
					<td><%=campaign.getEmail()%></td>
					<td><button class="btn btn-info"
							onclick="edit(<%=campaign.getId()%>, '<%=campaign.getTitle()%>', '<%=campaign.getEmail()%>')">
							<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
						</button></td>
					<td><button class="btn btn-danger"
							onclick="remove(<%=campaign.getId()%>)">
							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
						</button></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
		<nav aria-label="Page navigation">
			<ul class="pagination">
				<%
					
					Integer campaignsCount = ((Double)Math.ceil((dao.getCount())/limit)).intValue();
					for(Integer pageValue = 1; pageValue <= campaignsCount; pageValue++){
				%>
					<li <%if(pageValue.equals(pageNum)){%> class="active" <%} %>><a href="/campaigns?page=<%=pageValue%>"><%=pageValue%></a></li>
				
				<%} %>
			</ul>
		</nav>
	</div>
	<hr>

	<div class="container">
		<!-- Button trigger modal -->
		<div class="row">
			<div class="col-md-8">
				<button type="button" class="btn btn-primary btn-lg"
					data-toggle="modal" data-target="#campaignEdit"
					onclick="clearData()">Добавить компанию</button>
			</div>
		</div>

		<!-- Modal -->
		<div class="modal fade" id="campaignEdit" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Добавление компании</h4>
					</div>
					<div class="modal-body">
						<form>
							<!-- hidden -->
							<input style="display: none" type="text" class="form-control"
								id="id">
							<!--  -->
							<div class="form-group">
								<label for="title" class="control-label">Название
									компании</label> <input type="text" class="form-control" id="title">
							</div>

							<div class="form-group">
								<label for="email" class="control-label">Email компании</label>
								<input type="text" class="form-control" id="email">
							</div>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							id="btn-save-campaign">Сохранить</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function clearData(){
		$("#title").val("");
		$("#email").val("");
		$("#id").val("");
	}
	
	function addChangeCampaign(campaign) {
		var table = $("#campaignsTable");
		var tds = $("tr#" + campaign["id"] + " td");
		if(tds.length){
			tds[0].innerHTML = campaign["title"];
			tds[1].innerHTML = campaign["email"];
		}else{
			var max = $("#campaignsTable tr").length;
			table.append("<tr><th scope='row'>" + (max + 1) + "</th><td>"
					+ campaign["title"] + "</td>" + "<td>" + campaign["email"]
					+ "</td>" + "<td style='display: none;'>" + campaign["id"] + "</td>"
			+ "<td><button class='btn btn-info' onclick='edit(" + campaign["id"] + ", '" + campaign["title"] +  "', '" + campaign["email"] + "')'>"
			+ "<span class='glyphicon glyphicon-pencil' aria-hidden='true'></span>"
			+ "</button></td><td><button class='btn btn-danger'>"
			+ "<span class='glyphicon glyphicon-trash' aria-hidden='true'></span></button></td></tr>");
		}
	}

	function remove(id){
		$.post("/campaigns", {
			mode : "to_delete",
			id : id
		}, function(data) {
			$("tr#" + id).remove();
			
		});
	}
	
	function edit(id, title, email) {
		$("#title").val(title);
		$("#email").val(email);
		$("#id").val(id);
		$("#campaignEdit").modal('show');
	}

	$(document).ready(function() {
		$("#btn-save-campaign").click(function() {
			var title = $("#title").val();
			var email = $("#email").val();
			var id = $("#id").val();
			$.post("/campaigns", {
				mode : "to_save",
				title : title,
				email : email,
				id : id
			}, function(data) {
				addChangeCampaign(data);
				$("#campaignEdit").modal('hide');
			});
		});
	});
</script>
<%@ include file="footer.jsp"%>