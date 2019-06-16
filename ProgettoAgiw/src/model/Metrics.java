package model;

public class Metrics {

	private String category;	
	private String site;	
	private double precision;	
	private double recall;	
	private double f1Measure;
	
	public Metrics(){}
	
	public Metrics(String category, String site, double precision, double recall, double f1Measure){
		this.category = category;
		this.site = site;
		this.precision = precision;
		this.recall = recall;
		this.f1Measure = f1Measure;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getF1Measure() {
		return f1Measure;
	}

	public void setF1Measure(double f1Measure) {
		this.f1Measure = f1Measure;
	}
	
}
