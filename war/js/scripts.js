function answersPaginationClick(answerPage){
	event.preventDefault();
	setGetParameter("answerPage", answerPage);	
}

function usersPaginationClick(usersPage){
	event.preventDefault();
	setGetParameter("userPage", usersPage);	
}

function ordersPaginationClick(ordersPage){
	event.preventDefault();
	setGetParameter("orderPage", ordersPage);	
}


function setGetParameter(paramName, paramValue){
    var url = window.location.href;
    var hash = location.hash;
    url = url.replace(hash, '');
    if (url.indexOf(paramName + "=") >= 0)
    {
        var prefix = url.substring(0, url.indexOf(paramName));
        var suffix = url.substring(url.indexOf(paramName));
        suffix = suffix.substring(suffix.indexOf("=") + 1);
        suffix = (suffix.indexOf("&") >= 0) ? suffix.substring(suffix.indexOf("&")) : "";
        url = prefix + paramName + "=" + paramValue + suffix;
    }
    else
    {
    if (url.indexOf("?") < 0)
        url += "?" + paramName + "=" + paramValue;
    else
        url += "&" + paramName + "=" + paramValue;
    }
    window.location.href = url + hash;
}

function elementClickHandler(elem, callback) {
	elem
			.click(function() {
				var span = elem.children().first();

				if (span.prop('tagName') !== "SPAN")
					return;

				var val = span.text();
				callback(val);

				span.remove();
				elem
						.append("<input class='custom-input form-control' style='display:inline;width: 150px;' type='text' value='"
								+ val + "' />");
			});

	elem.keypress(function(e) {
		if (e.which == 13) {
			var input = elem.children().first();

			if (input.prop('tagName') !== "INPUT")
				return;

			var val = input.val();
			callback(val);

			input.remove();
			elem.append("<span class='label label-info'>" + val + "</span>");
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
