package model;

public class SiteMetricsData {
	
	private String siteName;
	private int totalRelevantCases;
	private int extractedTotalcases;
	private int extractedPositiveCases;
	private String category;
	
	public SiteMetricsData(){}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}	
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}	

	public int getTotalRelevantCases() {
		return totalRelevantCases;
	}

	public void setTotalRelevantCases(int totalRelevantCases) {
		this.totalRelevantCases = totalRelevantCases;
	}

	public int getExtractedTotalcases() {
		return extractedTotalcases;
	}

	public void setExtractedTotalcases(int extractedTotalcases) {
		this.extractedTotalcases = extractedTotalcases;
	}

	public int getExtractedPositiveCases() {
		return extractedPositiveCases;
	}

	public void setExtractedPositiveCases(int extractedPositiveCases) {
		this.extractedPositiveCases = extractedPositiveCases;
	}
	
}
