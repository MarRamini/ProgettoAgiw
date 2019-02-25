<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div id="container">
<button type="button" onclick="doTex()"> click me to run TEX </button>
</div>
<script type="text/javascript">
	var siteList = ["http://localhost:8080/ProgettoAgiw/t1.html","http://localhost:8080/ProgettoAgiw/t2.html","http://localhost:8080/ProgettoAgiw/t3.html"];
	

	siteList.forEach(function (url){		
		var format="application/json";
		var params={
			"default-graph": "", "should-sponge": "soft",
			"debug": "on", "timeout": "", "format": format,
			"save": "display", "fname": ""
		};
		
		var querypart = "";
		for(var param in params) {
			querypart += param + "=" + encodeURIComponent(params[param]) + "&";
		}
		var queryURL = url + '?' + querypart;
		
		if (window.XMLHttpRequest) {
		  	var xmlHttp = new XMLHttpRequest();
		  }
		 else {
		  	var xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		 }
		
		 xmlHttp.onreadystatechange = function() { 
		     if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
		    	
		    	var response = this.responseText;	    	
		    	siteList.push(response);
		    
		    	var xhr = new XMLHttpRequest();
		    	xhr.onreadystatechange = function() { 
		    	     if (xhr.readyState == 4 && xhr.status == 200){
		    	    	var div = document.createElement("div");
		    	    	div.style.height = "10px";
		    	    	div.style.width = "10px";
		    	    	div.style.backgroundColor = "green";
		    	    	div.style.display = "inline-block";
		    	    	document.getElementById("container").appendChild(div);
		    	     }
		    	 };
		    	
		    	 xhr.open("POST", "http://localhost:8080/ProgettoAgiw/Tex", true); // true for asynchronous 
		    	 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		    	 console.log(response)
		    	 xhr.send("textPage=" + encodeURIComponent(response));
		     }
		 };
		 
		 xmlHttp.open("GET", queryURL, true); // true for asynchronous 
		 xmlHttp.send();
	});	
</script>


<script type="text/javascript">
	function doTex(){
		var xhr = new XMLHttpRequest();
    	xhr.onreadystatechange = function() { 
    	     if (xhr.readyState == 4 && xhr.status == 200){
    	    	document.getElementById("response").textContent = this.responseText;
    	     }
    	 };
    	
    	 xhr.open("GET", "http://localhost:8080/ProgettoAgiw/Tex", true); // true for asynchronous 
  		 xhr.send();
	}
</script>
<div id="response"></div>
</body>
</html>