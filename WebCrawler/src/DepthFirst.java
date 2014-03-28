/**
 * @author John Wiltberger
 *
 */

// Note on regex, the code for parsing out the URL from a string was found here: http://blog.houen.net/java-get-url-from-string/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;

public class DepthFirst {
	private ArrayList<String> links = new ArrayList<String>();
	private int depthLimit, stringMatch;
	private static int currentDepth;
	private String qryString, urlString;
	private String regex = ".*href=\"([^\"]*?)\".*";
	private Pattern p = Pattern.compile(regex);
	private URLConnection con = null, rcon = null;
	private InputStream ins = null, rins = null;
	private InputStreamReader isr = null, risr = null;
	private static ArrayList<String> robots = new ArrayList<String>();
	private static ArrayList<String> visited = new ArrayList<String>();
	private final int CON_TIMEOUT = 4000;  		// Connection timeout (in milliseconds)
	
	// need to have hash table for urls

	public DepthFirst(String url, int dLimit, int curDepth, int strMatch, String query) {
		this.depthLimit = dLimit;
		currentDepth = curDepth;
		this.stringMatch = strMatch;
		this.qryString = query;
		this.urlString = url;
		
		// Actually start the searching (add to a separate class later)
		try {
			URL urlSearch = new URL(this.urlString);
			if (this.urlString.matches("^https")) {
				con = (HttpsURLConnection)urlSearch.openConnection();
			} else {
				con = urlSearch.openConnection();
			}
			con.setConnectTimeout(CON_TIMEOUT);
			
			// Change user agent
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			ins = con.getInputStream();
		} catch (MalformedURLException e) {
			System.out.println("Unable to connect to URL!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			System.out.println("Unable to open connection");
			e.printStackTrace();
		}
		
	    this.isr = new InputStreamReader(ins);
		BufferedReader br = new BufferedReader(this.isr);
		String curLine = null;
		try {
			curLine = br.readLine();
		} catch (IOException e) {
			System.out.println("Cannot read from site!");
			System.exit(1);
		}
		
		// populate robots.txt info
		if (curDepth == dLimit) {
			try {
				URL robotSearch = new URL(this.urlString + "/robots.txt");
				if (this.urlString.matches("^https.*")) {
					rcon = (HttpsURLConnection)robotSearch.openConnection();
				} else {
					rcon = robotSearch.openConnection();
				}
				rins = rcon.getInputStream();
			} catch (MalformedURLException e) {
				System.out.println("Unable to connect to robots.txt!");
				rcon = null;
			} catch (FileNotFoundException e) {
				rcon = null;
			} catch (IOException e) {
				System.out.println("Unable to open robots.txt");
				rcon = null;
			}
			if (!(rcon == null)) {
			    risr = new InputStreamReader(rins);
				BufferedReader rbr = new BufferedReader(risr);
				String curRobotLine = null;
				try {
					curRobotLine = rbr.readLine();
				} catch (IOException e1) {
					System.out.println("Can't read robots.txt");
				}
				if (curRobotLine == null) { 
					try {
						curRobotLine = rbr.readLine();
					} catch (IOException e) {
						System.out.println("Cannot read from site!");
						System.exit(1);
					}
				}
				while (!(curRobotLine == null)) {
					Pattern urlP = Pattern.compile("Disallow: (.*)");
					Matcher rm = urlP.matcher(curRobotLine);
					if (rm.matches()) {
						robots.add(this.urlString + rm.group(1));
					}
					try {
						curRobotLine = rbr.readLine();
					} catch (IOException e) {
						System.out.println("Connection was lost!");
					}
				}
			}
			
		}
		// check to make sure the depth hasn't hit its minimum
		if ( curDepth >= 0 )
		{
			// string matching here
			while(curLine != null) {
				switch(strMatch) {
				case 0:
					//LCS
					break;
				case 1:
					//Naive String
					NaiveString nsURL = new NaiveString("href=", curLine);
					String result = nsURL.matches();
					String fullStr = null;
					if (result != null) {
						Matcher m = p.matcher(result);
						if (m.matches()) {
							String urlStr = m.group(1);
							fullStr = normalizeUrl(urlStr);
							if (!(fullStr == null))
								ConsoleCrawler.pw.println(fullStr);
							if (!(fullStr == null)) {
								if (robotSafe(fullStr)) {
									links.add(fullStr);
									visited.add(fullStr);
								}
							}
						}
					}	
					// For matching the search query, deal with later
					NaiveString nss = new NaiveString(this.qryString, curLine);
					String queryMatch = nss.matches();
					if (!(queryMatch == null) && !(fullStr == null))
						System.out.println(fullStr);
					break;
				case 2:
					//Rabin-Karp
					break;
				case 3:
					//Finite Automata
					break;
				case 4:
					//KMP
					break;
				default:
					System.out.println("Incorrect String matching algorithm selected!");
					System.exit(1);
					break;
				}
				try {
					curLine = br.readLine();
				} catch (IOException e) {
					System.out.println("Cannot read from site!");
					System.exit(1);
				}
			}
			
			// TEST to make sure the algorithm is going recursively
			//System.out.println("The current depth is: " + (4 - currentDepth));
			for (int i = 0; i < links.size(); i++) {
				DepthFirst next = new DepthFirst(this.links.get(i), this.depthLimit, currentDepth - 1, this.stringMatch, this.qryString);
				currentDepth++;
			}
		}
	}
	
	/**
	 * Normalizes the URL to be added to the search list
	 * @param url Seeding string for the url
	 * @return Normalized URL to be added to links
	 */
	public String normalizeUrl(String url) {
		String result;
		String re = "\\/.*\\/(.*)$";
		String linkList = visited.toString();
		
		if ((url.startsWith("http"))) {
			if (!(url.startsWith("http://www")))
					url = url.replace("http://", "http://www.");
			if (!url.startsWith(this.urlString))
				result = null;
			else
				result = url;
		}
		else if (url.contains(":"))
			result = null;
		else if (url.startsWith("/"))
			result = ConsoleCrawler.urlText + url;
		else if (url.startsWith("./")) {
			url.replace(".", "");
			result = this.urlString.replace(re, url);
		} else
			result = ConsoleCrawler.urlText + "/" + url;
		if (!(result == null) && result.contains("?"))
			result = result.replaceFirst("\\?.*", "");
		if (!(linkList == "[]")) {	
			if (!(result == null) && visited.contains(result))
				result = null;
		}
		return result;
	}
	
	/**
	 * Checks to make sure the URL is safe to traverse
	 * @param url URL to check
	 * @return Boolean on where it follows robots.txt policy
	 */
	public boolean robotSafe(String url) {
		if (!(robots == null)) {
			for (int i = 0; i < robots.size(); i++) {
				if (url.startsWith(robots.get(i)))
					return false;
			}
		}
		return true;
	}

}
