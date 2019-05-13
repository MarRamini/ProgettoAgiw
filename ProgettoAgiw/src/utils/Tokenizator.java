package utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Tokenizator {
	
	private static Tokenizator instance = new Tokenizator();
	
	private Tokenizator() {}
	
	public static Tokenizator instance() {
		return instance;
	}
	
	public List<String> Tokenize(String text){
		List<String> tokens = new ArrayList<String>();
		
		for(int i=0 ; i<text.length() ; i++){
			if(text.charAt(i) == '<'){
				String token = "";
				while(text.charAt(i) != '>' && i < text.length()){
					token = token.concat(String.valueOf(text.charAt(i)));
					i++;
				}
				token = token.concat(String.valueOf(text.charAt(i)));
				tokens.add(token);
			}
			else{
				String token = "";
				while(text.charAt(i) != '<' && i < text.length()){
					token = token.concat(String.valueOf(text.charAt(i)));
					i++;
				}
				i--;
				tokens.add(token);
			}
		}
		
		return tokens;
	}	
}