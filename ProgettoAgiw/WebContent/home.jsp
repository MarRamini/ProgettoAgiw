<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Insert title here</title>
	</head>
	<body>	
		<div class="inputContainer">
			<form method="post" action="Tex">
				<span class="text">Inserire la cartella contenente il dataset</span>
				<input type="text" class="pathInput" id="datasetPath" name="datasetPath"></input>
				<input type="submit" value="eseguiTex"></input>	
			</form>
		</div>
	</body>
</html>