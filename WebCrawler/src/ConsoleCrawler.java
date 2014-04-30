/**
 * ConsoleCrawler.java
 * @author John Wiltberger 
 * @date 26 February
 * @description Application is used to query a URL for a
 * search term. Input URL into the URL field, and a query 
 * into the query field, and the application will crawl the 
 * URL for the search term utilizing the specifications
 * chose in the menu.
 * 
 * *****THIS IS THE CONSOLE VERSION.  FOR A GUI, OPEN EPICRAWLER.JAVA******
 *
 * Additional Information----
 * Side Ideas
 *  - Hash table for normalized URLs to avoid duplicates
 *  - Read on Googlebot
 *  - Possible Max Link Depth for saving from infinite loops
 *  - Scrapy may be good resource
 *  - http://www.michaelnielsen.org/ddi/how-to-crawl-a-quarter-billion-webpages-in-40-hours/
 *  - http://andreas-hess.info/programming/webcrawler/
 *  - http://www0.cs.ucl.ac.uk/staff/ucacdxq/others/crawling.pdf
 *  - http://irl.cs.tamu.edu/people/hsin-tsang/thesis.pdf
 *  - Most likely store pages in Collection
 *
 * Crawler Policy
 *  - Selection (which pages to download)
 *      - Restrict follow links by MIME types
 *      - Possibly avoid URLs with '?' 
 *      - Search only files with .htm(l), .asp(x), .php, .jsp(x), or '/'
 *      - URL normalization
 *  - Re-visit (when to check for updates)
 *  - Politeness (how to avoid overloading websites)
 *      - obeying robots.txt
 *      - Potentially ignore page w/  <meta name="Googlebot" content="nofollow" /> 
 *      - "Crawl-delay"
 *  - Parallelization (how to coordinate distributed web crawlers) **May not apply
 */
import java.net.MalformedURLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleCrawler {
	private static int optionalNum;		// Parameter for either depth or max pages to search
	protected static String urlText;	// Domain to search
	private static String queryText;	// String to search for
	private static int searchType, stringType;		// Determines both the searching and string matching algorithms
	private static Scanner input = new Scanner(System.in);	// Read user input
		
	
	public static void main(String[] args) throws MalformedURLException {
		// Begin console printing
		System.out.println();
		System.out.println(CrHandler.loadInfo + "\n\n");		// Print warning to user
		System.out.println("Welcome to the CLI version of EpiCrawl!\n");
		System.out.print("URL: ");
		urlText = input.nextLine();		// Input for url search
		CrHandler handle = new CrHandler("console");	// Begins handler with the console settings
		handle.normURL(urlText);
		urlText = handle.getURL();
		
		// Populates variables and checks for valid input
		try {
			System.out.print("queryText: ");
			queryText = input.nextLine();
			System.out.print("Choose your Search type\n(1): DLS\n(2): BFS\nChoice: ");
			searchType = input.nextInt();
			while(searchType < 1 || searchType > 2) {
				System.out.print("Invalid response. Please choose '1' or '2': ");
				searchType = input.nextInt();
			}
			if (searchType == 1) {
				System.out.print("How many levels would you like to search? ");
				optionalNum = input.nextInt();
				if (optionalNum < 1) {
					System.out.println("You entered a value too small.  Setting to 4.");
					optionalNum = 4;
				}
			} else {
				System.out.print("How many pages would you like to search? ");
				optionalNum = input.nextInt();
				if (optionalNum < 1) {
					System.out.println("You entered a value too small.  Setting to 200.");
					optionalNum = 200;
				}
			}
			System.out.print("Choose you String Matching Algorithm\n(1): Longest Common Sequence\n(2): Naive String Matching"
					+ "\n(3): Rabin-Karp\n(4): Finite Automata\n(5): KMP\nChoice: ");
			stringType = input.nextInt();
			while(stringType < 1 || stringType > 5) {
				System.out.print("Invalid response. Please choose '1-5': ");
				stringType = input.nextInt();
			}
		} catch (InputMismatchException e) {
			System.out.println("\n****You inputted an illegal character!  Try \nrunning the program again with appropriate parameters!");
			System.exit(1);
		}
		
		// Begins search based off of search choice
		switch(searchType) {
			case 1:
				System.out.println("Results will be printed below:\n");
				new DepthFirst(getUrl(), optionalNum, optionalNum, getStringType() - 1, queryText);
				break;
			case 2:
				new BFS(getUrl(), optionalNum, queryText, (stringType-1));
				break;
			default:
				break;
		}
		CrHandler.pw.close();
		System.out.println("\nThank you for using this!");
	}

	/**
	 * Get the string matching algorithm type
	 * @return integer matching string type
	 */
	private static int getStringType() {
		return stringType;
	}

	/**
	 * Get the domain to be searched
	 * @return string version of the domain to be searched
	 */
	private static String getUrl() {
		return urlText;
	}
}
