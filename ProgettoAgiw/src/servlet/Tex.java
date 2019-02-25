package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Tex
 */
@WebServlet("/Tex")
public class Tex extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<String> textSet = new ArrayList<String>(); 
	
	public List<String> getTextSet() {
		return textSet;
	}

	public void setTextSet(List<String> textSet) {
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
		List<String> result = this.tex(textSet, 9999, 1);
		
		String pattern = "ababaca";
		int[] prefixes = this.prefixFunction(pattern);
		
		for(int elem : prefixes) {
			System.out.print(elem + " ");
		}
		
		String responseText = ""; //importare json e scrivere risposta
		
		response.getWriter().println(responseText);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String textPage = request.getParameter("textPage");
		
		this.textSet.add(textPage);
		System.out.println("doPost");
		String pattern = "ababaca";
		int[] prefixes = this.prefixFunction(pattern);
		
		for(int elem : prefixes) {
			System.out.print(elem + " ");
		}
	}
	
	/**
	 * 
	 * @param textSet
	 * @param max: numero massimo di match degli elementi nei documenti del textset
	 * @param min: numero minimo di match. (In genere 1, almeno il nodo html)
	 * @return
	 */
	private List<String> tex(List<String> textSet, int max, int min){
		List<String> resultText;
		List<String> extracted;
		
		extracted = extract(textSet, max, min);
		resultText = filter(extracted);		
		
		return resultText;
	}
	
	private List<String> extract(List<String> textSet, int max, int min){
		List<String> result = textSet;
		
		for(int size = max ; size >= min ; size--){
			List<String> buffer = new ArrayList<String>();
			while(!result.isEmpty()){
				List<String> ts = new ArrayList<String>();
				ts.add(result.remove(0)); //dequeue
				List<String> expansion = expand(ts, size);
				if(expansion.isEmpty()){
					buffer.addAll(ts);
				}
				else{
					result.addAll(expansion);
				}
			}
			result = buffer;
		}
		return result;
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
	
	private List<String> expand(List<String> texts, int size){
		List<String> result = new ArrayList<String>();
		String shortest = findShortestText(texts);
		if(shortest != null && !shortest.isEmpty() && shortest.length() >= size){
			Map<String, List<Integer>> shared = findPattern(texts, shortest, size);
			if(!shared.isEmpty()) {
				result.addAll(createExpansion(texts, shared));
			}
		}
			
		return result;
	}
	
	private String findShortestText(List<String> list){
		Iterator<String> it = list.iterator();
		String result = "";
		while(it.hasNext()){
			String current = it.next();
			if(result.equals("") || result.length() > current.length()){
				result = current;
			}
		}
		return result;
	}
	
	private Map<String, List<Integer>> findPattern(List<String> list, String shortest, int size){
		boolean found = false;
		Map<String,List<Integer>> result = new HashMap<String, List<Integer>>();
		for(int i = 0 ; i <= (shortest.length() - size) ; i++){
			if(!found){				
				found = true;
				Iterator<String> it = list.iterator();
				List<Integer> matches = new ArrayList<Integer>();
				while(it.hasNext() && found){
					String current = it.next();
					matches.add(findMatches(current, shortest, i, size));
					found = !matches.isEmpty();
					result.put(current, matches);
				}				
			}
		}
		return result;
	}
	
	private int findMatches(String text, String pattern, int counter, int size){
		int textLength = text.length();
		int patternLength = pattern.length();
		int[] prefixes = this.prefixFunction(pattern);
		int i = 0; //???
		int j = 0; //???
		while (i < textLength) {
			if (pattern.charAt(j) == text.charAt(i)) {
				if (j == patternLength - 1) {
					return i - patternLength + 1; //abbinamento
				}
				i++;
				j++;
			}
			else if (j > 0)
				j = prefixes[j - 1];
			else i++;
		}
		return - 1; //nessun abbinamento		 
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
