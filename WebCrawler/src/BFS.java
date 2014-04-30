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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import javax.net.ssl.HttpsURLConnection;

public class BFS {
	private static Queue<String> links = new LinkedList<String>();	// Store links to be visited
	private int strMatchAlg;			// Integer representing the chosen string matching algorithm
	private String url, qText;	// String representing the URL and query text
	private URLConnection con = null;
	private InputStream ins = null;
	private InputStreamReader isr = null;
	private final int CON_TIMEOUT = 4000;  		// Connection timeout (in milliseconds)
	private static int visitedLinks = 0;		// Store number of links visited
	private static int maxLinksVisit = 0;		// Maximum visited links
	private double matchMin = 1;				// Sets the minimum characters that must match in the query


	/**
	 * Constructor for BFS.  Takes in the important information for the class
	 * @param url String representation of the URL
	 * @param qText String representation of the query text
	 * @param strMatchChoice Integer for the string matching choice
	 */
	public BFS(String url, int maxPages, String qText, int strMatchChoice) {
		// Any constructor stuff goes here
		this.setqText(qText);
		maxLinksVisit = maxPages;
		this.setUrl(url);
		this.setStrMatchAlg(strMatchChoice);
		this.matchMin = Math.floor(getqText().length() * 0.6);

		// Attempt to open the connection
		try {
			URL urlSearch = new URL(this.url);
			if (this.url.matches("^https")) {
				con = (HttpsURLConnection)urlSearch.openConnection();
			} else {
				con = urlSearch.openConnection();
			}
			con.setConnectTimeout(CON_TIMEOUT);
			con.setRequestProperty("User-Agent", "EpiCrawl v1.0");
			try {
				ins = con.getInputStream();
			} catch (IOException e) {
				return;
			}
		} catch (MalformedURLException e) {
			CrHandler.printOut("Unable to connect to URL!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			CrHandler.printOut("Unable to open connection!");
			e.printStackTrace();
		}
		
		// Begin reading from the site
	    this.isr = new InputStreamReader(ins);
		BufferedReader br = new BufferedReader(this.isr);
		String curLine = null;
		try {
			curLine = br.readLine();
		} catch (IOException e) {
			CrHandler.printOut("Cannot read from site!");
			System.exit(1);
		}
		
		// populate robots.txt info
		if (visitedLinks == 0) {
			CrHandler.buildRobots();
		}
		// check to make sure the depth hasn't hit its minimum
		if ( visitedLinks < maxLinksVisit )
		{
			// string matching here
			while(curLine != null) {
				switch(getStrMatchAlg()) {
				case 0:
					//LCS
					
					// First check the site for the links and populate the links array
					String lfullStr = CrHandler.populateLinks(curLine);
					if (!(lfullStr == null)) {
						if ( visitedLinks < maxLinksVisit ) {
							links.add(lfullStr);
							visitedLinks++;
						}
					}
					// Run String Matching
					LCS lcs = new LCS();
					String lcsQueryMatch = lcs.match(getqText(), curLine);
					if (!(lfullStr == null) && lcsQueryMatch.length() >= this.matchMin) {
						CrHandler.printOut(lfullStr);
					}
					break;
				case 1:
					//Naive String

					// First check the site for the links and populate the links array
					String nfullStr = CrHandler.populateLinks(curLine);
					if (!(nfullStr == null)) {
						if ( visitedLinks < maxLinksVisit ) {
							links.add(nfullStr);
							visitedLinks++;
						}
					}
					// Run String Matching
					NaiveString nss = new NaiveString(getqText(), curLine);
					String queryMatch = nss.matches();
					if (!(queryMatch == null) && !(nfullStr == null))
						CrHandler.printOut(nfullStr);
					break;
					//}
				case 2:
					//Rabin-Karp

					// First check the site for the links and populate the links array
					String rfullStr = CrHandler.populateLinks(curLine);
					if (!(rfullStr == null)) {
						if ( visitedLinks < maxLinksVisit ) {
							links.add(rfullStr);
							visitedLinks++;
						}
					}
					// Run String Matching
					RabinKarp rk = new RabinKarp(getqText());
					int found = rk.search(curLine);
					if (found >= 0)
						if (rk.newLink(this.getUrl())) {
							CrHandler.printOut(this.getUrl());
							rk.addLinks(this.getUrl());
						}
					break;
				case 3:
					//Finite Automata

					// First check the site for the links and populate the links array
					String ffullStr = CrHandler.populateLinks(curLine);
					if (!(ffullStr == null)) {
						if ( visitedLinks < maxLinksVisit ) {
							links.add(ffullStr);
							visitedLinks++;
						}
					}
					// Run String Matching
					
					break;
				case 4:
					//KMP

					// First check the site for the links and populate the links array
					String kfullStr = CrHandler.populateLinks(curLine);
					if (!(kfullStr == null)) {
						if ( visitedLinks < maxLinksVisit ) {
							links.add(kfullStr);
							visitedLinks++;
						}
					}
					// Run String Matching
					
					break;
				default:
					CrHandler.printOut("Incorrect String matching algorithm selected!");
					System.exit(1);
					break;
				}
				
				// Read the next line after the match
				try {
					curLine = br.readLine();
				} catch (IOException e) {
					CrHandler.printOut("Cannot read from site!");
					System.exit(1);
				}
			}
			
			// Search through the links that were populated (recursive part of the class)
			while (!(links.isEmpty())) {
				new BFS(links.remove(), maxLinksVisit, qText, getStrMatchAlg());
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
	
	public void clearLinks() {
		links.clear();
		CrHandler.clearLinks();
	}
}
