/**
 * BFS.java
 * @author John Wiltberger
 * This class handles the Breadth First Searching algorithm for
 * the web crawler.  Probably will be based off of the provided 
 * for the class.
 * 
 * Both search classes will need Graph (and adjacency), Vertex, and more.  See BFS example for walkthrough
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class BFS {
	private int strMatchAlg;			// Integer representing the chosen string matching algorithm
	private String url, qText;	// String representing the URL and query text
	
	/**
	 * Constructor for BFS.  Takes in the important information for the class
	 * @param url String representation of the URL
	 * @param qText String representation of the query text
	 * @param strMatchChoice Integer for the string matching choice
	 */
	public BFS(String url, String qText, int strMatchChoice) {
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
			URLConnection con = null;
			InputStream ins = null;
			InputStreamReader isr = null;
			try {
				URL urlSearch = new URL(getUrl());
				if (getUrl().matches("^https")) {
					con = (HttpsURLConnection)urlSearch.openConnection();
				} else {
					con = urlSearch.openConnection();
				}
				// Hehe, look like a real web browser
				con.setRequestProperty("User-Agent", "EpiCrawl v1.0");
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
	    	String tmpLine = " ";
			// Pull the first domain, and search content.
			
			// Use string matching algorithm here to match query text
			switch(this.getStrMatchAlg()) {
			
				// Longest Common Sequence
				case(0):
					try {
						while((tmpLine = br.readLine()) != null && this.isSearching()) {
							LCS lcs = new LCS();
							// for testing
							String pat;
							pat = lcs.match(this.getqText(), tmpLine);
							//pat = nss.giveString();
							System.out.println(pat);
						}
					} catch (MalformedURLException e) {
						System.out.println("Unable to open URL!");
						System.exit(1);
					} catch (IOException e) {
						
					}
					break;
					
				// Naive String Matching
				case(1):
					try {
						tmpLine = br.readLine();
						while(tmpLine != null && this.isSearching()) {
							NaiveString nss = new NaiveString(this.getqText(), tmpLine);
							String pat;
							pat = nss.matches();
							if (pat != null)
								System.out.println(pat);
							tmpLine = br.readLine();
						}
					} catch (MalformedURLException e) {
						System.out.println("Unable to open URL!");
						System.exit(1);
					} catch (IOException e) {
						
					}
					break;
					
				// Rabin-Karp String Matching
				case(2):
					try {
						while((tmpLine = br.readLine()) != null && this.isSearching()) {
							RabinKarp rks = new RabinKarp(this.getqText(), tmpLine);
							// for testing
							rks.test();
							System.out.println(tmpLine);
						}
					} catch (MalformedURLException e) {
						System.out.println("Unable to open URL!");
						System.exit(1);
					} catch (IOException e) {
						
					}
					break;
					
				// Finite Automata String Matching
				case(3):
					try {
						while((tmpLine = br.readLine()) != null && this.isSearching()) {
							FiniteAutomata fas = new FiniteAutomata(this.getqText(), tmpLine);
							// for testing
							fas.test();
						}
					} catch (MalformedURLException e) {
						System.out.println("Unable to open URL!");
						System.exit(1);
					} catch (IOException e) {
						
					}
					break;
				
				// Knuth-Morris-Pratt String Matching
				case(4):
					try {
						while((tmpLine = br.readLine()) != null && this.isSearching()) {
							KMP kmp = new KMP(this.getqText(), tmpLine);
							// for testing
							kmp.test();
						}
					} catch (MalformedURLException e) {
						System.out.println("Unable to open URL!");
						System.exit(1);
					} catch (IOException e) {
						
					}
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
		return true;
	}
}
