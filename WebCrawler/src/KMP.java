import java.util.ArrayList;

/**
 * KMP.java
 * @author John Wiltberger
 * KMP pattern matching class.  Class takes the full line and the query string
 * as inputs, and will determine if there is a match.  It will then return the full line
 * if the match exists.
 * 
 * Code used from http://algs4.cs.princeton.edu/53substring/KMP.java with minor
 *  modifications to fit in the program.
 */

public class KMP {
    private final int R;       // the radix
    private int[][] dfa;       // the KMP automoton
    private String pat;        // or the pattern string
    private static ArrayList<String> seenLinks = new ArrayList<String>();


    // create the DFA from a String
    public KMP(String pat) {
        this.R = 256;
        this.pat = pat;

        // build DFA from pattern
        int M = pat.length();
        dfa = new int[R][M]; 
        dfa[pat.charAt(0)][0] = 1; 
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++) 
                dfa[c][j] = dfa[c][X];     // Copy mismatch cases. 
            dfa[pat.charAt(j)][j] = j+1;   // Set match case. 
            X = dfa[pat.charAt(j)][X];     // Update restart state. 
        } 
    } 

    // return offset of first match; N if no match
    public int search(String txt) {

        // simulate operation of DFA on text
        int M = pat.length();
        int N = txt.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
        	if(txt.charAt(i) <= 255)					// save from unprintable characters
        		j = dfa[txt.charAt(i)][j];
        }
        if (j == M) return i - M;    // found
        return -1;                    // not found
    }
    
    public void addLinks(String link) {
    	seenLinks.add(link);
    }
    
    public boolean newLink(String link) {
    	if (seenLinks.contains(link)) {
    		return false;
    	}
    	return true;
    }
}
