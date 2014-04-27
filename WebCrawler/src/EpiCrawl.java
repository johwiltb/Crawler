import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

/**
 * EpiCrawl.java
 * @author John Wiltberger
 * @date 25 Feb 2014
 * Class sets up User's GUI Menu to allow the user to select individual searching algorithms,
 * as well as string matching algorithms.
 */

public class EpiCrawl {
	private int algSearch, algString;		// Holds choices for search algorithm and string matching algorithm
	private String searchURL, searchText;	// Holds the user's URL choice and query text
    //protected static boolean stopPressed = false; 	// Stops the searching
    private int modifier;					// Determine either max pages or max depth, depending on value
    private String strMod;
	
	// Initiate options for searches and string matching
    private String[] searchOpts = {"Breadth First Search (BFS)", "Depth Limited Search (DLS)"};
    private String[] stringMatches = {"Longest Common Sequence", "Naive String Matching Algorithm", "Rabin-Karp Algorithm", "Finite Automata Algorithm", "KMP Algorithm"};
    
	
	// Set both the main frame and the skeleton panels
    private JFrame mainFrame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JPanel searchParamsPanel = new JPanel();
    private JPanel combSearchesPanel = new JPanel();
    private JPanel xtrSearchParamsPanel = new JPanel();
    private JPanel searchTypePanel = new JPanel();
    private JPanel strMatchPanel = new JPanel();
    private JPanel urlPanel = new JPanel();
    private JPanel qryPanel = new JPanel();
    private JPanel btnPanel = new JPanel();
    
    // Used to print out results
    protected static JTextArea resultsTxtArea = new JTextArea();
    protected static JScrollPane resultScrollBx = new JScrollPane(resultsTxtArea);
    private JPanel resultPanel = new JPanel();
    protected static JFrame resultFrame = new JFrame();

    /**
     * Constructor for the EpiCrawl class
     */
    public EpiCrawl() {
    	// Sets up the results panel for displaying links
		resultScrollBx.setPreferredSize(new Dimension(480,300));
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.add(resultScrollBx);
		resultFrame.add(resultPanel);
		resultFrame.pack();
		resultFrame.setLocationRelativeTo(null);
    }
    	
