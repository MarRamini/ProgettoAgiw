package test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Metrics;
import model.Text;
import model.TextSet;
import tex.Main;
import tex.MetricsCalculator;
import tex.Tex;
import utils.HtmlReader;
import utils.HtmlWriter;
import utils.Tokenizator;

public class Test {
	
	public static void main(String[] args) {
		
		/*Text t1 = new Text();
		t1.setTokens(Tokenizator.instance().Tokenize("<html><head><title>Results</title></head><body>Catch Me<br /><b>Lisa Gardner</b><br /> $14.94<br /></body></html>"));
		Text t2 = new Text();
		t2.setTokens(Tokenizator.instance().Tokenize("<html><head><title>Results</title></head><body>Raylan<br /><b>Elmore Leonard</b><br /> $19.54<br />Wonderstruck<br /><b>Brian Selznick</b><br />$19.54<br /></body></html>"));
		Text t3 = new Text();
		t3.setTokens(Tokenizator.instance().Tokenize("<html><head><title>Results</title></head><body>Divergent<br /><b>Veronica Roth</b><br /> $9.99<br />West<br /><b>R.J. Singer</b><br /> $3.49<br /></body></html>"));
		TextSet ts = new TextSet();
		ts.getList().add(t1);
		ts.getList().add(t2);
		ts.getList().add(t3);
		
		List<TextSet> input = new ArrayList<TextSet>();
		input.add(ts);
		
		List<TextSet> result = Tex.instance().tex(input, 13, 1);
		
		for(TextSet s : result){
			for(Text t : s.getList()){
				System.out.println(t.toString());
			}
		}*/
		//Main main = new Main();		
		//main.doTex("C:\\Users\\Cerberus 2.0\\Desktop\\alf-dataset");
		//File file = testCreateEmptyFile("C:\\Users\\Cerberus 2.0\\Desktop\\TestCreateNewFile\\test.txt");
		String datasetFolder = "C:\\Users\\Cerberus 2.0\\Desktop\\alf-dataset";
		String resultFolder = "C:\\Users\\Cerberus 2.0\\Desktop\\TexResults";
		Map<String, Metrics> res = MetricsCalculator.instance().getMetrics(datasetFolder, resultFolder);
		
		/*File metricsRes = new File("C:\\Users\\Cerberus 2.0\\Desktop\\metricsResults");
		metricsRes.mkdir();
		List<String> toWrite = new ArrayList<String>();
		for(String s : res.keySet()){			
			toWrite.add(s);
			toWrite.add("precision: " + String.valueOf(res.get(s).getPrecision()));
			toWrite.add("recall: " + String.valueOf(res.get(s).getRecall()));
			toWrite.add("f-measure: " + String.valueOf(res.get(s).getF1Measure()));
		}
		
		HtmlWriter.instance().writeHtmlFile(metricsRes, toWrite);*/
	}
	
	
	
	public static File testCreateEmptyFile(String filename){
		File file = new File(filename);
		try{
			file.createNewFile();
		}
		catch(IOException e){
			System.out.println("unable to write file");
		}
		return file;	
	}
	
	public static List<Integer> findMatch(String text, String pattern,  int dimension) {
		int i = 0;
		int j = 0;
		List<Integer> result = new ArrayList<Integer>();
		
		while(i < text.length()) {
			if(j < dimension && text.charAt(i) == pattern.charAt(j)) {
				if(j == dimension - 1) {
					result.add(i - (dimension - 1));
				}
				i++;
				j++;
			}
			else if(j > 0) {
				j = 0;
			}
			else {
				i++;
			}
		}
		return result;
	}
}
