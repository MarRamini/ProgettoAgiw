package tex;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Metrics;
import model.SiteMetricsData;
import utils.HtmlReader;
import utils.ResultsReader;
import utils.Tokenizator;

public class MetricsCalculator {
	
	private static MetricsCalculator instance = new MetricsCalculator();
	
	private MetricsCalculator(){}
	
	public static MetricsCalculator instance(){
		return instance;
	}
	
	public List<Metrics> getMetrics(String datasetFolder, String resultsFolder){
		List<Metrics> result = new ArrayList<Metrics>();
		List<SiteMetricsData> data = new ArrayList<SiteMetricsData>();
				
		File folder = new File("C:\\Users\\Cerberus 2.0\\Desktop\\TexResults");
	
		for(File category : folder.listFiles()){
			
			System.out.println("Analizing result category " + category.getName() + " " + Calendar.getInstance().getTime().toString());
	
			for(File site : category.listFiles()){
				
				System.out.println("Analizing result site " + site.getName() + " " + Calendar.getInstance().getTime().toString());
				
				List<String> totalCases = getExtractedCases(new File(site.getPath() + "\\TexResult"));
				int totalExtractedCases = totalCases.size();
				
				System.out.println("Valuating extracted relevant cases... " + Calendar.getInstance().getTime().toString());
				List<String> relevantCases = getRelevantCases(totalCases);
				int totalExtractedRelevantCases = relevantCases.size();
				
				totalCases.clear();
				relevantCases.clear();
				
				System.out.println("Found " + totalExtractedCases + " extracted cases, " + totalExtractedRelevantCases + " were relevant " + Calendar.getInstance().getTime().toString());
			
				SiteMetricsData metric = new SiteMetricsData();
				metric.setCategory(category.getName());
				metric.setSiteName(site.getName());
				metric.setExtractedPositiveCases(totalExtractedRelevantCases);
				metric.setExtractedTotalcases(totalExtractedCases);
				
				data.add(metric);
			}
		}	
		
		folder = new File("C:\\Users\\Cerberus 2.0\\Desktop\\alf-dataset");
		
		for(File category : folder.listFiles()){
			
			System.out.println("Analizing original category " + category.getName() + " " + Calendar.getInstance().getTime().toString());
			
			for(File site : category.listFiles()){
				
				if(!site.getName().equals("desktop.ini")){
					System.out.println("Analizing original site " + site.getName() + " " + Calendar.getInstance().getTime().toString());
					
					System.out.println("Analizing original site pages " + Calendar.getInstance().getTime().toString());
					List<String> totalCases = getTotalOriginalCases(site);
					System.out.println("Valuating original relevant cases " + Calendar.getInstance().getTime().toString());
					List<String> relevantCases = getRelevantCases(totalCases);
					
					int totalRelevantOriginalCases = relevantCases.size();
					
					totalCases.clear();
					relevantCases.clear();
					
					System.out.println("Found " + totalRelevantOriginalCases + " relevant original cases "  + Calendar.getInstance().getTime().toString());
					
					for(SiteMetricsData metric : data){
						if(metric.getCategory().equals(category.getName()) && metric.getSiteName().equals(site.getName())){
							metric.setTotalRelevantCases(totalRelevantOriginalCases);
						}
					}
				}
				
			}
		}	
		
		System.out.println("Calculating metrics... " + Calendar.getInstance().getTime().toString());
		
		for(SiteMetricsData m : data){
			Metrics metrics = new Metrics();
			metrics.setCategory(m.getCategory());
			metrics.setSite(m.getSiteName());
			metrics.setPrecision(getPrecision(m.getExtractedPositiveCases(), m.getExtractedTotalcases()));
			metrics.setRecall(getRecall(m.getExtractedPositiveCases(), m.getTotalRelevantCases()));
			metrics.setF1Measure(getF1Measure(metrics.getPrecision(), metrics.getRecall()));
			result.add(metrics);
		}
		
		System.out.println("DONE");
		
		return result;
	}
	
	private void getFileNames(File folder, List<String> filenames){
		for (File file : folder.listFiles()) {
	        if (file.isDirectory()) {
	        	getFileNames(file, filenames); // Calls same method again.
	        } else {
	           filenames.add(file.getPath());
	        }
	    }
	}	
	
	private List<String> getTotalOriginalCases(File file){
		List<String> total = new ArrayList<String>();
		List<String> filenames = new ArrayList<String>();
		
		try{
			getFileNames(file, filenames);
			for(String name : filenames){				
				String document = HtmlReader.instance().readHtmlFile(name);
				total.addAll(Tokenizator.instance().TokenizeOptimized(document));
			}
		}
		catch(Exception e){
			System.out.println("An error occurred while getting total positives " + e.getMessage());
		}
		
		return total;
	}
	
	private List<String> getRelevantCases(List<String> totalCases){
		List<String> positives = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\<.*\\>");
		for(int i=0 ; i < totalCases.size() ; i++){			
			Matcher matcher = pattern.matcher(totalCases.get(i));
			if(!matcher.matches()){
				positives.add(totalCases.get(i));				
			}
		}
		return positives;
	}
	
	private List<String> getExtractedCases(File file){
		List<String> total = new ArrayList<String>();
		
		try{
			System.out.println("Reading result file " + Calendar.getInstance().getTime().toString());
			total = ResultsReader.instance().readResultFile(file.getPath());
		}
		catch(Exception e){
			System.out.println("An error occurred while getting total positives " + e.getMessage());
		}
		
		return total;
	}
	
	private double getPrecision(int positivesExtractedCases, int totalExtractedCases){
		return (double)positivesExtractedCases / (double)totalExtractedCases;
	}
	
	private double getRecall(int positivesExtractedCases, int totalRelevantCases){
		return (double)positivesExtractedCases / (double)totalRelevantCases;
	}
	
	private double getF1Measure(double precision, double recall){
		return (2*precision*recall) / (precision + recall);
	}
}
