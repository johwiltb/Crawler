/**
 * NaiveString.java
 * @author John Wiltberger
 * Naive String pattern matching class.  Class takes the full line and the query string
 * as inputs, and will determine if there is a match.  It will then return the full line
 * if the match exists.
 */

public class NaiveString {
	private String qText, fLine;			// qText is the query, fLine is the line to search
	
	/**
	 * Constructor for the class
	 * @param query	String that is being searched for
	 * @param fullLine Line that is being searched
	 */
	public NaiveString(String query, String fullLine) {
		this.setqText(query);
		this.setfLine(fullLine);
	}
	
	
	/**
	 * Matches the current string with the pattern and returns the result
	 * @return matching string (if matches) or null (if no match found)
	 */
	public String matches() {
		int qTextLen = this.getqText().length();
		int fLineLen = this.getfLine().length();
		for (int i = 0; i < (fLineLen - qTextLen); i++) {
			int j = 1;
			while(j < qTextLen && (this.getfLine().charAt(i+j) == this.getqText().charAt(j)))
				j++;
			if (j >= qTextLen)
				return this.getfLine();
		}
		return null;
	}

	/**
	 * @return the qText
	 */
	public String getqText() {
		return qText;
	}

	/**
	 * @param qText the qText to set
	 */
	public void setqText(String qText) {
		this.qText = qText;
	}

	/**
	 * @return the fLine
	 */
	public String getfLine() {
		return fLine;
	}

	/**
	 * @param fLine the fLine to set
	 */
	public void setfLine(String fLine) {
		this.fLine = fLine;
	}
}
