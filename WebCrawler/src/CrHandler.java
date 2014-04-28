import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * @file CrHandler.java
 * @author John Wiltberger
 * @description Class that handles information pertaining to both classes.
 * This class is easily translate the different needs of the classes, 
 * including URL normalization, robots.txt tracking, writing out information,
 * and more.
 */
public class CrHandler {
	protected static String normURL, urlString; // normURL = domain being searched, urlString = current URL being searched
	protected static PrintWriter pw;			// writes to a file (mostly for testing purposes)
	private static String iface;				// handles whether the crawler is in console or GUI mode
	private static ArrayList<String> robots = new ArrayList<String>();	// Stores robots.txt information
	private static ArrayList<String> visited = new ArrayList<String>(); // Stores links that have been visited
	private static String regex = ".*href=\"([^\"]*?)\".*";		// Matches for the 'href=' attribute
	private static Pattern p = Pattern.compile(regex);

	
	// Store necessary warning
	protected static String loadInfo = "Thank you for using EpiCrawl by John Wiltberger\n\n"
			+ "It should be noted that this program is distributed\n"
			+ "in the hope that it will be useful, but WITHOUT\n"
			+ "ANY WARRANTY; without even the implied warranty of\n"
			+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.";	


	/**
	 * Determines the proper interface for user output
	 * @param interf Either "console" or "gui"
	 */
	public CrHandler(String interf) { 
		iface = interf;
	}
	
	/**
	 * Normalizes the initial URL input
	 * @param userURL Domain that will be searched
	 */
	public void normURL (String userURL) {
		// add 'http://(www)' if it contains no header
		if (!(userURL.contains("http"))) {
			if (!(userURL.contains("www")) && !(userURL.matches(".*?\\..*?\\..*?\\."))) 
				normURL = "http://www." + userURL;
			else 
				normURL = "http://" + userURL;
		} else
			normURL = userURL;
		
		// Print links to file
		try {
			pw = new PrintWriter("./links", "UTF-8");
		} catch (FileNotFoundException e1) {
			System.out.println("Unable to open file storing links.  Check permissions"
					+ " in the current directory");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Does not accept encoding scheme for links file.");
		}
	}
	
	/**
	 * Returns normalized URL for top level domain
	 * @return top level domain
	 */
	public String getURL() {
		return normURL;
	}
	
	/**
	 * Prints output to the proper interface
	 * @param output String to be outputted
	 */
	public static void printOut(String output) {
		if (iface == "console")
		{
			System.out.println(output);
		} else if (iface == "gui") {
			EpiCrawl.resultsTxtArea.append(output + "\n");
		} else {
			System.out.println("Failure of output.  There is an error in your choices");
			System.exit(1);
		}
	}
	
	/**
	 * Builds the robots.txt information for the domain
	 */
	public static void buildRobots() {
		URLConnection rcon;
		InputStream rins = null;
		
		// Attempts connection to the robots.txt page
		try {
			URL robotSearch = new URL(CrHandler.normURL + "/robots.txt");
			if (CrHandler.normURL.matches("^https.*")) {
				rcon = (HttpsURLConnection)robotSearch.openConnection();
			} else {
				rcon = robotSearch.openConnection();
			}
			rins = rcon.getInputStream();
		} catch (MalformedURLException e) {
			CrHandler.printOut("Unable to connect to robots.txt!");
			rcon = null;
		} catch (FileNotFoundException e) {
			rcon = null;
		} catch (IOException e) {
			CrHandler.printOut("Unable to open robots.txt");
			rcon = null;
		}
		
		// Reads and populates robots.txt
		if (!(rcon == null)) {
		    InputStreamReader risr = new InputStreamReader(rins);
			BufferedReader rbr = new BufferedReader(risr);
			String curRobotLine = null;
			try {
				curRobotLine = rbr.readLine();
			} catch (IOException e1) {
				CrHandler.printOut("Can't read robots.txt");
			}
			if (curRobotLine == null) { 
				try {
					curRobotLine = rbr.readLine();
				} catch (IOException e) {
					CrHandler.printOut("Cannot read robots.txt from site!");
				}
			}
			while (!(curRobotLine == null)) {
				if (curRobotLine.matches("^[ \\t]*User-[aA]gent: \\*\\.*")) {
					while (!(curRobotLine == "") && !(curRobotLine == null)) {
						Pattern urlP = Pattern.compile("Disallow: (.*)");
						Matcher rm = urlP.matcher(curRobotLine);
						if (rm.matches()) {
							String adder = rm.group(1);
							adder = adder.replaceAll("\\.", "\\\\.");
							adder = adder.replaceAll("\\*", "\\.\\*");
							adder = adder.replaceFirst("#.*", "");
							robots.add(CrHandler.normURL + adder);
						}
						try {
							curRobotLine = rbr.readLine();
						} catch (IOException e) {
							CrHandler.printOut("Lost connection to robots.txt");
							curRobotLine = null;
						}
					}
				}
				try {
					curRobotLine = rbr.readLine();
				} catch (IOException e) {
					CrHandler.printOut("Connection was lost!");
				}
			}
		}
	}
	
