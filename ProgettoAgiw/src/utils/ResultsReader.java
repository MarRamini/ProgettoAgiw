package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsReader {
	private static ResultsReader instance = new ResultsReader();
	
	private ResultsReader(){}
	
	public static ResultsReader instance(){
		return instance;
	}
	
	public List<String> readResultFile(String filename){
		List<String> result = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;
		
		try{
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			
			String line = "";
			do{
				line = br.readLine();
				if(line != null){
					result.add(line);
				}
			}while(line != null);				
		}
		catch(FileNotFoundException e){
			System.out.println("Unable to find file at path: " + filename);
			return null;
		} catch (IOException e) {
			System.out.println("Input/Output error");
			return null;
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
				System.out.println("Input/Output error");
				return null;
			}
		}
		
		return result;
	}

}
