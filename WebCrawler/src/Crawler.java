/**
 * @author John Wiltberger 
 * @date 26 February
 * @description Application is used to query a URL for a
 * search term. Input URL into the URL field, and a query 
 * into the query field, and the application will crawl the 
 * URL for the search term utilizing the specifications
 * chose in the menu.
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
public class Crawler {

	/**
	 * Build the User Menu
	 * @param args <None should be entered>
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			System.out.println("No arguments needed.  Simply run 'java Crawler'!");
			System.exit(0);
		}
		// Create the user menu that you will interact with
		UserMenu menu = new UserMenu();
		menu.start();
	}

}
