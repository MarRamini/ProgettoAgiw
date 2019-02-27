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
import model.TextSet;

/**
 * Servlet implementation class Tex
 */
@WebServlet("/Tex")
public class Tex extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private List<TextSet> textSet = new ArrayList<TextSet>(); 
	
	public List<TextSet> getTextSet() {
		return textSet;
	}

	public void setTextSet(List<TextSet> textSet) {
		this.textSet = textSet;
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Tex() {
        super();
        // TODO Auto-generated constructor stub
        this.textSet.add(new TextSet());
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		List<TextSet> result = this.tex(this.textSet, 17, 1);
		
		
		String responseText = ""; //importare json e scrivere risposta
		
		for(TextSet elem : result) {
			for(Text text : elem.getList()){
				responseText = responseText.concat(text.toString());
			}
		}		
		
		response.getWriter().write(responseText);
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
			this.textSet.get(0).getList().add(text);
			response.getWriter().write("documents succesfully uploaded");
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
	private List<TextSet> tex(List<TextSet> textSet, int max, int min){
		List<TextSet> resultText;
		List<TextSet> extracted;
		
		extracted = extract(textSet, max, min);
		resultText = filter(extracted);		
		
		return resultText;
	}
	
	private List<TextSet> extract(List<TextSet> textSet, int max, int min){
		List<TextSet> result = textSet;
		
		for(int size = max ; size >= min ; size--){
			List<TextSet> buffer = new ArrayList<TextSet>();
			while(!result.isEmpty()){
				TextSet ts = (result.remove(0));		//dequeue del primo textset
				List<TextSet> expansion = expand(ts, size);
				if(expansion.isEmpty()){
					buffer.add(ts); //non ho espansione, ovvero ho espanso già il possibile per il dato size
				}
				else{
					buffer.addAll(expansion); //ho espansioni, enqueue di tutte le espansioni
				}
			}
			result = buffer;
		}
		return result;
	}	
	
	private List<TextSet> filter(List<TextSet> textSet){
		List<TextSet> result = new ArrayList<TextSet>();
		for(TextSet set : textSet) {
			if(this.hasVariability(set)) {
				result.add(set);
			}
		}
		return result;
	}
	
	private List<TextSet> expand(TextSet texts, int size){
		List<TextSet> result = new ArrayList<TextSet>();
		Text shortest = findShortestText(texts.getList()); //trovo il documento più corto
		if(shortest != null && !shortest.isEmpty() && shortest.length() >= size){
			Map<Text, List<Integer>> shared = findPattern(texts.getList(), shortest, size);
			if(!shared.isEmpty()) {
				result = createExpansion(texts.getList(), shared, size);
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
		
		Map<Text, List<Integer>> result = new HashMap<Text, List<Integer>>();
		boolean found = false;
		
		for(int i = 0 ; i <= shortest.getSize() - size; i++){ //ciclo sulle posizioni di shortest
			if(!found){
				found = true;			
				Iterator<Text> it = list.iterator();
				while(it.hasNext() && found){ 
					Text current = it.next();
					List<Integer> matches =  findMatches(current, shortest, i, size);
					found = !matches.isEmpty();
					if(found){
						result.put(current, matches);
					}
					else{
						result.clear();
					}
				}
			}
		}
		
		/*boolean found = false;
		Map<Text,List<Integer>> result = new HashMap<Text, List<Integer>>();
		for(int i = 0 ; i <= (shortest.getSize() - size) ; i++){ 
			if(!found){				
				found = true;
				Iterator<Text> it = list.iterator();
				List<Integer> matches = new ArrayList<Integer>();
				while(it.hasNext() && found){
					Text current = it.next();
					matches = findMatches(current, shortest, size);
					found = !matches.isEmpty();
					result.put(current, matches);
				}				
			}
		}*/
		return result;
	}
	//<html><Head><title>Result</title></head><body>abbase..............
	//<html><Head><title>Result</title></head><body>Result..............
	private List<Integer> findMatches(Text text, Text pattern, int position, int size){
		List<Integer> matches = new ArrayList<Integer>();
		int i = 0;
		
		while(i < text.getSize()){
			int j = position;
			while(j < pattern.getSize() && i < text.getSize()){
				if(j < size + position && text.getToken(i).equals(pattern.getToken(j))){
					if(j == size + position - 1){
						matches.add(i - (size - 1)); //indice del primo token di match
					}
					i++;
					j++;
				}
				else if(j > position){
					j = position;
				}
				else{
					i++;
				}
			}
		}
		
		/*while(i < text.getSize()){
			if(j < size && text.getToken(i).equals(pattern.getToken(j))){
				if(j == size - 1){
					matches.add(i - (size - 1)); //indice del primo token di match
				}
				i++;
				j++;
			}
			else if(j > 0){
				j = 0;
			}
			else{
				i++;
			}
		}	*/
		
		return matches;
	}
	
	private List<TextSet> createExpansion(List<Text> texts, Map<Text, List<Integer>> shared, int size){
		List<TextSet> result = new ArrayList<TextSet>();
		
		TextSet set1 = new TextSet();
		TextSet set2 = new TextSet();
		TextSet set3 = new TextSet();
		
		for(Text text : texts) {
			List<Integer> matches = shared.get(text);
			if(matches != null && !matches.isEmpty()){			
				int matchIndex = matches.get(0); //prendo il primo match avvenuto
				Text prefix = text.getSubSet(0, matchIndex);
				if(prefix != null && !prefix.isEmpty()) {
					set1.getList().add(prefix);
				}
				Text separators = text.getSubSet(matchIndex, matchIndex + size - 1);
				if(separators != null && !separators.isEmpty()) {
					set2.getList().add(separators);
				}
				Text suffix = text.getSubSet(matchIndex + size , text.getSize() - 1);
				if(suffix != null && !suffix.isEmpty()) {
					set3.getList().add(suffix);
				}
			}			
		}
		
		if(!set1.isEmpty()) {
			result.add(set1);
		}
		if(!set2.isEmpty()) {
			result.add(set2);
		}
		if(!set3.isEmpty()) {
			result.add(set3);
		}
		return result;
	}
	
	private boolean hasVariability(TextSet textSet) {
		return textSet.hasVariability();
	}
}
