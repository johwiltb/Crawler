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
	private URLConnection con = null;
	private InputStream ins = null;
	private InputStreamReader isr = null;
	private static ArrayList<String> visited = new ArrayList<String>();
	private final int CON_TIMEOUT = 4000;  		// Connection timeout (in milliseconds)
	
	// need to have hash table for urls

	public DepthFirst(String url, int dLimit, int curDepth, int strMatch, String query) {
		this.depthLimit = dLimit;
		currentDepth = curDepth;
		this.stringMatch = strMatch;
		this.qryString = query;
		this.urlString = url;
		
		if (EpiCrawl.stopPressed) {
			CrHandler.printOut("Interruption Detected!\nThank you for using!");
			System.exit(1);
		}
		//currentDepth++;	
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
							if (!(fullStr == null)) {
								if (CrHandler.robotsSafe(fullStr)) {
									CrHandler.pw.println(fullStr);
									links.add(fullStr);
									visited.add(fullStr);
								}
							}
						}
					}	
					NaiveString nss = new NaiveString(this.qryString, curLine);
					String queryMatch = nss.matches();
					if (!(queryMatch == null) && !(fullStr == null))
						CrHandler.printOut(fullStr);
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
					CrHandler.printOut("Incorrect String matching algorithm selected!");
					System.exit(1);
					break;
				}
				try {
					curLine = br.readLine();
				} catch (IOException e) {
					CrHandler.printOut("Cannot read from site!");
					System.exit(1);
				}
			}
			
			// TEST to make sure the algorithm is going recursively
			//CrHandler.printOut("The current depth is: " + (4 - currentDepth));
			for (int i = 0; i < links.size(); i++) {
				new DepthFirst(this.links.get(i), this.depthLimit, currentDepth - 1, this.stringMatch, this.qryString);
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
		else if (CrHandler.normURL.contains(url)) {
			String replacement = CrHandler.normURL.replaceFirst("http.?:\\/\\/", "");
			url = url.replaceFirst("^.*" + replacement, "");
			result = CrHandler.normURL + url;
		}
		else if (url.startsWith("//")) {
			String adder = url.replaceFirst("\\/\\/[^\\/].*?\\/", "");
			result = CrHandler.normURL + "/" + adder;
		}
		
		else if (url.startsWith("/"))
			result = CrHandler.normURL + url;
		// add ../ condition
		else if (url.startsWith("../")) {
			String adder = null;
			while (url.contains("../")) {
				adder = this.urlString.replaceFirst("(\\/[^\\/]*?)\\/[^\\/]*?$", "");
				url = url.replaceFirst("\\.\\.", "");
			}
			if (url.contains("//"))
				url = url.replaceAll("//", "/");
			result = adder + url;
		}
		else if (url.startsWith("./")) {
			url.replace(".", "");
			result = this.urlString.replace(re, url);
		} else
			result = CrHandler.normURL + "/" + url;
		if (!(result == null) && result.contains("?"))
			result = result.replaceFirst("\\?.*", "");
		if (!(result == null) && result.contains("#"))
			result = result.replaceFirst("#.*", "");
		if (!(result == null) && !(result.contains(CrHandler.normURL)))
			result = null;
		if (!(linkList == "[]")) {	
			if (!(result == null) && visited.contains(result))
				result = null;
		}
		return result;
	}
}
