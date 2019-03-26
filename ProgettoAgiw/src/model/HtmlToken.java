package model;

import java.util.Map;

public class HtmlToken implements Token{
	private String tagOpening;
	private String tagName;
	private Map<String, String> attributes;
	private String tagClosure;
	
	public HtmlToken(String tagOpening, String tagName, Map<String, String> attributes, String tagClosure){
		this.setTagOpening(tagOpening);
		this.setTagName(tagName);
		this.setAttributes(attributes);
		this.setTagClosure(tagClosure);
	}

	private String getTagOpening() {
		return tagOpening;
	}

	private void setTagOpening(String tagOpening) {
		this.tagOpening = tagOpening;
	}

	private String getTagName() {
		return tagName;
	}

	private void setTagName(String tagName) {
		this.tagName = tagName;
	}

	private Map<String, String> getAttributes() {
		return attributes;
	}

	private void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	private String getTagClosure() {
		return tagClosure;
	}

	private void setTagClosure(String tagClosure) {
		this.tagClosure = tagClosure;
	}
	
	public boolean equals(Token token) {
		try {
			return token.getClass().equals(this.getClass()) && this.getTagName().equals(((HtmlToken)token).getTagName()) && this.sameAttributes(this, (HtmlToken)token);
		}
		catch(Exception e) {
			return false;
		}
	}
	
	private boolean sameAttributes(HtmlToken _this, HtmlToken token) {
		boolean result = true;
		
		if(_this.getAttributes().size() == token.getAttributes().size() && this.getAttributes().size() > 0) {
			for(String key : _this.getAttributes().keySet()) {
				result = result && (token.getAttributes().containsKey(key) && _this.getAttributes().get(key).equals(token.getAttributes().get(key)));		
			}
		}
		
		return result;
	}
	
	public String toString() {
		String string = "" + this.getTagOpening() + this.getTagName();
		if(!this.getAttributes().isEmpty()) {
			for(String key : this.getAttributes().keySet()) {
				string = string.concat(" " + key + "='" + this.getAttributes().get(key) + "' ");
			}
		}
		string = string + this.getTagClosure();
		
		return string;
	}

	@Override
	public boolean isEmpty() {
		return this.getTagOpening().isBlank() && this.getTagName().isBlank() && this.getAttributes().isEmpty() && this.getTagClosure().isBlank();
	}
}
