/**
 * JohnsMatching.java
 * @author John Wiltberger
 * Pointless matching class that is for testing purposes only.  I 
 * really hope I remember to remove this later
 */
public class JohnsMatching {
	private String qText, fLine;
	
	public JohnsMatching(String query, String fullLine) {
		this.qText = query;
		this.fLine = fullLine;
		match();
		
	}

	private void match() {
		//if (this.fLine.matches("<a\b([^>]+)>(.+?)</a>"))
		if (fLine.matches("^.*<[a,LINK].*href=.*")){
			System.out.println(this.fLine);
		}
		//if (fLine.matches("^.*src="))
	}

}
