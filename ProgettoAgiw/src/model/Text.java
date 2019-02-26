package model;

import java.util.ArrayList;
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
}
