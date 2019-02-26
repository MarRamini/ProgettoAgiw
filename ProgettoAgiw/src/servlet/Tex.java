package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Text;

/**
 * Servlet implementation class Tex
 */
@WebServlet("/Tex")
public class Tex extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<Text> textSet = new ArrayList<Text>(); 
	
	public List<Text> getTextSet() {
		return textSet;
	}

	public void setTextSet(List<Text> textSet) {
		this.textSet = textSet;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tex() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		for(Text website : this.textSet){
			System.out.println(website.getTokens().toString() + "\n-----------------\n");
		}
		
		//List<String> result = this.tex(this.textSet, 10, 1);
		List<String> test = new ArrayList<String>();
		test.add("aba");
		test.add("abc");
		test.add("abdcab");
		List<String> result = this.expand(test, 3);
		
		
		String responseText = ""; //importare json e scrivere risposta
		
		for(String elem : result) {
			responseText.concat("\n-----------------\n");
		}		
		
		response.getWriter().println(responseText);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String textPage = request.getParameter("textPage");
		
		try {
			JSONObject json = new JSONObject(textPage);
			JSONArray tokens = json.getJSONArray("tokens");
			List<String> unparsedDocument = new ArrayList<String>();
			for(int i = 0 ; i<tokens.length() ; i++) {
				String token = tokens.getString(i);
				unparsedDocument.add(token);
			}			
			Text text = new Text();
			text.setTokens(unparsedDocument);
			this.textSet.add(text);
			response.getWriter().write("document succesfully parsed");
		}catch(Exception e) {
			e.printStackTrace();
			response.getWriter().write("error while parsing document: " + e.getMessage());
		}			
	}
	
	/**
	 * 
	 * @param textSet
	 * @param max: numero massimo di match degli elementi nei documenti del textset
	 * @param min: numero minimo di match. (In genere 1, almeno il nodo html)
	 * @return
	 */
	private List<Text> tex(List<Text> textSet, int max, int min){
		List<Text> resultText;
		List<Text> extracted;
		
		extracted = extract(textSet, max, min);
		resultText = filter(extracted);		
		
		return resultText;
	}
	
	private List<Text> extract(List<Text> textSet, int max, int min){
		List<Text> result = textSet;
		
		for(int size = max ; size >= min ; size--){
			List<Text> buffer = new ArrayList<Text>();
			while(!result.isEmpty()){
				List<Text> ts = new ArrayList<Text>();
				ts = dequeue(result, ts);		//dequeue di tutti i documenti
				List<Text> expansion = expand(ts, size);
				if(expansion.isEmpty()){
					buffer.addAll(ts); //non ho espansione, ovvero ho espanso già il possibile per il dato size
				}
				else{
					result.addAll(expansion); //ho espansioni, enqueue di tutte le espansioni
				}
			}
			result = buffer;
		}
		return result;
	}
	
	private List<Text> dequeue(List<Text> queue, List<Text> buffer){
		for(int i=0 ; i< queue.size() i++) {
			buffer.add(queue.remove(i))
		}
	}
	
	private List<String> filter(List<String> textSet){
		List<String> result = new ArrayList<String>();
		for(String text : textSet) {
			if(this.hasVariability(text, textSet)) {
				result.add(text);
			}
		}
		return result;
	}
	
	private List<Text> expand(List<Text> texts, int size){
		List<Text> result = new ArrayList<Text>();
		Text shortest = findShortestText(texts); //trovo il documento più corto
		if(shortest != null && !shortest.isEmpty() && shortest.length() >= size){
			Map<Text, List<Integer>> shared = findPattern(texts, shortest, size);
			if(!shared.isEmpty()) {
				result.addAll(createExpansion(texts, shared));
			}
		}
			
		return result;
	}
	
	private Text findShortestText(List<Text> list){
		Iterator<Text> it = list.iterator();
		Text result = new Text();
		while(it.hasNext()){
			Text current = it.next();
			if(result.isEmpty() || result.length() > current.length()){
				result = current;
			}
		}
		return result;
	}
	
	private Map<Text, List<Integer>> findPattern(List<Text> list, Text shortest, int size){
		/*
		 * cerco un pattern nei documenti, di dimensione size
		 * */
		boolean found = false;
		Map<Text,List<Integer>> result = new HashMap<Text, List<Integer>>();
		for(int i = 0 ; i <= (shortest.getSize() - size) ; i++){ 
			if(!found){				
				found = true;
				Iterator<Text> it = list.iterator();
				List<Integer> matches = new ArrayList<Integer>();
				while(it.hasNext() && found){
					Text current = it.next();
					matches = findMatches(current, shortest, i, size);
					found = !matches.isEmpty();
					result.put(current, matches);
				}				
			}
		}
		return result;
	}
	
	private List<Integer> findMatches(Text text, Text pattern, int position, int size){
		/*int i = position;
		int j = 0;
		List<Integer> result = new ArrayList<Integer>();
		
		while(i < text.length()) {
			if(j < dimension && text.charAt(i) == pattern.charAt(j)) {
				if(j == dimension - 1) {
					result.add(i - (pattern.length() - 1));
				}
				i++;
				j++;
			}
			else if(j > 0) {
				j = 0;
			}
			else {
				i++;
			}
		}
		return result;*/
		
		
		
		
		List<Integer> matches = new ArrayList<Integer>();
		int textLength = text.length();
		int patternLength = pattern.length();
		int[] prefixes = this.prefixFunction(pattern);
		int i = position; //???
		int j = position; //???
		while (i < textLength) {
			//cerco un match con il primo carattere del pattern
			if (pattern.charAt(j) == text.charAt(i)) {
				continue;
			}
			/*
			if (pattern.charAt(j) == text.charAt(i)) {
				if (j == patternLength - 1) {
					matches.add(i - patternLength + 1); //abbinamento
				}
				i++;
				j++;
			}
			else if (j > 0)
				j = prefixes[j - 1];
			else i++;*/
		}
		return matches;
	}
	
	private List<String> createExpansion(List<String> texts, Map<String, List<Integer>> shared){
		List<String> result = new ArrayList<String>();
		
		List<String> set1 = new ArrayList<String>();
		List<String> set2 = new ArrayList<String>();
		List<String> set3 = new ArrayList<String>();
		
		for(String text : texts) {
			List<Integer> matches = shared.get(text);
			int matchIndex = matches.get(0);
			String prefix = text.substring(0, matchIndex);
			if(!prefix.isEmpty()) {
				set1.add(prefix);
			}
			int lastMatchIndex = matches.get(matches.size()-1);
			String separators = text.substring(matchIndex, lastMatchIndex);
			if(!separators.isEmpty()) {
				set2.add(separators);
			}
			String suffix = text.substring(lastMatchIndex, text.length()-1);
			if(!suffix.isEmpty()) {
				set3.add(suffix);
			}		
		}
		
		if(!set1.isEmpty()) {
			result.addAll(set1);
		}
		if(!set2.isEmpty()) {
			result.addAll(set2);
		}
		if(!set3.isEmpty()) {
			result.addAll(set3);
		}
		return null;
	}
	
	private int[] prefixFunction(String pattern) {
		int length = pattern.length();
		int [] prefixes = new int[length];
		prefixes[0] = 0;
		int prefixCounter = 0;
		int counter = 1;
		
		while (counter < length) {
			if (pattern.charAt(prefixCounter) == pattern.charAt(counter)) {
				prefixes[counter] = prefixCounter + 1;
				prefixCounter++;
				counter++; 
			}
			else {
				if (prefixCounter > 0) {
					prefixCounter = prefixes[prefixCounter - 1];
				}
				else { 
					prefixes[counter] = 0;
					counter++; 
				}
			}
		}
		return prefixes;
	}
	
	private boolean hasVariability(String text, List<String> textSet) {
		boolean variability = true;
		Iterator<String> it = textSet.iterator();
		while(it.hasNext()) {
			String current = it.next();
			variability = variability || !text.equals(current);	//devo rimuovere quelli il cui contenuto è lo stesso
		}
		return variability;
	}
}
