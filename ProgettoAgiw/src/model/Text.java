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
	
	public String getToken(int index){
		return this.tokens.get(index);
	}
	
	public Text getSubSet(int startIndex, int endIndex){
		if(startIndex < 0 || startIndex >= this.tokens.size() || endIndex < 0 || endIndex >= this.tokens.size() || startIndex > endIndex){
			return null;
		}
		else{
			List<String> subSet = new ArrayList<String>();
			if(startIndex < endIndex){
				int i = startIndex;
				while(i <= endIndex){
					subSet.add(this.tokens.get(i));
					i++;
				}
			}			
			Text text = new Text();
			text.setTokens(subSet);
			return text;
		}
	}
	
	public boolean equals(Text text){
		return this.getSize() == text.getSize() && this.length() == text.length() && this.checkContentEquality(text);
	}
	
	public boolean checkContentEquality(Text text){
		boolean equality = true;
		for(int i = 0 ; i < this.getSize() - 1 && i < text.getSize() - 1 && equality ; i++){
			equality = this.getToken(i).equals(text.getToken(i));
		}
		return equality;
	}
	
	public String toString(){
		String result = "";
		for(String token : this.tokens){
			result = result + token + "\n";
		}
		return result;
	}
}
