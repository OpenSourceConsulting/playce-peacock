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
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/jquery-validate/jquery.validate.min.js"></script>
<style>
body {
	font-family: "Trebuchet MS", "Helvetica", "Arial",  "Verdana", "sans-serif";
	font-size: 80%;
}

input.pass {width: 98%;}

#resizable {
	width: 350px;
	height: 180px;
	padding: 0.5em;
}

#resizable h3 {
	text-align: center;
	margin: 0;
}

#resizable h2 {
	text-align: center;
	margin: 30px 0 0 0 ;
}

div.error { display: none; }
input:focus { border: 1px dotted black; }
input.error { border: 1px dotted red; }
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
	
	var contextPath = "<%=request.getContextPath()%>";
	
	$.validator.setDefaults({
		submitHandler: function() {
			$.ajax({
				  type: "POST",
				  url: contextPath + "/alm/project/user/passwordreset",
				  contentType : "application/json; charset=UTF-8",
				  data: JSON.stringify({ key: $("#key").val(), password: $("#password").val(), passwordCheck: $("#passwordCheck").val() }),
				  dataType: "json"
			})
			.done(function(  data, textStatus, jqXHR ) {
				  //alert( data.msg );
				$("#loginForm").replaceWith( "<h2>"+data.msg+"</h2>" );
			})
			.fail(function(jqXHR, textStatus) {
			    alert( "Request failed: " + textStatus );
			});
			
		}
	});
	
	
	$(function() {
		
		$("#resizable").resizable().draggable({ 
	    	cursor: "move",
	    	handle: "h3" 
	    }).center();
		
		// validate the comment form when it is submitted
		$("#loginForm").validate({
			rules: {
				passwordCheck: {
					required : true,
					equalTo: "#password"
				}
			},
			messages: {
				password: {
					required : "비밀번호를 입력해주세요.",
				},
				passwordCheck: {
					required : "비밀번호를 다시한번 입력해주세요.",
					equalTo: "같은 비밀번호를 입력해주세요. "
				}
			}
		});
		

	});
</script>
</head>
<body>
	<div id="resizable" class="ui-widget-content">
		<h3 class="ui-widget-header">Reset User</h3>
		<form id="loginForm">
			<input type="hidden" id="key" name="key" value="<%= request.getParameter("key") %>">
			<table width="100%" style="margin: 30px 0 0 0">
				<tbody>
					<!-- 
					<tr>
						<td width="35%">사용자 아이디 : </td>
						<td><input type="text" id="loginId" value=""/></td>
					</tr>
					 -->
					<tr>
						<td>비밀번호: </td>
						<td><input type="password" id="password" name="password" value="" class="pass" required/></td>
					</tr>
					<tr>
						<td>비밀번호 한번더: </td>
						<td><input type="password" id="passwordCheck" name="passwordCheck" value="" class="pass"/></td>
					</tr>
					<tr>
						<td colspan="2" class="btnTd" align="center">
							<input class="submit" type="submit" value="submit" style="margin: 10px 0 0 0;">
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>