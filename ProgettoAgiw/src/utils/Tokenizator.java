package utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.HtmlToken;
import model.InformationToken;
import model.Token;

public class Tokenizator {
	
	private static Tokenizator instance = new Tokenizator();
	
	private Tokenizator() {}
	
	public static Tokenizator Instance() {
		return instance;
	}
	
	public List<Token> Tokenize(String text){
		List<Token> result = new ArrayList<Token>();
		
		for(int i=0 ; i<text.length() ; i++) {
			Token token = null;
			if(text.charAt(i) == '<') {
				
			}
			else {
				String information = new String();
				while(i<text.length() && text.charAt(i) != '<') {
					information = information.concat(String.valueOf(text.charAt(i)));
					i++;
				}
				token = new InformationToken(information);
			}
			
			if(token != null) {
				result.add(token);
			}
		}	
		
		return result;
	}
}
