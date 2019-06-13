package model;

public class Metrics {

	private double precision;
	
	private double recall;
	
	private double f1Measure;
	
	public Metrics(){}
	
	public Metrics(double precision, double recall, double f1Measure){
		this.precision = precision;
		this.recall = recall;
		this.f1Measure = f1Measure;
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
