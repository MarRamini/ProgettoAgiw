package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Metrics;
import model.SiteMetricsData;
import utils.HtmlReader;
import utils.ResultsReader;
import utils.Tokenizator;

public class PartialTest {
	
	public static void main(String[] args) {
		/*STEPS
		 * 
		 * 1) ciclo directory category/site
		 * 2) leggo risultati
		 * 3) leggo originali
		 * 4) creo oggetto metrics
		 * 
		 */
		List<SiteMetricsData> data = new ArrayList<SiteMetricsData>();
		
		String resDir = "";
		String dataDir = "";
		
		File resFolder = new File(resDir);
		File dataFolder = new File(dataDir);
		
		File[] resFiles = resFolder.listFiles(); //same categories between res and data
		File[] dataFiles = dataFolder.listFiles();
		
		//cicle through categories
		for(int i=0 ; i<resFiles.length && i<dataFiles.length ; i++) {
			File resCategory = resFiles[i];
			File dataCategory = dataFiles[i];
			
			System.out.println("Analizing category " + resCategory.getName() + " " + Calendar.getInstance().getTime().toString());
			
			File[] resSites = resCategory.listFiles();
			File[] dataSites = dataCategory.listFiles();
			
			//cicle through sites
			for(int j=0 ; j<resSites.length ; j++){
				File resSite = resSites[j];
				File dataSite = dataSites[j];
				
				System.out.println("Analizing site " + resSite.getName() + " " + Calendar.getInstance().getTime().toString());
				
				//sono dentro al sito, leggo i risultati;
				System.out.println("Valuating extracted cases..");
				List<String> extractedCases = getExtractedCases(new File(resSite.getPath() + "\\TexResult"));
				int totalExtractedCases = extractedCases.size();
				
				System.out.println("Valuating extracted relevant cases...");
				List<String> extractedRelevantCases = getRelevantCases(extractedCases);
				int totalExtractedRelevantCases = extractedRelevantCases.size();
				
				System.out.println("Found " + totalExtractedCases + "extracted cases, " + totalExtractedRelevantCases + " were relevant." + Calendar.getInstance().getTime().toString());
				
				if(dataSite.getName().equals(resSite.getName())) {
					System.out.println("Analizing original site pages");
					List<String> totalOriginalCases = getTotalOriginalCases(dataSite);
					System.out.println("Valuating original relevant cases");
					List<String> relevantOriginalCases = getRelevantCases(totalOriginalCases);
					
					int totalRelevantOriginalCases = relevantOriginalCases.size();
					
					System.out.println("Found " + totalRelevantOriginalCases + "relevant original cases "  + Calendar.getInstance().getTime().toString());
					
					SiteMetricsData metric = new SiteMetricsData();
					metric.setCategory(resCategory.getName());
					metric.setSiteName(resSite.getName());
					metric.setExtractedPositiveCases(totalExtractedRelevantCases);
					metric.setExtractedTotalcases(totalExtractedCases);
					metric.setTotalRelevantCases(totalRelevantOriginalCases);
					
					data.add(metric);
				}				
			}
			
		}
		System.out.println("DONE");
	}
	
	private static List<String> getExtractedCases(File file){
		List<String> total = new ArrayList<String>();
		
		try{
			System.out.println("Reading result file " + Calendar.getInstance().getTime().toString());
			total = ResultsReader.instance().readResultFile(file.getPath());
		}
		catch(Exception e){
			System.out.println("An error occurred while getting total extracted " + e.getMessage());
		}
		
		return total;
	}
	
	private static List<String> getRelevantCases(List<String> totalCases){
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
	
	private static List<String> getTotalOriginalCases(File file){
		List<String> total = new ArrayList<String>();
		List<String> filenames = new ArrayList<String>();
		File pagesFolder = new File(file.getPath() + "\\pages");
		try{
			getFileNames(pagesFolder, filenames);
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
	
	private static void getFileNames(File folder, List<String> filenames){
		for (File file : folder.listFiles()) {
	        if (file.isDirectory()) {
	        	getFileNames(file, filenames); // Calls same method again.
	        } else {
	           filenames.add(file.getPath());
	        }
	    }
	}
}
	/*
	 Vecchia Logica
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
	 
	 
	 
	 
	 */
