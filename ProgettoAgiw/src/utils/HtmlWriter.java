package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class HtmlWriter {
	
	private static HtmlWriter instance = new HtmlWriter();
	
	private HtmlWriter(){}
	
	public static HtmlWriter instance(){
		return instance;
	}
	
	public boolean writeHtmlFile(File file, List<String> texts){
		FileWriter fr = null;
		BufferedWriter br = null;
		
		try{
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			
			for(String s : texts){
				br.write(s);
				br.newLine();
			}
			//br.flush();
		}
		catch(FileNotFoundException e){
			return false;
		} 
		catch (IOException e) {
			return false;
		}
		finally{
			try{				
				if(br != null){
					br.close();
				}
				if(fr != null){
					fr.close();
				}
			}
			catch(IOException e){
				return false;
			}
		}
		
		return true;
	}

}
