<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>peacock</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/jquery-ui-1.11.1.redmond/jquery-ui.min.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/jquery-ui-1.11.1.redmond/jquery-ui.min.js"></script>
<style>
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 80%;
}

input {width: 98%;}

#resizable {
	width: 350px;
	height: 200px;
	padding: 0.5em;
}

#resizable h3 {
	text-align: center;
	margin: 0;
}
</style>
<script>

jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + 
                                                $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + 
                                                $(window).scrollLeft()) + "px");
    return this;
};
	$(function() {
		$("#resizable").resizable().draggable({ 
	    	cursor: "move",
	    	handle: "h3" 
	    }).resize(function(){
	    	$( "button" ).position({
				of : $("td.btnTd"),
				my : "center top",
				at : "center center"
			});
	    }).center();
		
		$( "button" ).position({
			of : $("td.btnTd"),
			my : "center top",
			at : "center center"
		});
		
		$("#submitBtn").click(function(){
			if($("#loginId").val() == ""){
				alert("사용자 아이디를 입력해주세요.");
				$("#loginId").focus();
				return;
			}
			
			if($("#password").val() == ""){
				alert("비밀번호를 입력해주세요.");
				$("#password").focus();
				return;
			}
			
			if($("#password2").val() == ""){
				alert("비밀번호를 한번더 입력해주세요.");
				$("#password2").focus();
				return;
			}
			
			if($("#password").val() != $("#password2").val()){
				alert("비밀번호가 다릅니다.");
				$("#password2").select();
				return;
			}
		});

	});
</script>
</head>
<body>
	<div id="resizable" class="ui-widget-content">
		<h3 class="ui-widget-header">Reset User</h3>
		<table width="100%" style="margin: 30px 0 0 0">
			<tbody>
				<tr>
					<td width="35%">사용자 아이디 : </td>
					<td><input type="text" id="loginId" value=""/></td>
				</tr>
				<tr>
					<td>비밀번호: </td>
					<td><input type="password" id="password" value=""/></td>
				</tr>
				<tr>
					<td>비밀번호 한번더: </td>
					<td><input type="password" id="password2" value=""/></td>
				</tr>
				<tr>
					<td colspan="2" class="btnTd">
						<button id="submitBtn">submit</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>