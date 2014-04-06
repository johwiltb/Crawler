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
	protected static String normURL;
	protected static PrintWriter pw;
	private static String iface;
	private static ArrayList<String> robots = new ArrayList<String>();

	
	public CrHandler(String interf) { 
		iface = interf;
	}
	
	public void normURL (String userURL) {
		// add 'http://(www)' if it contains no header
		if (!(userURL.contains("http"))) {
			if (!(userURL.contains("www"))) 
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
	
	public String getURL() {
		return normURL;
	}
	
	public static void printOut(String output) {
		if (iface == "console")
		{
			System.out.println(output);
		} else if (iface == "gui") {
			EpiCrawl.resultsTxtArea.append(output);
		} else {
			System.out.println("Failure of output.  There is an error in your choices");
			System.exit(1);
		}
	}
	
	public void buildRobots() {
		URLConnection rcon;
		InputStream rins = null;
		try {
			URL robotSearch = new URL(CrHandler.normURL + "/robots.txt");
			if (CrHandler.normURL.matches("^https.*")) {
				rcon = (HttpsURLConnection)robotSearch.openConnection();
			} else {
				rcon = robotSearch.openConnection();
			}
			rins = rcon.getInputStream();
		} catch (MalformedURLException e) {
			//System.out.println("Unable to connect to robots.txt!");
			CrHandler.printOut("Unable to connect to robots.txt!");
			rcon = null;
		} catch (FileNotFoundException e) {
			rcon = null;
		} catch (IOException e) {
			//System.out.println("Unable to open robots.txt");
			CrHandler.printOut("Unable to open robots.txt");
			rcon = null;
		}
		if (!(rcon == null)) {
		    InputStreamReader risr = new InputStreamReader(rins);
			BufferedReader rbr = new BufferedReader(risr);
			String curRobotLine = null;
			try {
				curRobotLine = rbr.readLine();
			} catch (IOException e1) {
				//System.out.println("Can't read robots.txt");
				CrHandler.printOut("Can't read robots.txt");
			}
			if (curRobotLine == null) { 
				try {
					curRobotLine = rbr.readLine();
				} catch (IOException e) {
					//System.out.println("Cannot read from site!");
					CrHandler.printOut("Cannot read robots.txt from site!");
				}
			}
			while (!(curRobotLine == null)) {
				if (curRobotLine.matches("^[ \\t]*User-Agent: \\*\\.*")) {
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
							//System.out.println("Lost connection to robots.txt");
							CrHandler.printOut("Lost connection to robots.txt");
							curRobotLine = null;
						}
					}
				}
				try {
					curRobotLine = rbr.readLine();
				} catch (IOException e) {
					//System.out.println("Connection was lost!");
					CrHandler.printOut("Connection was lost!");
				}
			}
		}
	}
}
