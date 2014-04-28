/**
 * DepthFirst.java
 * @author John Wiltberger
 * This class handles the Depth Limited Searching algorithm for
 * the web crawler (even though it is called DepthFirst.java).  
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;

public class DepthFirst {
	private ArrayList<String> links = new ArrayList<String>();	// Store links to be visited
	private int depthLimit, stringMatch;
	private static int currentDepth;							// Keeps track of the current depth
	private String qryString, urlString, fullStr = null;
	private URLConnection con = null;
	private InputStream ins = null;
	private InputStreamReader isr = null;
	private final int CON_TIMEOUT = 4000;  		// Connection timeout (in milliseconds)
	private double matchMin = 1;								// Sets the minimum characters that must match in the query
	
	/**
	 * Constructor for Depth First class (this is recursive)
	 * @param url URL string that will be searched
	 * @param dLimit the limit set for the depth of the search
	 * @param curDepth the current depth of the search
	 * @param strMatch the string that is getting matched
	 * @param query the string that is getting matched
	 */
	public DepthFirst(String url, int dLimit, int curDepth, int strMatch, String query) {
		this.depthLimit = dLimit;
		currentDepth = curDepth;
		this.stringMatch = strMatch;
		this.qryString = query;
		this.urlString = url;
		this.matchMin = Math.floor(this.qryString.length() * 0.6);
		
		// Attempt to open the connection
		try {
			URL urlSearch = new URL(this.urlString);
			if (this.urlString.matches("^https")) {
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
		if (curDepth == dLimit) {
			CrHandler.buildRobots();
		}
		// check to make sure the depth hasn't hit its minimum
		if ( curDepth > 0 )
		{
			// string matching here
			while(curLine != null) {
				switch(strMatch) {
				case 0:
					//LCS
					
					// First check the site for the links and populate the links array
					fullStr = CrHandler.populateLinks(curLine);
					if (!(fullStr == null))
						links.add(fullStr);
					
					// Run String Matching
					LCS lcs = new LCS();
					String lcsQueryMatch = lcs.match(this.qryString, curLine);
					if (!(fullStr == null) && lcsQueryMatch.length() >= this.matchMin) {
						CrHandler.printOut(fullStr);
					}
					break;
				case 1:
					//Naive String

					// First check the site for the links and populate the links array
					fullStr = CrHandler.populateLinks(curLine);
					if (!(fullStr == null))
						links.add(fullStr);
					
					// Run String Matching
					NaiveString nss = new NaiveString(this.qryString, curLine);
					String queryMatch = nss.matches();
					if (!(queryMatch == null) && !(fullStr == null))
						CrHandler.printOut(fullStr);
					break;
					//}
				case 2:
					//Rabin-Karp

					// First check the site for the links and populate the links array
					fullStr = CrHandler.populateLinks(curLine);
					if (!(fullStr == null))
						links.add(fullStr);
					
					// Run String Matching
					
					break;
				case 3:
					//Finite Automata

					// First check the site for the links and populate the links array
					fullStr = CrHandler.populateLinks(curLine);
					if (!(fullStr == null))
						links.add(fullStr);
					
					// Run String Matching
					
					break;
				case 4:
					//KMP

					// First check the site for the links and populate the links array
					fullStr = CrHandler.populateLinks(curLine);
					if (!(fullStr == null))
						links.add(fullStr);
					
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
			for (int i = 0; i < links.size(); i++) {
				new DepthFirst(this.links.get(i), this.depthLimit, currentDepth - 1, this.stringMatch, this.qryString);
				currentDepth++;
			}
		}
	}
}
