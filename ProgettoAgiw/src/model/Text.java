package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Text {

	private List<String> tokens;
	
	public Text() {
		this.tokens = new ArrayList<String>();
	}
	
	public void setTokens(List<String> tokens) {
		this.tokens.addAll(tokens);
	}
	
	public List<String> getTokens() {
		return this.tokens;
	}
	
	public int getSize() {
		return this.tokens.size();
	}
	
	public int length() {
		int length = 0;
		for(String token : this.tokens) {
			length += token.length();
		}
		return length;
	}
	
	public boolean isEmpty() {
		return this.tokens.isEmpty();
	}
	
	public String getToken(int index){
		return this.tokens.get(index);
	}
	
	public Text getSubSet(int startIndex, int endIndex){
		if(startIndex < 0 || startIndex >= this.tokens.size() || endIndex < 0 || endIndex >= this.tokens.size()){
			return null;
		}
		else{
			List<String> subSet = new ArrayList<String>();
			for(int i = startIndex ; i < endIndex ; i++){
				subSet.add(this.tokens.remove(i));
			}
			Text text = new Text();
			text.setTokens(subSet);
			return text;
		}
	}
}
