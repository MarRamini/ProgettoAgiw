package utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
		
		List<String> polished = this.polish(tokens); //removes white spaces, comments and scripts
		
		return polished;
	}	
	
	public List<String> TokenizeOptimized(String text){
		List<String> tokens = new ArrayList<String>();
		String token = "";
		
		for(int i=0 ; i<text.length() ; i++){
			if(text.charAt(i) == '<'){
				if(!token.equals("")){
					tokens.add(token);
					token = "";
				}
				token = String.valueOf(text.charAt(i));
			}
			else if(text.charAt(i) == '>'){
				token = token.concat(String.valueOf(text.charAt(i)));
				tokens.add(token);
				token = "";
			}
			else{
				token = token.concat(String.valueOf(text.charAt(i)));
			}
		}  

		List<String> polished = this.polish(tokens); //removes white spaces, comments and scripts
		
		return polished;
	}	
	
	private List<String> polish(List<String> unpolished){
		List<String> polished = new ArrayList<String>();		
		Iterator<String> it = unpolished.iterator();
		int position = -1;
		int counter = 0;
		
		while(it.hasNext()){
			String current = it.next();		
			Pattern whiteSpacePattern = Pattern.compile("^\\s*$");
			Matcher whiteSpaceMatcher = whiteSpacePattern.matcher(current);
			if(!whiteSpaceMatcher.matches() && !(current.contains("<!--") || current.contains("-->"))){
				polished.add(current);
				
				if(current.contains("<script")){
					position = counter;
				}
				else if(current.contains("</script")){
					if(position > -1){
						for(int i = counter; i >= position ; i--){
							polished.remove(polished.get(i));
							counter --;
						}
					}
				}
				counter ++;
			}		
			
		}
		
		return polished;
	}
}