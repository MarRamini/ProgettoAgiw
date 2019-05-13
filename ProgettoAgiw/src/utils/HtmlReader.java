package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HtmlReader {
	
	private static HtmlReader instance = new HtmlReader();
	
	private HtmlReader(){}
	
	public static HtmlReader instance(){
		return instance;
	}
	
	public String readHtmlFile(String filename){
		String result = "";
		FileReader fr = null;
		BufferedReader br = null;
		
		try{
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			
			String line = "";
			do{
				line = br.readLine();
				if(line != null){
					result += line;
				}
			}while(line != null);				
		}
		catch(FileNotFoundException e){
			return "Unable to find file at path: " + filename;
		} catch (IOException e) {
			return "Input/Output error";
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
				return "Input/Output error";
			}
		}
		
		return result;
	}

}
