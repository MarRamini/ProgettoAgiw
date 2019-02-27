package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextSet {
	
	private List<Text> textSet;
	
	public TextSet() {
		this.textSet = new ArrayList<Text>();
	}
	
	public List<Text> getList() {
		return textSet;
	}

	public void setTextSet(List<Text> textSet) {
		this.textSet = textSet;
	}
	
	public boolean isEmpty(){
		return this.textSet.isEmpty();
	}
	
	public boolean hasVariability(){
		boolean variability = false;
		Iterator<Text> it = this.textSet.iterator();
		while(it.hasNext() && !variability){
			Text current = it.next();
			for(int i = this.textSet.indexOf(current) ; i < this.textSet.size() - 1 ; i++){
				variability = !current.equals(this.textSet.get(i));
			}
			
		}
		return variability;
	}
}
