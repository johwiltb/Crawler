/**
 * LCS.java
 * @author John Wiltberger
 * Longest Common Sequence pattern matching class.  Class takes the full line and the query string
 * as inputs, and will determine if there is a match.  It will then return the full line
 * if the match exists.
 */

public class LCS {
	private String matchStr = "";
	
	public LCS() {	}
	
	/**
	 * Matches the string with the query
	 * @param query Query the user is searching for
	 * @param fullLine Text for the user to search for
	 * @return The longest sequence that matches from the query string
	 */
	public String match(String query, String fullLine){
	    int qTextLen = query.length();
	    int fLineLen = fullLine.length();
	    int[][] opt = new int [qTextLen + 1][fLineLen+1];
	    
	    if(qTextLen == 0 || fLineLen == 0){
	        return null;
	    }
	    
	    // Build the table
	    for (int i = qTextLen - 1; i >= 0; i--) {
	    	for (int j = fLineLen - 1; j >=0; j--) {
	    		if (query.charAt(i) == fullLine.charAt(j))
	    			opt[i][j] = opt[i+1][j+1] + 1;
	    		else
	    			opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
	    	}
	    }
	    
	    // Return the string
	    int i = 0, j = 0;
	    while( i < qTextLen && j < fLineLen) {
	    	if (query.charAt(i) == fullLine.charAt(j)) {
	    		matchStr = matchStr + query.charAt(i);
	    		i++;
	    		j++;
	    	} else if (opt[i+1][j] >= opt[i][j+1]) 
	    		i++;
	    	else
	    		j++;
	    }
	    
	    return matchStr;
	}
}