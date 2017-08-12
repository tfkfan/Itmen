function answersPaginationClick(answerPage) {
	event.preventDefault();
	setGetParameter("answerPage", answerPage);
}

function usersPaginationClick(usersPage) {
	event.preventDefault();
	setGetParameter("userPage", usersPage);
}

function ordersPaginationClick(ordersPage) {
	event.preventDefault();
	setGetParameter("orderPage", ordersPage);
}

function allAnswers() {
	setGetParameter("showFavorites", false);
}

function favoriteAnswers() {
	setGetParameter("showFavorites", true);
}

function setFavorite(id, val) {
	$.post("/edit_answer", {
		answer_id : id,
		set_favorite : val
	}, function(data) {
		var json = JSON.parse(data);
		val = !json["isFavorite"];
		var td = $("td#favoriteAnswer" + id);
		td.empty();
		td.append("<button class='btn btn-success'" + "onclick='setFavorite("
				+ id + ", " + val + ")'>" + "<span class='glyphicon glyphicon-"
				+ (val ? "star" : "remove") + "' aria-hidden='true'></span>"
				+ "</button>");
	});
}

function showAnswer(id) {
	$.post(
			"/get_answer",
			{
				answer_id : id
			},
			function(info) {
				$("#answer_order_id").val(info["order_id"]);
				$("#answer_length").val(info["length"]);
				$("#answer_height").val(info["height"]);
				$("#answer_description").val(info["description"]);
				$("#answer_title").val(info["title"]);
				$("#answer_material").val(info["material"]);
				$("#answer_release").val(info["release_date"]);
				$("#answer_additional_info").val(info["addInfo"]);
				$("#answer_phone").val(info["campaign_phone"]);
				$("#answer_cost").val(info["cost"]);
				$("#answer_campaign_title").val(info["campaign_title"]);
				$("#answer_email").val(info["campaign_email"]);

				var images = info["images"];

				var imgJUMB = $("#answer_images");
				imgJUMB.empty();
				for ( var key in images) {
					var src = images[key];
					imgJUMB.append('<div><a  class="thumbnail">'
							+ '<img class="savedImages" src="' + src
							+ '" alt="...">' + '</a></div>');
				}

				$("#answerShow").modal('show');
			}).fail(function(jqXHR, textStatus, errorThrown) {
		alert("#1" + textStatus);
	});
}

function deleteAnswer(id) {
	$.post("/admin/delete_answer", {
		answer_id : id
	}, function(data) {
		location.reload();
	});
}

function setGetParameter(paramName, paramValue) {
	var url = window.location.href;
	var hash = location.hash;
	url = url.replace(hash, '');
	if (url.indexOf(paramName + "=") >= 0) {
		var prefix = url.substring(0, url.indexOf(paramName));
		var suffix = url.substring(url.indexOf(paramName));
		suffix = suffix.substring(suffix.indexOf("=") + 1);
		suffix = (suffix.indexOf("&") >= 0) ? suffix.substring(suffix
				.indexOf("&")) : "";
		url = prefix + paramName + "=" + paramValue + suffix;
	} else {
		if (url.indexOf("?") < 0)
			url += "?" + paramName + "=" + paramValue;
		else
			url += "&" + paramName + "=" + paramValue;
	}
	window.location.href = url + hash;
}

function editableElemClickHandler(elem, callback) {
	elem.click(function() {
		if(elem.has("input").length)
			return;
		var val = elem.text();
		elem.empty();
		elem.append("<input class='custom-input form-control btn-small' style='display:inline;width: 150px;' type='text' value='"
						+ val + "' />");
	});

	elem.keypress(function(e) {
		if (e.which == 13) {
			var chlds = elem.children();
			if(chlds.length === 0)
				return;
			
			var input = elem.children().first();
			if (input.prop('tagName') !== "INPUT")
				return;

			var val = input.val();
			callback(val);

			input.remove();
			elem.append(val);
			return false;
		}
	});
}

function setButtonEnabled(enabled) {
	$("#btn-save-info").prop('disabled', enabled);
}

function printErrorMsg(msg) {
	var errors = $("#errors");
	errors.empty();
	errors.css("display", "block");
	errors.append("<span>" + msg + "</span>");
}

function clearErrorMsg() {
	$("#errors").css("display", "none");
}

function getImages() {
	var arr = [];
	$(".savedImages").each(function(index) {
		arr.push($(this).attr("src"));
	});
	return arr;
}
$(document).ready(function() {

});