	/**
	 * Checks to make sure the URL is safe to traverse
	 * @param url URL to check
	 * @return Boolean on where it follows robots.txt policy
	 */
	public static Boolean robotsSafe(String url) {
		if (!(robots == null)) {
			for (int i = 0; i < robots.size(); i++) {
				String reg = "^" + robots.get(i) + ".*$";
				if (url.matches(reg))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Add visited URL to ArrayList
	 * @param url URL to be added
	 */
	public static void addVisited(String url) {
		visited.add(url);
	}
	
	/**
	 * Checks to see if the link has been visited or not
	 * @param url URL to check for a prior visit
	 * @return whether the link was visited or not.
	 */
	public static boolean visitedLink(String url) {
		if (visited.contains(url)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Normalizes the URL before running all of the checks.
	 * @param url URL to be normalized
	 * @return normalized URL (or null if it doesn't match requirements)
	 */
	public static String normalizeUrl(String url) {
		String result;
		String re = "\\/.*\\/(.*)$";
		
		// Check for full links (non-relative)
		if ((url.startsWith("https"))) {
			if (!(url.startsWith("https://www")))
					url = url.replace("https://", "https://www.");
			if (!(url.startsWith(normURL)))
				result = null;
			else
				result = url;
		} else if ((url.startsWith("http:"))) {
			if (!(url.startsWith("http://www")))
					url = url.replace("http://", "http://www.");
			if (!(url.startsWith(normURL)))
				result = null;
			else
				result = url;
		} else if (url.contains(":"))
			result = null;
		else if (normURL.contains(url)) {
			String replacement = normURL.replaceFirst("http.?:\\/\\/", "");
			url = url.replaceFirst("^.*" + replacement, "");
			result = normURL + url;
		}
		
		// Checks for links beginning with '//' (and not from http(s)://)
		else if (url.startsWith("//")) {
			String adder = url.replaceFirst("\\/\\/[^\\/].*?\\/", "");
			result = normURL + "/" + adder;
		}
		
		// Relative links that begin '/' i.e. /page.html
		else if (url.startsWith("/"))
			result = normURL + url;
		
		// Relative links that begin '../' i.e. ../calendar.html
		else if (url.startsWith("../")) {
			String adder = null;
			while (url.contains("../")) {
				adder = normURL.replaceFirst("(\\/[^\\/]*?)\\/[^\\/]*?$", "");
				url = url.replaceFirst("\\.\\.", "");
			}
			if (url.contains("//"))
				url = url.replaceAll("//", "/");
			result = adder + url;
		}
		
		// Relative links that begin './' i.e. ./fun.html
		else if (url.startsWith("./")) {
			url.replace(".", "");
			result = normURL.replace(re, url);
		} else
			result = normURL + "/" + url;
		
		// Remove additional url parameters (as in php variables)
		if (!(result == null) && result.contains("?"))
			result = result.replaceFirst("\\?.*", "");
		
		// Remove links to locations on a page
		if (!(result == null) && result.contains("#"))
			result = result.replaceFirst("#.*", "");
		
		// Remove links that are outside of the scope
		if (!(result == null) && !(result.contains(normURL)))
			result = null;
		
		// Remove links that have already been visited
		if (!(result == null) && visitedLink(result))
			result = null;
		return result;
	}

	public static String populateLinks(String curLine) {
		NaiveString urlMatching = new NaiveString("href=", curLine);
		String urlResult = urlMatching.matches();
		String urlfullStr = null;
		if (urlResult != null) {
			Matcher m = p.matcher(urlResult);
			if (m.matches()) {
				String urlStr = m.group(1);
				urlfullStr = CrHandler.normalizeUrl(urlStr);
				if (!(urlfullStr == null)) {
					if (robotsSafe(urlfullStr)) {
						pw.println(urlfullStr);
						addVisited(urlfullStr);
						return urlfullStr;
					}
				}
			}
		}
		return null;
	}
	
	public static void clearLinks() {
		robots.clear();
		visited.clear();
	}
}
