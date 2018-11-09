import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

import java.lang.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class PrimaryGUI
{
	public static void main(String[] args)
	{
		System.out.println("Starting application...");
		
		new MainGUI();
		
		System.out.println("Done.");
	} // end main()
}//end PrimaryGUI class

//-----------------------------------------------------------------------------

class MainGUI extends JFrame implements ActionListener, ListSelectionListener/*,
																		ListSelectionListener,
																		MouseListener*/
{
	Toolkit tk;
	Dimension d;
	Container cp;
	
	JScrollPane scroller;
	JTable tableViewer;
	
	JPanel memberFieldPanel, memberButtonPanel, memberPanel, memberDataPanel, memberTopPanel, searchPanel;
	JButton search, clear, rentButton, sequalButton, userHistoryButton, adminGetLast24Button, adminGetTop10Button;
	JLabel eid, actor, title, genre, director, platform, searchLabel;
	JTextField idField, actorField, titleField, genreField, directorField, platformField;
	JTextField searchField;
	
	JCheckBox awardWinner;
	JComboBox<String> comboBox;
	JScrollPane sPane;
	JTable dataTable;
	
	JPanel loginPanel, userPanel, passPanel, adminPanel, fieldPanel, logButtonPanel;
	JButton login, register;
	JLabel userLabel, passLabel;
	JTextField userField;
	JPasswordField passField;
	
	User currentUser;
	//-----------------------------------------------------------------------------
	public MainGUI()
	{
		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		
		userPanel = new JPanel(new GridLayout(2, 1));
		passPanel = new JPanel(new GridLayout(2, 1));
		fieldPanel = new JPanel(new GridLayout(2, 1));
		loginPanel = new JPanel(new GridLayout(2,1));
		logButtonPanel = new JPanel();
				
		login = new JButton("Login");
		login.setActionCommand("LOGIN");
		login.addActionListener(this);
		rootPane.setDefaultButton(login);
		
		register = new JButton("Register");
		register.setActionCommand("REGISTER");
		register.addActionListener(this);
		
		userLabel = new JLabel("Username:");
		passLabel = new JLabel("Password:");
		
		userField = new JTextField();
		userField.setPreferredSize(new Dimension(120, 25));
		
		passField = new JPasswordField();
		passField.setPreferredSize(new Dimension(120, 25));
		
		userPanel.add(userLabel);
		userPanel.add(userField);
		passPanel.add(passLabel);
		passPanel.add(passField);
		fieldPanel.add(userPanel);
		fieldPanel.add(passPanel);
		logButtonPanel.add(login);
		logButtonPanel.add(register);
		loginPanel.add(fieldPanel);
		loginPanel.add(logButtonPanel);
		
		search = new JButton("Search");
		search.setActionCommand("SEARCH");
		search.addActionListener(this);
				
		clear = new JButton("Clear");
		clear.setActionCommand("CLEAR");
		clear.addActionListener(this);
		
		rentButton = new JButton("Rent");
		rentButton.setActionCommand("RENT");
		rentButton.addActionListener(this);
		
		sequalButton = new JButton("Get Sequals");
		sequalButton.setActionCommand("SEQUALS");
		sequalButton.addActionListener(this);
		sequalButton.setEnabled(false);
		
		userHistoryButton = new JButton("My History");
		userHistoryButton.setActionCommand("USER_HISTORY");
		userHistoryButton.addActionListener(this);
		
		adminGetLast24Button = new JButton("24 Hour Recap");
		adminGetLast24Button.setActionCommand("ADMIN_GET_24");
		adminGetLast24Button.addActionListener(this);
		
		adminGetTop10Button = new JButton("Top 10 Items");
		adminGetTop10Button.setActionCommand("ADMIN_GET_TOP_10");
		adminGetTop10Button.addActionListener(this);
		
		eid = new JLabel("ID Number:");
		actor = new JLabel("Actor/Actress:");
		title = new JLabel("Title:");
		genre = new JLabel("Genre:");
		director = new JLabel("Director:");
		platform = new JLabel("Platform:");
		
		idField = new JTextField();
		actorField = new JTextField();
		titleField = new JTextField();
		genreField = new JTextField();
		directorField = new JTextField();
		platformField = new JTextField();
		
		memberFieldPanel = new JPanel();
		searchPanel = new JPanel();
		memberTopPanel = new JPanel(new GridLayout(2, 1));
		memberButtonPanel = new JPanel(new GridLayout(1, 7));//new GridLayout(1,2));
		memberDataPanel = new JPanel(new BorderLayout());
		memberPanel = new JPanel(new BorderLayout());
		 
		searchLabel = new JLabel("Search: ");
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(200, 25));
		
		 
		String[] choices = { "Title","Actor", "Genre","Director","Platform"};
		
		comboBox = new JComboBox<String>(choices);
		 
		 
		dataTable = new JTable(new DefaultTableModel());
		 
		awardWinner = new JCheckBox("Award Winners Only");
		 
		sPane = new JScrollPane(dataTable);
		
		memberFieldPanel.setPreferredSize(new Dimension(500, 80));
		memberButtonPanel.setPreferredSize(new Dimension(500, 40));
		memberDataPanel.setPreferredSize(new Dimension(500, 200));
		
		
		//memberButtonPanel.add(search);
		memberButtonPanel.add(clear);
		memberButtonPanel.add(sequalButton);
		memberButtonPanel.add(rentButton);
		memberButtonPanel.add(userHistoryButton);
		memberButtonPanel.add(adminGetLast24Button);
		memberButtonPanel.add(adminGetTop10Button);
		memberFieldPanel.add(searchLabel);
		memberFieldPanel.add(searchField);
		memberFieldPanel.add(comboBox);
		memberFieldPanel.add(awardWinner);
		memberDataPanel.add(sPane, BorderLayout.CENTER);
		 
		memberTopPanel.add(memberFieldPanel);
		searchPanel.add(search);
		memberTopPanel.add(searchPanel);
		
		memberPanel.add(memberButtonPanel, BorderLayout.SOUTH);
		//memberPanel.add(memberDataPanel, BorderLayout.CENTER);
		 
		cp = getContentPane();
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.WEST);
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.EAST);
		cp.add(Box.createRigidArea(new Dimension(0,25)), BorderLayout.NORTH);
		cp.add(loginPanel, BorderLayout.CENTER);
		setupMainFrame();
		
	}//end constructor
	
	//-----------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("REGISTER"))
		{
			
			doRegister();
		}//end resgister conditional

		else if (e.getActionCommand().equals("LOGIN"))
		{
			String username = userField.getText().trim();
			String password = new String(passField.getPassword());
			boolean isSuccessful = attemptLogin(username, password);
			if (isSuccessful)
				doLogin();
			else{
				//TODO: Display some sort of error on login page
				System.out.println("Failed login.");
			}
				
		}//end logon conditional
		
		else if (e.getActionCommand().equals("SEARCH")){
			String searchTerm = searchField.getText().trim();
			String searchBy = comboBox.getSelectedItem().toString().toUpperCase();
			
			Entertainment entertainment = new Entertainment();
			DefaultTableModel tableModel = entertainment.searchBy(searchTerm, searchBy, null, awardWinner.isSelected());

			memberDataPanel.removeAll();
			dataTable = new JTable(tableModel);
			dataTable.getSelectionModel().addListSelectionListener(this);
			JScrollPane newPane = new JScrollPane(dataTable);
			newPane.setPreferredSize(new Dimension(500, 200));
			memberDataPanel.add(newPane, BorderLayout.CENTER);
			memberDataPanel.revalidate();
			memberDataPanel.repaint();
		}
		
		else if (e.getActionCommand().equals("SEQUALS")){
			String eid = dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString();
			System.out.println("Find sequals with this EID: " + eid);
			
			Entertainment entertainment = new Entertainment();
			DefaultTableModel tableModel = entertainment.findAllSequalsForEid(Integer.valueOf(eid));
			
			memberDataPanel.removeAll();
			dataTable = new JTable(tableModel);
			dataTable.getSelectionModel().addListSelectionListener(this);
			JScrollPane newPane = new JScrollPane(dataTable);
			newPane.setPreferredSize(new Dimension(500, 200));
			memberDataPanel.add(newPane, BorderLayout.CENTER);
			memberDataPanel.revalidate();
			memberDataPanel.repaint();
		}
		
		else if (e.getActionCommand().equals("USER_HISTORY")){
			DefaultTableModel tableModel = currentUser.getRentHistory();
			
			JTable rentHistoryTable = new JTable(tableModel);
			JScrollPane scrollPane = new JScrollPane(rentHistoryTable);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			
			JOptionPane.showMessageDialog(this, scrollPane, currentUser.getName() + "'s Rent History", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if (e.getActionCommand().equals("ADMIN_GET_24")){
			DefaultTableModel tableModel = currentUser.adminGetLast24Hours();
			
			JTable rentHistoryTable = new JTable(tableModel);
			JScrollPane scrollPane = new JScrollPane(rentHistoryTable);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			
			JOptionPane.showMessageDialog(this, scrollPane, "24-Hour Order History", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if (e.getActionCommand().equals("ADMIN_GET_TOP_10")){
			DefaultTableModel tableModel = currentUser.adminGetTop10LastMonth();
			
			JTable rentHistoryTable = new JTable(tableModel);
			JScrollPane scrollPane = new JScrollPane(rentHistoryTable);
			scrollPane.setPreferredSize(new Dimension(600, 200));
			
			JOptionPane.showMessageDialog(this, scrollPane, "Top 10 Items of the Month", JOptionPane.INFORMATION_MESSAGE);
		}
	}//end actionPerformed() method
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		System.out.println(dataTable.getValueAt(dataTable.getSelectedRow(), dataTable.getSelectedColumn()).toString());
		SwingUtilities.invokeLater( 
	        new Runnable() {
	            public void run() {
	                sequalButton.setEnabled(true);
	            }
	        }
	    );
	}
	
	//-----------------------------------------------------------------------------
	public void doLogin()
	{
		setVisible(false);
		setSize((d.width/3)+300, (d.height/3)+250);
		setLocationRelativeTo(null);
		cp.removeAll();
		//cp.add(Box.createRigidArea(new Dimension(30,0)), BorderLayout.WEST);
		//cp.add(Box.createRigidArea(new Dimension(30,0)), BorderLayout.EAST);
		cp.add(memberTopPanel, BorderLayout.NORTH);
		cp.add(memberDataPanel, BorderLayout.CENTER);
		rootPane.setDefaultButton(search);
		//cp.add(client.secondaryButtonPanel, BorderLayout.SOUTH);
		setTitle("Movies-R-Us");
		setVisible(true);
	}//end doLogin() method
	//-----------------------------------------------------------------------------
	public void doRegister()
	{
		new RegHub(this);
	}//end doRegister() method
	//-----------------------------------------------------------------------------
	public	void setupMainFrame()
	{
		setSize((d.width/3)-100, (d.height/3));
		setLocation(d.width/3, d.height/3);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("Movies-R-Us Login");

		setLocationRelativeTo(null);

		setVisible(true);
	}  // end setupMainFrame()
	
	private boolean attemptLogin(String email, String password) {
		currentUser = new User();
		try {
			currentUser.login(email, password);
			return true;
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}//end MainGUI class