    /**
     * Builds and displays the main user interface for the class
     */
    public void buildMenu() {
    	// Set up a loading frame
    	final JFrame loadFrame = new JFrame();
    	JPanel loadPanel = new JPanel();
    	JTextPane loadText = new JTextPane();
    	JButton loadBtn = new JButton("Ok");
    	loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));
    	loadText.setEditable(false);
    	loadText.setBorder(new EmptyBorder(3,3,3,3));
    	loadText.setText(CrHandler.loadInfo + "\n\nYou must press 'Ok' to continue...");
    	loadBtn.setEnabled(false);
    	loadPanel.add(loadText);
    	loadPanel.add(loadBtn);
    	loadFrame.add(loadPanel);
    	loadFrame.getRootPane().setDefaultButton(loadBtn);
    	loadFrame.pack();
    	loadFrame.setLocationRelativeTo(null);
    	loadFrame.setVisible(true);
    	
    	// Build Main Menu
    	resultsTxtArea.setEnabled(true);
    	resultsTxtArea.setEditable(false);
    	
    	// Set default behaviors of the panels and frames
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainFrame.setTitle("EpiCrawl");
    	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    	searchParamsPanel.setLayout(new BoxLayout(searchParamsPanel, BoxLayout.X_AXIS));
    	combSearchesPanel.setLayout(new BoxLayout(combSearchesPanel, BoxLayout.PAGE_AXIS));
    	xtrSearchParamsPanel.setLayout(new BoxLayout(xtrSearchParamsPanel, BoxLayout.X_AXIS));
    	
    	// Set up search panel
    	JLabel searchTypeLabel = new JLabel("Search Type:");
    	final JComboBox<String> searchTypeCmbBx = new JComboBox<String>(searchOpts);
    	searchTypePanel.add(searchTypeLabel);
    	searchTypePanel.add(searchTypeCmbBx);
    	
    	// Set up string matching panel
    	JLabel strMatchLabel = new JLabel("String Matching Type:");
    	final JComboBox<String> strMatchCmbBx = new JComboBox<String>(stringMatches);
    	strMatchPanel.add(strMatchLabel);
    	strMatchPanel.add(strMatchCmbBx);
    	
    	// Set up extra parameters panel
    	final JLabel DfxtraSrchParamsLbl = new JLabel("<html>Max Depth<br>to Search</html>");
    	final JLabel BfxtraSrchParamsLbl = new JLabel("<html>Max Number<br>of Pages</html>");
    	JLabel blankLabel = new JLabel(" ");
    	final JTextField xtraSrchParamsTxtFld = new JTextField(10);
    	JPanel xtraSrchParamsTxtPanel = new JPanel();
    	xtraSrchParamsTxtPanel.setLayout(new BoxLayout(xtraSrchParamsTxtPanel, BoxLayout.PAGE_AXIS));
    	
    	DfxtraSrchParamsLbl.setVisible(false);
    	
    	// Displays the different types of extra search paramaters per search choice
    	searchTypeCmbBx.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent event) {
    			DfxtraSrchParamsLbl.setVisible(!(DfxtraSrchParamsLbl.isVisible()));
    			BfxtraSrchParamsLbl.setVisible(!(BfxtraSrchParamsLbl.isVisible()));
    		}
    	});
    	xtraSrchParamsTxtPanel.add(blankLabel);
    	xtraSrchParamsTxtPanel.add(xtraSrchParamsTxtFld);
    	
    	xtrSearchParamsPanel.add(DfxtraSrchParamsLbl);
    	xtrSearchParamsPanel.add(BfxtraSrchParamsLbl);
    	xtrSearchParamsPanel.add(xtraSrchParamsTxtPanel);
    	
    	
    	// Set up search paramaters panel
    	combSearchesPanel.add(searchTypePanel);
    	combSearchesPanel.add(strMatchPanel);
    	
    	// Set the whole thing up for searching
    	searchParamsPanel.add(combSearchesPanel);
    	searchParamsPanel.add(xtrSearchParamsPanel);
    	
    	// Set up the URL panel
    	JLabel urlLabel = new JLabel("URL:");
    	final JTextField urlTxtFld = new JTextField(40);
    	urlPanel.add(urlLabel);
    	urlPanel.add(urlTxtFld);
    	
    	// Set up the query panel
    	JLabel qryLable = new JLabel("Query:");
    	final JTextField qryTxtFld = new JTextField(39);
    	qryPanel.add(qryLable);
    	qryPanel.add(qryTxtFld);
    	
    	// Create Buttons for search
    	JButton goBtn = new JButton("Go");
    	goBtn.setEnabled(true);    	
        
    	// goBtn listener that sets the private variables once the user chooses them
        goBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		algSearch = searchTypeCmbBx.getSelectedIndex();
        		algString = strMatchCmbBx.getSelectedIndex();
        		searchURL = urlTxtFld.getText();
        		searchText = qryTxtFld.getText();
        		strMod = xtraSrchParamsTxtFld.getText();
        		pressGo();
        	}
        });
        
        btnPanel.add(goBtn);
        
    	// Add all the separate panels to the main panel
    	mainPanel.add(searchParamsPanel);
    	mainPanel.add(urlPanel);
    	mainPanel.add(qryPanel);
    	mainPanel.add(btnPanel);
    	mainPanel.add(resultPanel);
    	
    	// Add all the panels to the GUI and make it visible
    	mainFrame.add(mainPanel, BorderLayout.CENTER);
    	mainFrame.getRootPane().setDefaultButton(goBtn);
    	mainFrame.pack();
    	mainFrame.setLocationRelativeTo(null);
    	loadBtn.setEnabled(true);
    	loadBtn.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    	    	mainFrame.setVisible(true);
      	    	loadFrame.dispose();
    		}
    	});
    }
    
    /**
     * Handles the procedures once 'Go' has been pressed
     */
    public void pressGo() {
    	// Verify integer in extra string parameters
    	try {
			modifier = Integer.parseInt(strMod);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Only integers in the 'Max Search' field!");
			return;
		}
    	
    	// Verify that the search fields are not empty
    	if (searchURL.isEmpty() || searchText.isEmpty()) {
    		JOptionPane.showMessageDialog(null, "Please fill out all query boxes!");
    		return;
    	}
    	
    	// Initiate the text in the results panel
    	resultsTxtArea.setText("Results displayed below:\n");
    	CrHandler handle = new CrHandler("gui");
    	handle.normURL(searchURL);
    	searchURL = handle.getURL();
    	
    	// Sets up the search algorithm
    	if (this.algSearch == 0) {
        	BFS search = new BFS(this.searchURL, this.searchText, this.algString);
        	search.search();
        } else if (this.algSearch == 1) {
        	new DepthFirst(this.searchURL, this.modifier, this.modifier, this.algString, this.searchText);
        	CrHandler.pw.close();
        	resultsTxtArea.append("All Finished!");
        } else {
        	resultsTxtArea.append("There was an issue in the choice of Search Algorithms");
        	System.exit(1);
        }
	}

	public static void main(String[] args) {
		EpiCrawl crawl = new EpiCrawl();
		crawl.buildMenu();
	}

}
