package tex;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Text;
import model.TextSet;
import utils.HtmlReader;
import utils.HtmlWriter;
import utils.Tokenizator;

public class Main {
	
	private List<TextSet> textSet;
	
	public List<TextSet> getTextSet() {
		return textSet;
	}

	public void setTextSet(List<TextSet> textSet) {
		this.textSet = textSet;
	}
	
	public Main(){
		this.textSet = new ArrayList<TextSet>();
	}
	
	
	public void doTex(String folder){		
		System.out.println("BEGIN-----------------------------------------------------");
		List<TextSet> sets = new ArrayList<TextSet>();		
		File root = new File(folder); //root folder of TextSet
		
		for(File type : root.listFiles()){
			System.out.println("Analizing folder " + type.getName() + " " + Calendar.getInstance().getTime().toString());
			for(File site : type.listFiles()){
				System.out.println("Analizing folder " + site.getName());
				if(!site.getName().contains("desktop.ini")){
					List<String> filenames = new ArrayList<String>();
					getFileNames(site, filenames);
					System.out.println("Loaded " + filenames.size() + "files");
					TextSet set = new TextSet();
					System.out.println("Tokenizing files in directory: " + site.getName() + " " + Calendar.getInstance().getTime().toString());
					for(String name : filenames){
						System.out.println("Tokenizing " + name);
						Text text = new Text();
						text.setTokens(Tokenizator.instance().TokenizeOptimized(HtmlReader.instance().readHtmlFile(name)));
						set.getList().add(text);
					}
					System.out.println("Tokenizing done" + " " + Calendar.getInstance().getTime().toString());
					sets.add(set);
					String resDir = site.getPath() + "\\TexResult";
					File result = new File(resDir);
					result.mkdir();
					try{		
						System.out.println("Launching Tex on text set of site: " + site.getName() + " " + Calendar.getInstance().getTime().toString());
						List<TextSet> resSets = Tex.instance().tex(sets, sets.get(0).getMaxMatchNumber(), 1);
						System.out.println("writing results" + " " + Calendar.getInstance().getTime().toString());
						File file = new File(resDir + "\\" + "Results.txt");
						writeFile(file, resSets);
					}
					catch(Exception e){
						System.out.println("unable to write file to filesystem");
					}
				}
			}
		}
		System.out.println("DONE--------------------------");
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
	
	private void writeFile(File file, List<TextSet> texts){
		boolean res = false;
		
		try{
			List<Text> textsToWrite = new ArrayList<Text>();
			for(TextSet ts : texts){
				textsToWrite.addAll(ts.getList());
			}
			
			List<String> toWrite = new ArrayList<String>();
			
			for(Text t : textsToWrite){
				toWrite.addAll(t.getTokens());
			}
			
			res = HtmlWriter.instance().writeHtmlFile(file, toWrite);
			
			if(!res){
				System.out.println("Something went wrong while writing text");
				System.out.println("Aborting writing...");
			}		
		}
		catch(Exception e){}
	}
		
	public static void main(String[] args){
		Main main = new Main();
		main.doTex("C:\\Users\\Cerberus 2.0\\Desktop\\sss");
	}
}
