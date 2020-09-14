$(document).ready(function() {
	$("button").click(function() {
		$.ajax({
			type : 'POST',
			url : '/shortenurl',
			data : $("#urlinput").val(),
			contentType: 'application/json; charset=utf-8',
			success : function(data) {
				$("#shorturltext").val(data);
			}
		});
	});
});