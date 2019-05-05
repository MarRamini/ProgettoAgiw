package utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.HtmlToken;
import model.InformationToken;
import model.Token;

public class Tokenizator {
	
	private static Tokenizator instance = new Tokenizator();
	
	private Tokenizator() {}
	
	public static Tokenizator Instance() {
		return instance;
	}
	
	//passaggi pulizia html
	//1)trim
	//2)rimozione script
	//2)match e rimozione html
	//3)il resto è informazione
	
	public List<Token> Tokenize(String text){
		//regex per match html (\\<\\/?\\!?)(\\w+)\\s?(.*?)(\\/?\\>)|(\\&.+?\\;)
		
		//regex per match attributi (\\w+\\-?\\w+)\\=?(\\'|\\")(.*?)(\\'|\\") a valle di un replace \n con \s
		
		//regex per match informazione \\>(?!\\&.+?\\;)(.*?)\\<
		List<Token> result = new ArrayList<Token>();
		for(int i=0 ; i<text.length() ; i++) {
			Token token = null;
			if(text.charAt(i) == '<') {
				String htmlElement = new String();
				while(text.charAt(i) != '>') {
					htmlElement = htmlElement.concat(String.valueOf(text.charAt(i)));
					i++;
				}
				htmlElement = htmlElement.concat(String.valueOf(text.charAt(i))); //aggiungo il carattere di fine tag
				String tagOpening = new String();
				String tagClosure = new String();
				String tagName = new String();
				Map<String, String> attributes = new HashMap<String, String>();
				splitAttributes(htmlElement, tagOpening, tagClosure, tagName, attributes);
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
	
	public void splitAttributes(String element, String tagOpening, String tagClosure, String tagName, Map<String, String> attributes){
		if(!element.isBlank()) {
			
			String regex = "(<)(\\/)?(\\w+)( .*)?(>)";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(element);
			
			if(matcher.matches()) {
				if(matcher.group(1) != null && matcher.group(2) != null) {
					tagOpening = matcher.group(1).concat(matcher.group(2));
				}
				if(matcher.group(3) != null) {
					tagName = matcher.group(3);
				}
				if(matcher.group(4) != null) {
					String attr = matcher.group(4);
					String attrRegex = "(\\w+)=\\\"(.*?\\(?.*?\\)?)\\\"";
					Pattern attrPattern = Pattern.compile(attrRegex);
					Matcher attrMatcher = attrPattern.matcher(attr);
					
					if(attrMatcher.matches()) {
						attributes.put(attrMatcher.group(1), attrMatcher.group(2));
					}
					
				}
				if(matcher.group(5) != null) {
					tagClosure = matcher.group(5);
				}
			}
		}
	}
}
