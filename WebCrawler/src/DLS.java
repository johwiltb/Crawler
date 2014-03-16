/**
 * DLS.java
 * @author John Wiltberger
 * This class handles the Depth Limited Searching algorithm for
 * the web crawler.  Will probably be written after the BFS
 * class as it will lead to a better understanding on how to
 * complete the search.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DLS {
	private int strMatchAlg; 						// Integer representing the chosen string matching algorithm
	private String url, qText;						// String representing the URL and query text
	private boolean searching = false; 				// Check to see if searching is running
	private final static int MAX_SEARCH_DEPTH = 4;	// Integer representing the maximum search depth for the depth-limited search
	
	/**
	 * Constructor for DLS.  Takes in the important information for the class
	 * @param url String representation of the URL
	 * @param qText String representation of the query text
	 * @param strMatchChoice Integer for the string matching choice
	 */
	public DLS(String url, String qText, int strMatchChoice) {
		// Any constructor stuff goes here

		// For Test Purposes
		this.setqText(qText);
		this.setUrl(url);
		this.setStrMatchAlg(strMatchChoice);
	}
	
	/**
	 * Searching method, will be the system that initiates the tree building, as well as dives toward
	 * the string matching algorithm, and will handle the result info.
	 */
	public void search() {
		if (this.isSearching()) {
			int numOfSites = 0;								// Variable to track how deep the search has gone
			URLConnection con = null;
			InputStream ins = null;
			InputStreamReader isr = null;
			
			// Attempt to connect to the site, and match both HTTP and HTTPS
			try {
				URL urlSearch = new URL(getUrl());
				if (getUrl().matches("^https")) {
					con = (HttpsURLConnection)urlSearch.openConnection();
				} else {
					con = urlSearch.openConnection();
				}
				
				// Hehe, look like a real web browser
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
				ins = con.getInputStream();
			    isr = new InputStreamReader(ins);
			} catch (MalformedURLException e1) {
				System.out.println("Unable to connect to URL!");
				e1.printStackTrace();
			} catch (IOException e) {
				System.out.println("Unable to open https connection");
				e.printStackTrace();
			}
	    	BufferedReader br = null;
			br = new BufferedReader(isr);
	    	String tmpLine = " ";			// Set for a space so it matches initial readLine
			
			// Use string matching algorithm here to match query text
			switch(this.getStrMatchAlg()) {
			
				// Longest Common Sequence
				case(0):
					while(numOfSites < MAX_SEARCH_DEPTH) {
						try {
							while(tmpLine != null && this.isSearching()) {
								tmpLine = br.readLine();
								LCS lcs = new LCS();
								// for testing
								String pat;
								pat = lcs.match(this.getqText(), tmpLine);
								//pat = nss.giveString();
								if (pat != null)
									System.out.println(tmpLine);
								numOfSites++;
							}
						} catch (MalformedURLException e) {
							System.out.println("Unable to open URL!");
							System.exit(1);
						} catch (IOException e) {
							
						}
					}
					numOfSites++;	
					break;
					
				// Naive String Matching
				case(1):
					while(numOfSites < MAX_SEARCH_DEPTH) {
						try {
							while(tmpLine != null && this.isSearching()) {
								tmpLine = br.readLine();
								NaiveString nss = new NaiveString(this.getqText(), tmpLine);
								// for testing
								nss.test();
								String pat;
								//pat = nss.giveString();
								//System.out.println(pat);
							}
						} catch (MalformedURLException e) {
							System.out.println("Unable to open URL!");
							System.exit(1);
						} catch (IOException e) {
							
						}
					}
					numOfSites++;
					break;
					
				// Rabin-Karp String Matching
				case(2):
					while(numOfSites < MAX_SEARCH_DEPTH) {
						try {
							while(tmpLine != null && this.isSearching()) {
								tmpLine = br.readLine();
								RabinKarp rks = new RabinKarp(this.getqText(), tmpLine);
								// for testing
								rks.test();
								System.out.println(tmpLine);
								String pat;
								//pat = nss.giveString();
								//System.out.println(pat);
							}
						} catch (MalformedURLException e) {
							System.out.println("Unable to open URL!");
							System.exit(1);
						} catch (IOException e) {
							
						}
					}
					numOfSites++;
					break;
					
				// Finite Automata String Matching
				case(3):
					while(numOfSites < MAX_SEARCH_DEPTH) {
						try {
							while(tmpLine != null && this.isSearching()) {
								tmpLine = br.readLine();
								FiniteAutomata fas = new FiniteAutomata(this.getqText(), tmpLine);
								// for testing
								fas.test();
								String pat;
								//pat = nss.giveString();
								//System.out.println(pat);
							}
						} catch (MalformedURLException e) {
							System.out.println("Unable to open URL!");
							System.exit(1);
						} catch (IOException e) {
							
						}
					}
					numOfSites++;
					break;
					
				// Knuth-Morris-Pratt String Matching
				case(4):
					while(numOfSites < MAX_SEARCH_DEPTH) {
						try {
							while(tmpLine != null && this.isSearching()) {
								tmpLine = br.readLine();
								KMP kmp = new KMP(this.getqText(), tmpLine);
								// for testing
								kmp.test();
								String pat;
								//pat = nss.giveString();
								//System.out.println(pat);
							}
						} catch (MalformedURLException e) {
							System.out.println("Unable to open URL!");
							System.exit(1);
						} catch (IOException e) {
							
						}
					}
					numOfSites++;
					break;
				
				// Default Case
				default:
					System.out.println("Invalid String Matching Algorithm selected!");
					System.exit(1);
			}
		}
	}
	
	/**
	 * Accessor for the string matching algorithm
	 * @return integer representing chosen string matching algorithm
	 */
	public int getStrMatchAlg() {
		return strMatchAlg;
	}

	/**
	 * Mutator for string matching algorithm integer
	 * @param strMatchAlg integer representing strMatchAlg choice
	 */
	public void setStrMatchAlg(int strMatchAlg) {
		this.strMatchAlg = strMatchAlg;
	}

	/**
	 * Accessor for the URL used in the query
	 * @return string of URL requested
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Mutator for the URL used in the query
	 * Adds "http://" if not present
	 * @param url string of URL requested
	 */
	public void setUrl(String url) {
		if (!url.matches("^http.*"))
			this.url = "http://" + url;
		else
			this.url = url;
	}

	/**
	 * Accessor for the text to be queried
	 * @return text string used in query
	 */
	public String getqText() {
		return qText;
	}

	/**
	 * Mutator for the text to be queried
	 * @param qText text string used in query
	 */
	public void setqText(String qText) {
		this.qText = qText;
	}
	
	/**
	 * Check to see if a search is running
	 * @return whether searching is running or not
	 */
	public boolean isSearching() {
		return searching;
	}

	/**
	 * Changes state of searching
	 * @param searching True/False for currently searching
	 */
	public void setSearching(boolean searching) {
		this.searching = searching;
	}
}
