<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="Tokenizator.js"></script>
<title>Insert title here</title>
</head>
<body>
<div id="container">
<button type="button" onclick="doTex()"> click me to run TEX </button>
</div>
<script type="text/javascript">
	var siteList = ["http://localhost:8080/ProgettoAgiw1/t1.html","http://localhost:8080/ProgettoAgiw1/t2.html","http://localhost:8080/ProgettoAgiw1/t3.html"];
	var htmlDocuments = [];
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
		    	var documentsTokenized = [];
		    	var response = this.responseText.trim();
		    	var tokens = tokenize(response)
		    	htmlDocuments.push(tokens);
		    	var jsonTokens = {tokens: tokens}; 
		    	jsonTokens = JSON.stringify(jsonTokens);
		    	console.log(jsonTokens)
		    
		    	var xhr = new XMLHttpRequest();
		    	xhr.onreadystatechange = function() { 
		    	     if (xhr.readyState == 4 && xhr.status == 200){
		    	    	document.getElementById("response").innerHTML = this.responseText;
		    	     }
		    	 };
		    	
		    	 xhr.open("POST", "http://localhost:8080/ProgettoAgiw1/Tex", true); // true for asynchronous 
		    	 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		    	 xhr.send("textPage=" + encodeURIComponent(jsonTokens));
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
    	
    	 xhr.open("GET", "http://localhost:8080/ProgettoAgiw1/Tex", true); // true for asynchronous 
  		 xhr.send();
	}
</script>
<div id="response"></div>
</body>
</html>