/**
 * ConsoleCrawler.java
 * @author John Wiltberger
 * Console Version Crawler for the web crawler.  Mostly used for testing purposes.
 */

import java.net.MalformedURLException;
import java.util.Scanner;

public class ConsoleCrawler {
	private static final int MAX_DEPTH_LIMIT = 4;
	protected static String urlText;
	private static String queryText;
	private static int searchType, stringType;
	private static Scanner input = new Scanner(System.in);	
		

	public static void main(String[] args) throws MalformedURLException {

		System.out.println("Welcome to the CLI version of EpiCrawl!\n");
		System.out.print("URL: ");
		urlText = input.nextLine();
		CrHandler handle = new CrHandler("console");
		handle.normURL(urlText);
		urlText = handle.getURL();
		
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
				System.out.println("Results will be printed below:\n");
				DepthFirst dls = new DepthFirst(getUrl(), MAX_DEPTH_LIMIT, MAX_DEPTH_LIMIT, getStringType() - 1, queryText);
				break;
			case 2:
				BFS bfs = new BFS(urlText, queryText, (stringType-1));
				bfs.search();
				break;
			default:
				break;
		}
		handle.pw.close();
		System.out.println("\nThank you for using this!");
	}

	private static int getStringType() {
		return stringType;
	}

	private static String getUrl() {
		return urlText;
	}

}
