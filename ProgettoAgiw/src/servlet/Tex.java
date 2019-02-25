package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
		
		String responseText = ""; //importare json e scrivere risposta
		
		response.getWriter().println(responseText);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String textPage = request.getParameter("textPage");
		
		this.textSet.add(textPage);
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
	
	private List<String> filter(List<String> extracted){
		return extracted;
	}
	
	private List<String> expand(List<String> texts, int size){
		List<String> result = new ArrayList<String>();
		String shortest = findShortestText(texts);
		if(shortest != null && !shortest.isEmpty() && shortest.length() >= size){
			List<String> shared = findPattern(texts, shortest, size);
		}
		
		
		return new ArrayList<String>();
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
	
	private List<String> findPattern(List<String> list, String shortest, int size){
		boolean found = false;
		List<String> result = new ArrayList<String>();
		for(int i = 0 ; i <= (shortest.length() - size) ; i++){
			if(!found){				
				found = true;
				Iterator<String> it = list.iterator();
				while(it.hasNext() && found){
					String current = it.next();
					List<String> matches = findMatches(current, shortest, i, size);
					found = !matches.isEmpty();
					result.addAll(matches);
				}				
			}
		}
		return result;
	}
	
	private List<String> findMatches(String text, String base, int counter, int size){
		return new ArrayList<String>();
	}
}
