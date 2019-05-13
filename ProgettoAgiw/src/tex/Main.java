package tex;

import java.io.File;
import java.util.ArrayList;
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
			System.out.println("Analizing folder " + type.getName());
			for(File site : type.listFiles()){
				System.out.println("Analizing folder " + site.getName());
				if(!site.getName().contains("desktop.ini")){
					List<String> filenames = new ArrayList<String>();
					getFileNames(site, filenames);
					System.out.println("Loaded " + filenames.size() + "files");
					TextSet set = new TextSet();
					System.out.println("Tokenizing files in directory: " + site.getName());
					for(String name : filenames){
						Text text = new Text();
						text.setTokens(Tokenizator.instance().Tokenize(HtmlReader.instance().readHtmlFile(name)));
						set.getList().add(text);
					}
					System.out.println("Tokenizing done");
					sets.add(set);
					File result = new File(site.getPath() + "\\TexResult");
					result.mkdir();
					try{		
						System.out.println("Launching Tex on text set of site: " + site.getName());
						List<TextSet> resSets = Tex.instance().tex(sets, sets.get(0).getMaxMatchNumber(), 1);
						System.out.println("writing results");
						for(TextSet res : resSets){
							for(int i = 0 ; i< res.getList().size() ; i++){
								File file = new File((i + 1) + ".txt");
								writeFile(file, res.getList().get(i));
							}
						}
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
	
	
	
	private void writeFile(File file, Text text){
		boolean res = false;
		try{
			while(!res){
				res = HtmlWriter.instance().writeHtmlFile(file, text.getTokens());
			}
			file.createNewFile();
		}
		catch(Exception e){}
	}
}
