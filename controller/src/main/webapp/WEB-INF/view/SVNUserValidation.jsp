<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>peacock :: svn user check</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-1.11.1.min.js"></script>
<style>
tr.ing td.prjCode { font-weight: bold; }
tr.error td, td.error { color: red; }
td.valid { color: blue; }

</style>
<script type="text/javascript">
	var contextPath = "<%=request.getContextPath()%>";

	$(function() {
		$("#checkBtn").click(function(){
			//alert("111");
			
			$("table tbody tr").each( function( i, el ) {
					
				  //alert($(".prjCode", $(el)).text());
				  
				  $(el).addClass("ing");
				  var groupname = $(".prjCode", $(el)).text();
				  var repoCode = $(".repoCode", $(el)).text();
				  
				    $.ajax({
				    	  async: false,
						  type: "GET",
						  url: contextPath + "/alm/groupmanagement/"+groupname+"/users",
						  //url: contextPath + "/svn/uvalid/"+groupname+"/users",
						  dataType: "json"
					})
					.done(function(  data, textStatus, jqXHR ) {
						
						if(data.success){
							$.each(data.list, function(i, user){
								$("<tr class='"+groupname+user.name+"'><td>"+user.name+"</td><td class='check_result'></td><td class='chk'></td></tr>").appendTo("table."+groupname);
								
								checkUser(groupname, repoCode, user.name);
							});
						}else{
							$(el).addClass("error");
							$(".result", $(el)).text(data.msg);
						}
						
					})
					.fail(function(jqXHR, textStatus) {
					    //alert( "Request failed: " + textStatus );
						$(el).addClass("error");
						$(".result", $(el)).text("Request failed: " + textStatus);
					});
				  
				  
				  $(el).removeClass("ing");
				  
				  //return i < 10;// until loop.
			});
			
		});
	});
	
	function checkUser(groupname, repoCode, username){
		
		$("tr."+groupname+username+" td.chk button").remove();
		
		var repoPath = repoCode;
		//var repoPath = "SS_인수인계";
		
		var result_td = $("tr."+groupname+username+" td.check_result");
		
		$.ajax({
	    	  async: false,
			  type: "POST",
			  url: contextPath + "/svn/uvalid/check",
			  data: {
				  //url : "http://localhost/svn/share/"+repoPath,
				  url : "svn://svn.hiway.hhi.co.kr/"+repoPath+"/"+groupname,
				  uname : username,
				  pass : username.substring(1)
			  },
			  dataType: "json"
		})
		.done(function(  data, textStatus, jqXHR ) {
			
			if(data.success){
				result_td.addClass("valid");
			}else{
				
				result_td.addClass("error");
				$("<button onclick=\"checkUser('"+groupname+"','"+repoCode+"','"+username+"');\">check</button>").appendTo("tr."+groupname+username+" td.chk");
			}
			result_td.text(data.msg);
			
		})
		.fail(function(jqXHR, textStatus) {
		    //alert( "Request failed: " + textStatus );
		    result_td.addClass("error").text("Request failed: " + textStatus);
		    $("<button onclick=\"checkUser('"+groupname+"','"+repoCode+"','"+username+"');\">check</button>").appendTo("tr."+groupname+username+" td.chk");
		});
	}
</script>

</head>
<body>
	<button id="checkBtn">start check!!</button>
	<table border="1">
		<thead>
			<tr>
				<th>No</th>
				<th>Project ID</th>
				<th>Repository Code</th>
				<th>Users</th>
				<th>Result</th>
			</tr>
		</thead>
		<c:forEach var="prj" items="${projects}" varStatus="status">
		<tbody>
			<tr>
				<td>${status.count}</td>
				<td class="prjCode">${prj.projectCode}</td>
				<td class="repoCode">${prj.repositoryCode}</td>
				<td class="users">
					<table class="${prj.projectCode}" border="1">
						
					</table>
				</td>
				<td class="result"></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>