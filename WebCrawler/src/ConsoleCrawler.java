/**
 * ConsoleCrawler.java
 * @author John Wiltberger
 * Console Version Crawler for the web crawler.  Mostly used for testing purposes.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class ConsoleCrawler {
	private static final int MAX_DEPTH_LIMIT = 4;
	protected static String urlText;
	private static String queryText;
	private static int searchType, stringType;
	private static Scanner input = new Scanner(System.in);	
		
	protected static PrintWriter pw;

	public static void main(String[] args) throws MalformedURLException {
		// Print links to file
		try {
			pw = new PrintWriter("./links", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Welcome to the CLI version of webcrawler!\n");
		System.out.print("URL: ");
		urlText = input.nextLine();
		
		// add 'http://(www)' if it contains no header
		if (!(urlText.contains("http"))) {
			if (!(urlText.contains("www"))) 
				urlText = "http://www." + urlText;
			else 
				urlText = "http://" + urlText;
		}
		
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
		
		switch(searchType) {
			case 1:
				DepthFirst dls = new DepthFirst(getUrl(), MAX_DEPTH_LIMIT, MAX_DEPTH_LIMIT, getStringType() - 1, queryText);
				break;
			case 2:
				BFS bfs = new BFS(urlText, queryText, (stringType-1));
				bfs.search();
				break;
			default:
				break;
		}
		pw.close();
		System.out.println("\nThank you for using this!");
	}

	private static int getStringType() {
		return stringType;
	}

	private static String getUrl() {
		return urlText;
	}

}
