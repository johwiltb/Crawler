import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author John Wiltberger
 * @date 25 Feb 2014
 * Class sets up User's GUI Menu to allow the user to select individual searching algorithms,
 * as well as string matching algorithms.
 */

public class EpiCrawl {
	private int algSearch, algString;		// Holds choices for search algorithm and string matching algorithm
	private String searchURL, searchText;	// Holds the user's URL choice and query text
    private boolean goPressed = false;		// Determines whether the button has been pressed (probably get rid of later)
    protected static boolean stopPressed = false; 	// Stops the searching
    private final int MAX_DEPTH_SEARCH = 4;
	
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

    public EpiCrawl() {
    	
    	// Set default behaviors of the panels and frames
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	mainFrame.setTitle("EpiCrawl");
    	mainFrame.setLocationRelativeTo(null);
    	
    	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    	searchParamsPanel.setLayout(new BoxLayout(searchParamsPanel, BoxLayout.X_AXIS));
    	combSearchesPanel.setLayout(new BoxLayout(combSearchesPanel, BoxLayout.PAGE_AXIS));
    	xtrSearchParamsPanel.setLayout(new BoxLayout(xtrSearchParamsPanel, BoxLayout.X_AXIS));
    	
    	// Set up search panel
    	JLabel searchTypeLabel = new JLabel("Search Type:");
    	JComboBox<String> searchTypeCmbBx = new JComboBox<String>(searchOpts);
    	searchTypePanel.add(searchTypeLabel);
    	searchTypePanel.add(searchTypeCmbBx);
    	
    	// Set up string matching panel
    	JLabel strMatchLabel = new JLabel("String Matching Type:");
    	JComboBox<String> strMatchCmbBx = new JComboBox<String>(stringMatches);
    	strMatchPanel.add(strMatchLabel);
    	strMatchPanel.add(strMatchCmbBx);
    	
    	// Set up extra parameters panel
    	final JLabel DfxtraSrchParamsLbl = new JLabel("<html>Max Depth<br>to Search</html>");
    	final JLabel BfxtraSrchParamsLbl = new JLabel("<html>Max Number<br>of Pages</html>");
    	JLabel blankLabel = new JLabel(" ");
    	JTextField xtraSrchParamsTxtFld = new JTextField(10);
    	JPanel xtraSrchParamsTxtPanel = new JPanel();
    	xtraSrchParamsTxtPanel.setLayout(new BoxLayout(xtraSrchParamsTxtPanel, BoxLayout.PAGE_AXIS));
    	
    	DfxtraSrchParamsLbl.setVisible(false);
    	
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
    	JTextField urlTxtFld = new JTextField(40);
    	urlPanel.add(urlLabel);
    	urlPanel.add(urlTxtFld);
    	
    	// Set up the query panel
    	JLabel qryLable = new JLabel("Query:");
    	JTextField qryTxtFld = new JTextField(39);
    	qryPanel.add(qryLable);
    	qryPanel.add(qryTxtFld);
    	
    	// Create Buttons for search
    	JButton goBtn = new JButton("Go");
    	goBtn.setEnabled(true);
    	
    	// Add all the separate panels to the main panel
    	mainPanel.add(searchParamsPanel);
    	mainPanel.add(urlPanel);
    	mainPanel.add(qryPanel);
    	mainPanel.add(btnPanel);
    	
    }
    
    
    public void start() {
    	// Add all the panels to the GUI and make it visible
    	mainFrame.add(mainPanel, BorderLayout.CENTER);
    	// uframe.getRootPane().setDefaultButton(goBtn);
    	mainFrame.pack();
    	mainFrame.setVisible(true);
    	
    	/*
    	 * try {
                     int1 = Integer.parseInt(JTextField.getText());   //This was a string coming from a resultset that I changed into and Int
                     JTextField.requestFocusInWindow();
                     } catch (Exception z) { 
                         JOptionPane.showMessageDialog(this, "Incorrect Data Type! Numbers Only!",
                            "Inane error", JOptionPane.ERROR_MESSAGE);
                         JTextField.setText("");
                         JTextField.requestFocusInWindow();
                         return;
                }
    	 */
    }


	public static void main(String[] args) {
		EpiCrawl crawl = new EpiCrawl();
		crawl.start();
	}

}
