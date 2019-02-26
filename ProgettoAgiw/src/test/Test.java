package test;
import java.util.ArrayList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		
		String text = "<html><head></head><body></div></body></html>";
		String pattern = "<html><head></head><body></body></html>";
		for(int j = pattern.length() ; j>1 ; j--) {
			List<Integer> matches = findMatch(text, pattern, j);
			System.out.println("trying for size: " + j + "---->" + matches.toString() + ", pattern size: " + j);
		}
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
