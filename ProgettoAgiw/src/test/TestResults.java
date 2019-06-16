package test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Metrics;
import model.SiteMetricsData;
import utils.HtmlWriter;
import utils.ResultsReader;

public class TestResults {

	public static void main(String[] args){
		
		List<String> logs = ResultsReader.instance().readResultFile("C:\\Users\\Cerberus 2.0\\Desktop\\MetricsRes");
	
		List<SiteMetricsData> data = new ArrayList<SiteMetricsData>();		

		Pattern categoryPattern = Pattern.compile("^Analizing result category (\\w*) .*");
		Pattern sitePattern = Pattern.compile("^Analizing result site (.*?) .*");
		Pattern extractedPattern = Pattern.compile("^Found (\\d*) extracted cases, (\\d*) .*");
		
		Pattern originalCategoryPattern = Pattern.compile("^Analizing original category (\\w*) .*");
		Pattern originalSitePattern = Pattern.compile("^Analizing original site ((?!pages).*?) .*");
		Pattern originalPattern = Pattern.compile("^Found (\\d*) relevant original cases .*");
		
		String category = "";
		String siteName = "";
		
		String originalCategory = "";
		String originalSiteName = "";
		
		for(String log : logs){
			Matcher categoryMatcher = categoryPattern.matcher(log);
			if(categoryMatcher.matches()){
				category = categoryMatcher.group(1);				
			}
			
			Matcher siteMatcher = sitePattern.matcher(log);
			if(siteMatcher.matches()){
				siteName = siteMatcher.group(1);
				
			}
			
			Matcher extractedMatcher = extractedPattern.matcher(log);
			if(extractedMatcher.matches()){				
				SiteMetricsData m = new SiteMetricsData();
				m.setCategory(category);
				m.setSiteName(siteName);
				m.setExtractedTotalcases(Integer.parseInt(extractedMatcher.group(1)));
				m.setExtractedPositiveCases(Integer.parseInt(extractedMatcher.group(2)));
				data.add(m);
			}
			
			Matcher originalCategoryMatcher = originalCategoryPattern.matcher(log);
			if(originalCategoryMatcher.matches()){
				originalCategory = originalCategoryMatcher.group(1);				
			}
			
			Matcher originalSiteMatcher = originalSitePattern.matcher(log);
			if(originalSiteMatcher.matches()){
				originalSiteName = originalSiteMatcher.group(1);		
			}
			
			Matcher originalMatcher = originalPattern.matcher(log);
			if(originalMatcher.matches()){				
				for(SiteMetricsData d : data){
					if(d.getCategory().equals(originalCategory) && d.getSiteName().equals(originalSiteName)){
						d.setTotalRelevantCases(Integer.parseInt(originalMatcher.group(1)));
						break;
					}
				}
			}
		}
		
		List<Metrics> metrics = new ArrayList<Metrics>();
		for(SiteMetricsData m : data){
			Metrics metric = new Metrics();
			metric.setCategory(m.getCategory());
			metric.setSite(m.getSiteName());
			metric.setPrecision(getPrecision(m.getExtractedPositiveCases(), m.getExtractedTotalcases()));
			metric.setRecall(getRecall(m.getExtractedPositiveCases(), m.getTotalRelevantCases()));
			metric.setF1Measure(getF1Measure(metric.getPrecision(), metric.getRecall()));
			metrics.add(metric);
		}
		
		File metricsRes = new File("C:\\Users\\Cerberus 2.0\\Desktop\\MetricsResults");
		List<String> toWrite = new ArrayList<String>();
		for(Metrics m : metrics){			
			toWrite.add(m.getCategory());
			toWrite.add(m.getSite());
			toWrite.add("precision: " + String.valueOf(m.getPrecision()));
			toWrite.add("recall: " + String.valueOf(m.getRecall()));
			toWrite.add("f-measure: " + String.valueOf(m.getF1Measure()));
			toWrite.add("");
		}
		
		HtmlWriter.instance().writeHtmlFile(metricsRes, toWrite);
	}
	
	private static double getPrecision(int positivesExtractedCases, int totalExtractedCases){
		return (double)positivesExtractedCases / (double)totalExtractedCases;
	}
	
	private static double getRecall(int positivesExtractedCases, int totalRelevantCases){
		return (double)positivesExtractedCases / (double)totalRelevantCases;
	}
	
	private static double getF1Measure(double precision, double recall){
		return (2*precision*recall) / (precision + recall);
	}
}
