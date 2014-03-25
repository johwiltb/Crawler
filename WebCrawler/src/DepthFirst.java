/**
 * @author John Wiltberger
 *
 */

// Note on regex, the code for parsing out the URL from a string was found here: http://blog.houen.net/java-get-url-from-string/

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 DLS(node, goal, depth) {
  if ( depth >= 0 ) {
    if ( node == goal )
      return node

    for each child in expand(node)
      DLS(child, goal, depth-1)
  }
}
 */
public class DepthFirst {
	private ArrayList<String> links = new ArrayList<String>();
	private int depthLimit, currentDepth, stringMatch;
	private String qryString;
	//private String regex =  "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
	private String regex =  "^.*href=\\\"\\(.*\\)\\\"";

	private Pattern p = Pattern.compile("^.*href=\"(.*)\"");

	public DepthFirst(String url, int dLimit, int curDepth, int strMatch, String query) {
		this.depthLimit = dLimit;
		this.currentDepth = curDepth;
		this.stringMatch = strMatch;
		this.qryString = query;
		
		BufferedReader br = new BufferedReader(ConsoleCrawler.isr);
		String curLine = null;
		try {
			curLine = br.readLine();
		} catch (IOException e) {
			System.out.println("Cannot read from site!");
			System.exit(1);
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
					if (result != null) {
						Matcher m = p.matcher(result);
						String urlStr = m.group(1);
						links.add(urlStr);
						System.out.println(this.currentDepth + " - " + urlStr);
					}	
					// For matching the search query, deal with later
					//NaiveString nss = new NaiveString(this.qryString, curLine);
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
			System.out.println("The current depth is: " + (4 - this.currentDepth));
			for (int i = 0; i < links.size(); i++) {
				DepthFirst next = new DepthFirst(this.links.get(i), this.depthLimit, this.currentDepth - 1, this.stringMatch, this.qryString);
		}
		// recursively search the next line
		//for (int i = 0; i < url.length(); i++) {
		//	DepthFirst next = new DepthFirst(this.links.get(i), this.depthLimit, this.currentDepth - 1, this.stringMatch, this.qryString);
		}
	}

}
