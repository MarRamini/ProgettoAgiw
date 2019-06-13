package tex;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Metrics;
import model.SiteMetricsData;
import model.Text;
import model.TextSet;
import utils.HtmlReader;
import utils.HtmlWriter;
import utils.ResultsReader;
import utils.Tokenizator;

public class MetricsCalculator {
	
	private static MetricsCalculator instance = new MetricsCalculator();
	
	private MetricsCalculator(){}
	
	public static MetricsCalculator instance(){
		return instance;
	}
	public Map<String, Metrics> getMetrics(String datasetFolder, String resultsFolder){
		Map<String, Metrics> result = new HashMap<String, Metrics>();
		List<SiteMetricsData> metricsData = new ArrayList<SiteMetricsData>();
		int relevantCases = 0;
		int positiveCases = 0;
		int totalExtractedCases = 0;
		
		System.out.println("Calculating total and positives extracted cases " + Calendar.getInstance().getTime().toString());
		File resRoot = new File(resultsFolder);
		File dataRoot = new File(datasetFolder);
		for(File resType : resRoot.listFiles()){
			System.out.println("Analizing " + resType.getName() + " folder");
			for(File resSite : resType.listFiles()){
				if(!resSite.getName().contains("desktop.ini")){					
					System.out.println("Calculating total and positives cases in directory: " + resSite.getName() + " " + Calendar.getInstance().getTime().toString());
					List<String> total = new ArrayList<String>();
					for(File resFile : resSite.listFiles()){
						total.addAll(getTotalCases(resFile));
					}
					
					totalExtractedCases = total.size();
					System.out.println("Valuating relevant cases " + Calendar.getInstance().getTime().toString());
					List<String> positives = getRelevantCases(total);
					positiveCases = positives.size();
					System.out.println("found " + totalExtractedCases + " total extracted cases, " + positiveCases + " were relevant" + Calendar.getInstance().getTime().toString());
					
					System.out.println("Valuating original relevant cases " + Calendar.getInstance().getTime().toString());
					for(File dataType : dataRoot.listFiles()){
						System.out.println("Analizing " + dataType.getName() + " folder");
						for(File dataSite : dataType.listFiles()){
							if(dataSite.getName().equals(resSite.getName())){
								System.out.println("Calculating positives cases in directory: " + dataSite.getName() + " " + Calendar.getInstance().getTime().toString());
								List<String> totalCases = new ArrayList<String>();
								for(File dataFile : dataSite.listFiles()){						
									total.addAll(getTotalRelevantCases(dataFile));				
								}
								
								relevantCases = totalCases.size();
								System.out.println("found " + relevantCases + " total relevant cases " + Calendar.getInstance().getTime().toString());
								
					
							}
						}
					}
					
					SiteMetricsData data = new SiteMetricsData();
					data.setExtractedPositiveCases(positiveCases);
					data.setExtractedTotalcases(totalExtractedCases);
					data.setSiteName(resSite.getName());
					data.setCategory(resType.getName());
					metricsData.add(data);
					System.out.println("found " + totalExtractedCases + " elements, " + positiveCases + " were relevant " + Calendar.getInstance().getTime().toString());
				}
			}
		}
		
		for(SiteMetricsData el : metricsData){
			String siteName = el.getSiteName();
			double precision = getPrecision(el.getExtractedPositiveCases(), el.getExtractedTotalcases());
			double recall = getRecall(el.getExtractedPositiveCases(), el.getTotalRelevantCases());
			double f_measure = getF1Measure(precision, recall);
			
			File metricsRes = new File("C:\\Users\\Cerberus 2.0\\Desktop\\metricsResults");
			metricsRes.mkdir();
			
			List<String> toWrite = new ArrayList<String>();
			
			toWrite.add(el.getCategory());
			toWrite.add(el.getSiteName());
			toWrite.add("precision: " + String.valueOf(precision));
			toWrite.add("recall: " + String.valueOf(recall));
			toWrite.add("f-measure: " + String.valueOf(f_measure));
			
			//Metrics metrics = new Metrics(precision, recall, f_measure);
			//result.put(siteName, metrics);
		}

		
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
	
	private List<String> getTotalRelevantCases(File file){
		List<String> total = new ArrayList<String>();
		List<String> positives = new ArrayList<String>();
		List<String> filenames = new ArrayList<String>();
		
		try{
			getFileNames(file, filenames);
			System.out.println("Reading dataset original files " + Calendar.getInstance().getTime().toString());
			for(String name : filenames){				
				String document = HtmlReader.instance().readHtmlFile(name);
				System.out.println("Tokenizing file " + name + " " + Calendar.getInstance().getTime().toString());
				total.addAll(Tokenizator.instance().TokenizeOptimized(document));
			}
			
			System.out.println("Valuating relevant cases " + Calendar.getInstance().getTime().toString());
			Pattern pattern = Pattern.compile("\\<.*\\>");
			
			for(int i=0 ; i < total.size() ; i++){				
				Matcher matcher = pattern.matcher(total.get(i));
				if(!matcher.matches()){
					positives.add(total.get(i));				
				}
			}
		}
		catch(Exception e){
			System.out.println("An error occurred while getting total positives " + e.getMessage());
		}
		
		return positives;
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
	
	private List<String> getTotalCases(File file){
		List<String> total = new ArrayList<String>();
		
		try{
			System.out.println("Reading result file " + Calendar.getInstance().getTime().toString());
			total = ResultsReader.instance().readResultFile(file.getPath());
			//total = Tokenizator.instance().Tokenize(document);
		}
		catch(Exception e){
			System.out.println("An error occurred while getting total positives " + e.getMessage());
		}
		
		return total;
	}
	
	private double getPrecision(int positivesExtractedCases, int totalExtractedCases){
		return positivesExtractedCases / totalExtractedCases;
	}
	
	private double getRecall(int positivesExtractedCases, int totalRelevantCases){
		return positivesExtractedCases / totalRelevantCases;
	}
	
	private double getF1Measure(double precision, double recall){
		return (2*precision*recall) / (precision + recall);
	}
}
