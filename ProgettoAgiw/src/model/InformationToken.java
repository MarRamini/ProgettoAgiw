package model;

public class InformationToken implements Token{
	private String information;
	
	public InformationToken(String info) {
		this.setInformation(info);
	}

	private String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}
	
	public boolean equals(Token token) {
		try {
			return this.getClass().equals(token.getClass()) && this.information.equals(((InformationToken)token).getInformation());
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public String toString() {
		return this.getInformation();
	}
	
	public boolean isEmpty() {
		return this.getInformation().isBlank();
	}
	
}
