/**
 * ConsoleCrawler.java
 * @author John Wiltberger
 * Console Version Crawler for the web crawler.  Mostly used for testing purposes.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class ConsoleCrawler {
	private static final int MAX_DEPTH_LIMIT = 4;
	private static String urlText, queryText;
	private static int searchType, stringType;
	private static Scanner input = new Scanner(System.in);
	
	// Added and used in later classes
	private static URLConnection con = null;
	private static InputStream ins = null;
	protected static InputStreamReader isr = null;
	
	public static void main(String[] args) throws MalformedURLException {
		System.out.println("Welcome to the CLI version of webcrawler!\n");
		System.out.print("URL: ");
		urlText = input.nextLine();
		
		// add 'http://' if it contains no header
		if (!urlText.contains("http"))
			urlText = "http://" + urlText;
		
		System.out.print("queryText: ");
		queryText = input.nextLine();
		System.out.print("Choose your Search type\n(1): DLS\n(2): BFS\nChoice: ");
		searchType = input.nextInt();
		while(searchType < 1 || searchType > 2) {
			System.out.print("Invalid response. Please choose '1' or '2': ");
			searchType = input.nextInt();
		}
		System.out.print("Choose you String Matching Algorithm\n(1): Longest Common Sequence\n(2): Naive String Matching"
				+ "\n(3): Rabin-Karp\n(4): Finite Automata\n(5): KMP\nChoice: ");
		stringType = input.nextInt();
		while(stringType < 1 || stringType > 5) {
			System.out.print("Invalid response. Please choose '1-5': ");
			stringType = input.nextInt();
		}
		
		// Actually start the searching (add to a separate class later)
		try {
			URL urlSearch = new URL(getUrl());
			if (getUrl().matches("^https")) {
				con = (HttpsURLConnection)urlSearch.openConnection();
			} else {
				con = urlSearch.openConnection();
			}
			ins = con.getInputStream();
		} catch (MalformedURLException e) {
			System.out.println("Unable to connect to URL!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Unable to open https connection");
			e.printStackTrace();
		}
		
	    isr = new InputStreamReader(ins);
		switch(searchType) {
			case 1:
				//DLS dls = new DLS(urlText, queryText, (stringType-1));
				//dls.setSearching(true);
				//dls.search();
				DepthFirst dls = new DepthFirst(getUrl(), MAX_DEPTH_LIMIT, MAX_DEPTH_LIMIT, getStringType() - 1, queryText);
				break;
			case 2:
				BFS bfs = new BFS(urlText, queryText, (stringType-1));
				bfs.search();
				break;
			default:
				break;
		}
		System.out.println("\nThank you for using this!");
	}

	private static int getStringType() {
		return stringType;
	}

	private static String getUrl() {
		return urlText;
	}

}
