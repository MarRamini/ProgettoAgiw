package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
	
	public boolean writeHtmlFileTest(File file, List<String> texts){
				
		try{
			Path filePath = Paths.get(file.getPath());
			byte[] emptyFile = new byte[0];
			Files.write(filePath, emptyFile, StandardOpenOption.APPEND);
			Files.write(filePath, texts, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
		}
		catch(FileNotFoundException e){
			return false;
		} 
		catch (IOException e) {
			return false;
		}
		
		return true;
	}

}
