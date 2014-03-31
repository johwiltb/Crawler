/**
 * @author John Wiltberger
 * @date 25 Feb 2014
 * Class sets up User's GUI Menu to allow the user to select individual searching algorithms,
 * as well as string matching algorithms.
 */
// This info is taken from
// http://java.about.com/od/creatinguserinterfaces/ss/Coding-A-Simple-Graphical-User-Interface-Part-I_3.htm
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class UserMenu {
	private int algSearch, algString;		// Holds choices for search algorithm and string matching algorithm
	private String searchURL, searchText;	// Holds the user's URL choice and query text
    private boolean goPressed = false;		// Determines whether the button has been pressed (probably get rid of later)
    protected static boolean stopPressed = false; 	// Stops the searching
    private final int MAX_DEPTH_SEARCH = 4;
	
	// Initiate options for searches and string matching
    private String[] searchOpts = {"Breadth First Search (BFS)", "Depth Limited Search (DLS)"};
    private String[] stringMatches = {"Longest Common Sequence", "Naive String Matching Algorithm", "Rabin-Karp Algorithm", "Finite Automata Algorithm", "KMP Algorithm"};

    public UserMenu() { }

    /**
     * Create the Menu and handle the search information
     */
    public void start() {
    	// Set up frame and main panel, as well as
    	// default actions for frame
    	JFrame uframe = new JFrame();
        JPanel umenu = new JPanel();
        uframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        umenu.setLayout(new BoxLayout(umenu, BoxLayout.PAGE_AXIS));
        uframe.setTitle("Web Crawler");
        uframe.setLocationRelativeTo(null);
        
        // Build individual search and string panels
        final JPanel searchPanel, stringPanel, urlPanel, textPanel;
        searchPanel = new JPanel();
        stringPanel = new JPanel();
        JLabel searchLbl = new JLabel("Searches:");
        final JComboBox<String> searches = new JComboBox<String>(searchOpts);
        JLabel stringLbl = new JLabel("String Matching:");
        final JComboBox<String> stringMatch = new JComboBox<String>(stringMatches);
        
        // compile labels and options for search and string options
        searchPanel.add(searchLbl);
        searchPanel.add(searches);
        stringPanel.add(stringLbl);
        stringPanel.add(stringMatch);
        
        // Build individual panels for the target URL
        // and query text
        urlPanel = new JPanel();
        textPanel = new JPanel();
        JLabel urlLbl = new JLabel("URL:");
        final JTextField queryURL = new JTextField(40);
        JLabel textLbl = new JLabel("Query:");
        final JTextField queryText = new JTextField(40);
        
        // Compile labels and text fields for URL and Query
        urlPanel.add(urlLbl);
        urlPanel.add(queryURL);
        textPanel.add(textLbl);
        textPanel.add(queryText);
        
        // Add query button for search and stop
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        // Stop Button
        final JButton stopBtn = new JButton("Stop");
        stopBtn.setEnabled(true);
        stopBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		while (getGoPressed()) {
        			System.exit(0);
        		}
        	}
        });
        
     // Go Button
        final JButton goBtn = new JButton("Go!");
        goBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		setAlgSearch(searches.getSelectedIndex());
        		setAlgString(stringMatch.getSelectedIndex());
        		setSearchURL(queryURL.getText());
        		setSearchText(queryText.getText());
        		pressGo();
        	}
        });
        
        // Add Go! and Stop to buttonPannel
        buttonPanel.add(goBtn);
        buttonPanel.add(stopBtn);

        // Add all objects to main menu panel
        umenu.add(searchPanel);
        umenu.add(stringPanel);
        umenu.add(urlPanel);
        umenu.add(textPanel);
        umenu.add(buttonPanel);
        
        // Add main menu to frame and make visible
        uframe.add(umenu, BorderLayout.CENTER);
        uframe.getRootPane().setDefaultButton(goBtn);
        uframe.pack();
        uframe.setVisible(true);
    }
    
    /**
     * Sets the algorithm for the search
     * @param selection Selection for the search type algorithm
     */
    public void setAlgSearch(int selection) {
    	algSearch = selection;
    }
    
    /**
     * Sets the algorithm for the string matching
     * @param selection Selection for the string matching algorithm
     */
    public void setAlgString(int selection) {
    	algString = selection;    	
    }
    
    /**
     * Sets the target URL
     * @param selection URL that is to be searched
     */
    public void setSearchURL(String selection) {
    	// add 'http://(www)' if it contains no header
		if (!(selection.contains("http"))) {
			if (!(selection.contains("www"))) 
				selection = "http://www." + selection;
			else 
				selection = "http://" + selection;
		}
    	searchURL = selection;
    }
    
    /**
     * Sets the text to be queried
     * @param selection Text to be queried for
     */
    public void setSearchText(String selection) {
    	searchText = selection;
    }
    
    /**
     * Gets the integer value for the search algorithm
     * @return Integer for search algorithm
     */
    public int getAlgSearch() {
    	return algSearch;
    }
    
    /**
     * Gets the integer value for the string matching algorithm
     * @return Integer for string matching algorithm
     */
    public int getAlgString() {
    	return algString;
    }
    
    /**
     * Gets the string for the target URL
     * @return Target URL string
     */
    public String getSearchURL() {
    	return searchURL;
    }
    
    /**
     * Gets the string for the search query
     * @return Text query string
     */
    public String getSearchText() {
    	return searchText;
    }

    /**
     * Monitors when the search is initiated
     */
    public void pressGo() {
        goPressed = true;
        // The following println's are for testing of the GUI outputs
        System.out.println("Alg. Search = " + this.getAlgSearch());
        System.out.println("Str. Search = " + this.getAlgString());
        System.out.println("Query text  = " + this.getSearchText());
        System.out.println("URL = " + this.getSearchURL());
        
        // Fill the searches and start the process of searching
        if (this.getAlgSearch() == 0) {
        	BFS search = new BFS(this.getSearchURL(), this.getSearchText(), this.getAlgString());
        	search.search();
        } else if (this.getAlgSearch() == 1) {
        	DepthFirst search = new DepthFirst(this.getSearchURL(), MAX_DEPTH_SEARCH, MAX_DEPTH_SEARCH, this.getAlgString(), this.getSearchText());
        } else {
        	System.out.println("There was an issue in the choice of Search Algorithms");
        	System.exit(1);
        }
    }

    /**
     * Returns whether the search has been initiated
     * @return Value of whether search is started or not
     */
    public boolean getGoPressed() {
        return goPressed;
    }
}
