/**
 * RabinKarp.java
 * @author John Wiltberger
 * Rabin-Karp pattern matching class.  Class takes the full line and the query string
 * as inputs, and will determine if there is a match.  It will then return the full line
 * if the match exists.
 * 
 * Material taken from http://algs4.cs.princeton.edu/53substring/RabinKarp.java.html with 
 * minor modifications. Only runs Las Vegas style.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class RabinKarp {
    private String pat;      // the pattern  // needed only for Las Vegas
    private long patHash;    // pattern hash value
    private int M;           // pattern length
    private long Q;          // a large prime, small enough to avoid long overflow
    private int R;           // radix
    private long RM;         // R^(M-1) % Q
    private static ArrayList<String> seenLinks = new ArrayList<String>();


    public RabinKarp(String pat) {
        this.pat = pat;      // save pattern (needed only for Las Vegas)
        R = 256;
        M = pat.length();
        Q = longRandomPrime();

        // precompute R^(M-1) % Q for use in removing leading digit
        RM = 1;
        for (int i = 1; i <= M-1; i++)
           RM = (R * RM) % Q;
        patHash = hash(pat, M);
    } 

    // Compute hash for key[0..M-1]. 
    private long hash(String key, int M) { 
        long h = 0; 
        for (int j = 0; j < M; j++) 
            h = (R * h + key.charAt(j)) % Q; 
        return h; 
    } 

    // Las Vegas version: does pat[] match txt[i..i-M+1] ?
    private boolean check(String txt, int i) {
        for (int j = 0; j < M; j++) 
            if (pat.charAt(j) != txt.charAt(i + j)) 
                return false; 
        return true;
    }

    // check for exact match
    public int search(String txt) {
        int N = txt.length(); 
        if (N < M) return -1;
        long txtHash = hash(txt, M); 

        // check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0))
            return 0;

        // check for hash match; if hash match, check for exact match
        for (int i = M; i < N; i++) {
            // Remove leading digit, add trailing digit, check for match. 
            txtHash = (txtHash + Q - RM*txt.charAt(i-M) % Q) % Q; 
            txtHash = (txtHash*R + txt.charAt(i)) % Q; 

            // match
            int offset = i - M + 1;
            if ((patHash == txtHash) && check(txt, offset))
                return offset;
        }

        // no match
        return -1;
    }


    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
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