/**
 * 
 */

function tokenize(document){
	var tokens = tokenizator(document);
	return tokens;	
}

function tokenizator(document){
	var tokens = [];
	for(var i=0 ; i<document.length ; i++){
		if(document.charAt(i) == "<"){
			var token = "" + document.charAt(i);
			var i = i + 1;
			while(i < document.length && document.charAt(i) != ">"){
				token += document.charAt(i);
				i++;
			}
			token += document.charAt(i);
			tokens.push(token);
		}
		else{
			var token = "" + document.charAt(i);
			var i = i + 1;
			while(i < document.length && document.charAt(i) != "<"){
				token += document.charAt(i);
				i++;
			}
			i--;
			tokens.push(token);
		}
	}
	return tokens;
}