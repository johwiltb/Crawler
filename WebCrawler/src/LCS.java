/**
 * LCS.java
 * @author John Wiltberger
 * Longest Common Sequence pattern matching class.  Class takes the full line and the query string
 * as inputs, and will determine if there is a match.  It will then return the full line
 * if the match exists.
 */

public class LCS {
	
	public LCS() {
		
	}
	
	public void test() {
		System.out.println("Starting Longest Common Sequence");
	}
	
	public String match(String query, String fullLine){
	    int qTextLen = query.length();
	    int fLineLen = fullLine.length();
	    if(qTextLen == 0 || fLineLen == 0){
	        return null;
	    }else if(query.charAt(qTextLen-1) == fullLine.charAt(fLineLen-1)){
	        return match(query.substring(0,qTextLen-1),fullLine.substring(0,fLineLen-1))
	            + query.charAt(qTextLen-1);
	    }else{
	        String a = match(query, fullLine.substring(0,fLineLen-1));
	        String b = match(query.substring(0,qTextLen-1), fullLine);
	        return (a.length() > b.length()) ? a : b;
	    }
	}
}
