import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

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

class MainGUI extends JFrame implements ActionListener, ListSelectionListener, MouseListener, WindowListener
{
	Toolkit tk;
	Dimension d;
	Container cp;
	
	JScrollPane scroller;
	JTable tableViewer;
	
	JPanel memberFieldPanel, memberPanel, memberDataPanel, movieDataPanel, memberTopPanel, searchPanel, radButtonPanel, cBoxPanel;
	JButton search, clear, rentButton, sequalButton, userHistoryButton, adminGetLast24Button, adminGetTop10Button, removeButton, updateButton, logoutButton;
	JLabel searchLabel;
	JTextField searchField;
	
	JCheckBox awardWinner, newToMe;
	JRadioButton gameRad, movRad, bothRad;
	ButtonGroup radGroup;
	
	JComboBox<String> comboBox;
	JScrollPane sPane;
	JTable dataTable;
	
	JPanel loginPanel, userPanel, passPanel, adminPanel, fieldPanel, logButtonPanel, srButtonPanel, adminSwitchPanel, adminSouthPanel;
	JButton login, register;
	JLabel userLabel, passLabel, adminOn, adminOff;
	JTextField userField;
	JPasswordField passField;
	
	JLabel castLabel, directorLabel, awardsLabel, sequalLabel;
	
	Switch adminModeSwitch;
	JMenu adminMenu;
	boolean adminStatus;
	User currentUser;
	RegHub mainRegHub;
	String username, password;
	//-----------------------------------------------------------------------------
	public MainGUI()
	{
		adminMenu = new JMenu("Admin");
		
		String[] choices = { "Title","Actor", "Genre","Director","Platform"};
		
		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		
		adminMenu.setMnemonic(KeyEvent.VK_A);
		adminMenu.setToolTipText("Admin. operations");
		adminMenu.add(newItem("Edit User", "EUSER", this, KeyEvent.VK_U, KeyEvent.VK_U, "Access and edit a users information."));
		adminMenu.add(newItem("Add Invetory", "INVENTORY", this, KeyEvent.VK_I, KeyEvent.VK_I, "Add a new movie or game to the inventory."));
		adminMenu.add(newItem("24 Hour Recap", "ADMIN_GET_24", this, KeyEvent.VK_H, KeyEvent.VK_H, "See movies rented in the last 24 hours."));
		adminMenu.add(newItem("Top 10", "ADMIN_GET_TOP_10", this, KeyEvent.VK_T, KeyEvent.VK_T, "See to 10 rented items."));
		
		adminSouthPanel = new JPanel();
		memberTopPanel = new JPanel(new GridLayout(4, 1));
		memberDataPanel = new JPanel(new GridLayout(2, 1));
		//memberPanel = new JPanel(new BorderLayout());
		memberFieldPanel = new JPanel();
		srButtonPanel = new JPanel();
		searchPanel = new JPanel();
		userPanel = new JPanel(new GridLayout(2, 1));
		passPanel = new JPanel(new GridLayout(2, 1));
		fieldPanel = new JPanel(new GridLayout(2, 1));
		loginPanel = new JPanel(new GridLayout(2,1));
		adminSwitchPanel = new JPanel();
		logButtonPanel = new JPanel();
		radButtonPanel = new JPanel();
		cBoxPanel = new JPanel();
		movieDataPanel = new JPanel(new GridLayout(4, 1));
		
		radGroup = new ButtonGroup();
				
		login = new JButton("Login");
		login.setActionCommand("LOGIN");
		login.addActionListener(this);
		rootPane.setDefaultButton(login);
		
		register = new JButton("Register");
		register.setActionCommand("REGISTER");
		register.addActionListener(this);
		
		logoutButton = new JButton("Logout");
		logoutButton.setActionCommand("LOGOUT");
		logoutButton.addActionListener(this);
		
		
		search = new JButton("Search");
		search.setActionCommand("SEARCH");
		search.addActionListener(this);
				
		clear = new JButton("Clear");
		clear.setActionCommand("CLEAR");
		clear.addActionListener(this);
		
		rentButton = new JButton("Rent");
		rentButton.setActionCommand("RENT");
		rentButton.addActionListener(this);
		rentButton.setEnabled(false);
		
		removeButton = new JButton("Remove");
		removeButton.addActionListener(this);
		removeButton.setActionCommand("REMOVE");
		removeButton.setEnabled(false);
		
		updateButton = new JButton("Update");
		updateButton.addActionListener(this);
		updateButton.setActionCommand("UPDATE");
		updateButton.setEnabled(false);
		
		sequalButton = new JButton("Get Sequals");
		sequalButton.setActionCommand("SEQUALS");
		sequalButton.addActionListener(this);
		sequalButton.setEnabled(false);
		
		adminModeSwitch = new Switch();
		adminModeSwitch.addMouseListener(this);
		
		
				
		userLabel = new JLabel("Username:");
		passLabel = new JLabel("Password:");
		searchLabel = new JLabel("Search: ");
		
		sequalLabel = new JLabel("Sequal: ");
		castLabel = new JLabel("Cast: ");
		directorLabel = new JLabel("Director: ");
		awardsLabel = new JLabel("Awards: ");
		
		adminOn = new JLabel("Admin");
		adminOff = new JLabel("User");
		
		adminSwitchPanel.add(adminOn);
		adminSwitchPanel.add(adminModeSwitch);
		adminSwitchPanel.add(adminOff);
		
		userField = new JTextField();
		userField.setPreferredSize(new Dimension(120, 25));
		
		passField = new JPasswordField();
		passField.setPreferredSize(new Dimension(120, 25));
		
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(200, 25));
		
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
		
		searchPanel.add(search);
		searchPanel.add(clear);
		
		comboBox = new JComboBox<String>(choices);
		  
		dataTable = new JTable(new DefaultTableModel());
		 
		awardWinner = new JCheckBox("Award Winners Only");
		newToMe = new JCheckBox("New To Me");
		
		cBoxPanel.add(awardWinner);
		cBoxPanel.add(newToMe);
		
		gameRad = new JRadioButton("Games");
		movRad = new JRadioButton("Movies");
		bothRad = new JRadioButton("Both", true);
		
		radGroup.add(gameRad);
		radGroup.add(movRad);
		radGroup.add(bothRad);
		
		radButtonPanel.add(movRad);
		radButtonPanel.add(gameRad);
		radButtonPanel.add(bothRad);
				
		sPane = new JScrollPane(dataTable);
		
		movieDataPanel.add(sequalLabel);
		movieDataPanel.add(directorLabel);
		movieDataPanel.add(castLabel);
		movieDataPanel.add(awardsLabel);
		
		memberFieldPanel.add(searchLabel);
		memberFieldPanel.add(searchField);
		memberFieldPanel.add(comboBox);
		
		memberDataPanel.add(sPane);
		memberDataPanel.add(movieDataPanel);
		
		memberTopPanel.add(memberFieldPanel);
		memberTopPanel.add(radButtonPanel);
		memberTopPanel.add(cBoxPanel);
		memberTopPanel.add(searchPanel);
				 
		cp = getContentPane();
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.WEST);
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.EAST);
		cp.add(Box.createRigidArea(new Dimension(0,25)), BorderLayout.NORTH);
		cp.add(loginPanel, BorderLayout.CENTER);
		setupMainFrame();
		
	}//end constructor
	
	//-----------------------------------------------------------------------------
	
	 @Override
	    public void mouseClicked(MouseEvent e) // toggle between admin and rental mode for admin users
	    {
		 	if (adminModeSwitch.isOnOff())
		 	{
		 		adminMenu.setVisible(true);
		 		rentButton.setVisible(false);
		 		updateButton.setVisible(true);
		 		removeButton.setVisible(true);
		 	}
		 	
		 	else
		 	{
		 		adminMenu.setVisible(false);
		 		rentButton.setVisible(true);
		 		updateButton.setVisible(false);
		 		removeButton.setVisible(false);
		 	}
		 		
	    }
	 
		//-----------------------------------------------------------------------------

	    public void mousePressed(MouseEvent e) 
	    { }
	    
		//-----------------------------------------------------------------------------

	    public void mouseReleased(MouseEvent e) 
	    {}
	    
		//-----------------------------------------------------------------------------

	    public void mouseEntered(MouseEvent e) 
	    {}
	    
		//-----------------------------------------------------------------------------

	    public void mouseExited(MouseEvent e) 
	    {}
	
	    //-----------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("REGISTER"))
		{
					doRegister();
		}//end resgister conditional

		else if (e.getActionCommand().equals("LOGIN"))
		{
			boolean login;
			login = MyInputVerifier.emailVerifier(userField, userLabel);
			if (login == false)
				return;
			
			username = userField.getText().trim();
			password = new String(passField.getPassword());
			boolean isSuccessful = attemptLogin(username, password);
			if (isSuccessful)
				doLogin();
			else{
				//TODO: Display some sort of error on login page
				System.out.println("Failed login.");
			}
				
		}//end logon conditional
		
		else if (e.getActionCommand().equals("SEARCH")){
			search();
		}
		
		else if (e.getActionCommand().equals("SEQUALS")){
			sequalButton.setEnabled(false);
			rentButton.setEnabled(false);
			removeButton.setEnabled(false);
			updateButton.setEnabled(false);
			String eid = dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString();
			System.out.println("Find sequals with this EID: " + eid);
			
			Entertainment entertainment = new Entertainment();
			DefaultTableModel tableModel = entertainment.findAllSequalsForEid(Integer.valueOf(eid));
			
			memberDataPanel.removeAll();
			dataTable = new JTable(tableModel);
			dataTable.getSelectionModel().addListSelectionListener(this);
			JScrollPane newPane = new JScrollPane(dataTable);
			newPane.setPreferredSize(new Dimension(500, 200));
			memberDataPanel.add(newPane);
			memberDataPanel.add(movieDataPanel);
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
	
		
		else if (e.getActionCommand().equals("LOGOUT"))
		{
			username = "";
			password = "";
			
			passField.setText("");
			userField.setText("");
			
			setVisible(false);
			cp.removeAll();
			setJMenuBar(null);
			
			cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.WEST);
			cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.EAST);
			cp.add(Box.createRigidArea(new Dimension(0,25)), BorderLayout.NORTH);
			cp.add(loginPanel, BorderLayout.CENTER);
			
			rootPane.setDefaultButton(login);
			
			setupMainFrame();
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
		
		else if (e.getActionCommand().equals("RENT")){
			try {
				int eid = (int) dataTable.getValueAt(dataTable.getSelectedRow(), 0);
				String statusMsg = currentUser.rent(eid);
				JOptionPane.showMessageDialog(this, statusMsg);
			} catch (RentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand().equals("MYINFO")){
			RegHub changeRegHub = new RegHub(currentUser);	
		}
		
		else if (e.getActionCommand().equals("INVENTORY")){
			AddInventoryDialog addInventoryDialog = new AddInventoryDialog();
		}
		
		else if (e.getActionCommand().equals("REMOVE")){
			try {
				int eid = (int) dataTable.getValueAt(dataTable.getSelectedRow(), 0);
				Entertainment entertainment = new Entertainment(eid);
				String statusMsg = entertainment.removeEntertainment();
				JOptionPane.showMessageDialog(this, statusMsg);
				search();
			} catch (GetEntertainmentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand().equals("UPDATE")){
			try {
				int eid = (int) dataTable.getValueAt(dataTable.getSelectedRow(), 0);
				Entertainment entertainment = new Entertainment(eid);
				AddInventoryDialog addInventoryDialog = new AddInventoryDialog(entertainment);
			} catch (GetEntertainmentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand().equals("EUSER")){
			UserManagementDialog userManagementDialog = new UserManagementDialog();
		}
	}//end actionPerformed() method
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		System.out.println(dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString());
		int eid = (int)dataTable.getValueAt(dataTable.getSelectedRow(), 0);
		SwingUtilities.invokeLater( 
	        new Runnable() {
	            public void run() {
	            	try {
						Entertainment entertainment = new Entertainment(eid);
						String sequalTitle = entertainment.getSequal().getTitle();
						if (sequalTitle != null)
							sequalLabel.setText("Sequal: " + entertainment.getSequal().getTitle());
						else
							sequalLabel.setText("Sequal: NONE");
						sequalLabel.repaint();
						sequalLabel.revalidate();
						
						ArrayList<CastMember> castList = entertainment.getCastMembers();
						String castLabelText = "Cast: ";
						String directorLabelText = "Director: ";
						for (int i = 0; i < castList.size()-1; i++) {
							if (castList.get(i).director){
								directorLabelText += castList.get(i).getName() + " ";
							}
							else
								castLabelText += castList.get(i).getName() + ", ";
						}
						if (castList.size() > 0)
							castLabelText += castList.get(castList.size()-1).getName();
						castLabel.setText(castLabelText);
						castLabel.repaint();
						castLabel.revalidate();
						directorLabel.setText(directorLabelText);
						directorLabel.repaint();
						directorLabel.revalidate();
						
						ArrayList<Award> awardList = entertainment.getAwardsWon();
						String awardLabelText = "Award: ";
						for (int i = 0; i < awardList.size()-1; i++) {
							awardLabelText += awardList.get(i).getName() + ", ";
						}
						if (awardList.size() > 0)
							awardLabelText += awardList.get(awardList.size()-1).getName();
						awardsLabel.setText(awardLabelText);
						awardsLabel.repaint();
						awardsLabel.revalidate();
						
					} catch (GetEntertainmentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                sequalButton.setEnabled(true);
	                rentButton.setEnabled(true);
	                removeButton.setEnabled(true);
	                updateButton.setEnabled(true);
	                
	                
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
		srButtonPanel.removeAll();
		adminSouthPanel.removeAll();
		
		cp.add(memberTopPanel, BorderLayout.NORTH);
		cp.add(memberDataPanel, BorderLayout.CENTER);
		setJMenuBar(newMenuBar());
		
		if (!adminStatus) //set up GUI for non-admin user
		{
			rentButton.setVisible(true);
			srButtonPanel.add(logoutButton);
			srButtonPanel.add(Box.createRigidArea(new Dimension(140,0)));
			srButtonPanel.add(rentButton);
			srButtonPanel.add(sequalButton);
			srButtonPanel.add(Box.createRigidArea(new Dimension(150,0)));
			cp.add(srButtonPanel, BorderLayout.SOUTH);
		}
		else  //set up GUI for admin purposes
		{
			//rentButton.setVisible(fasle);
			adminSouthPanel.add(logoutButton);
			adminSouthPanel.add(Box.createRigidArea(new Dimension(160,0)));
			adminSouthPanel.add(rentButton);
			rentButton.setVisible(false);
			adminSouthPanel.add(sequalButton);
			adminSouthPanel.add(updateButton);
			adminSouthPanel.add(removeButton);
			adminSouthPanel.add(Box.createRigidArea(new Dimension(50,0)));
			adminSouthPanel.add(adminSwitchPanel);
			adminSouthPanel.add(Box.createRigidArea(new Dimension(20,0)));
			cp.add(adminSouthPanel, BorderLayout.SOUTH);
		}
		rootPane.setDefaultButton(search);
		
		setTitle("Movies-R-Us");
		setVisible(true);
		search();
	}//end doLogin() method
	
	//-----------------------------------------------------------------------------
	public void doRegister()
	{
		mainRegHub = new RegHub(false);
		mainRegHub.addWindowListener(this);
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

//~~~~~~~~~~~~~~~~~~~~~~~~~~Method to create menu bar~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private JMenuBar newMenuBar()
		{
			JMenuBar menuBar;
			JMenu subMenu;

			menuBar = new JMenuBar();
			//menuBar.setBackground(new Color(129, 160, 225));
			
			//------------------------------------------------
			subMenu = new JMenu("My Account");

			subMenu.setMnemonic(KeyEvent.VK_M);
			subMenu.setToolTipText("List specific operations");
			subMenu.add(newItem("Edit My Info", "MYINFO", this, KeyEvent.VK_E, KeyEvent.VK_E, "Allows current user to edit/update their information."));
			subMenu.add(newItem("Rental History", "USER_HISTORY", this, KeyEvent.VK_R, KeyEvent.VK_R, "Retrievs current users rental history."));
				
			menuBar.add(subMenu);
			
			//-----------------------------------------------

			if (adminStatus)
			{
				menuBar.add(adminMenu);
			}
			//-------------------------------------------------

			return menuBar;
		}//End constructor for JMenuBar.

	//~~~~~~~~~~~~~~~~~~~~~~~~~~Constructor for menu Items~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private JMenuItem newItem(String 			label,
								  					   String 			actionCommand,
								  					   ActionListener	menuListener,
								  					   int 				mnemonic,
								  					   int 				keyCode,
								  					   String 			toolTipText)
		{
			JMenuItem m;
			m = new JMenuItem(label, mnemonic);
			m.setAccelerator(KeyStroke.getKeyStroke(keyCode, ActionEvent.ALT_MASK));
			m.setToolTipText(toolTipText);
			m.addActionListener(menuListener);
			m.setActionCommand(actionCommand);

			return m;
		}//End constructor for JMenuItem.

//------------------------------------------------------------------------------------------------------------	
	private boolean attemptLogin(String email, String password)
	{
		currentUser = new User();
		try {
			currentUser.login(email, password);
			adminStatus = currentUser.admin; //allows GUI to be setup for admin
			return true;
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void search(){
		sequalButton.setEnabled(false);
		rentButton.setEnabled(false);
		removeButton.setEnabled(false);
		updateButton.setEnabled(false);
		String searchTerm = searchField.getText().trim();
		String searchBy = comboBox.getSelectedItem().toString().toUpperCase();
		if (searchTerm.length() == 0){
			searchTerm = "%";
		}
		Entertainment entertainment = new Entertainment();
		String userEmail = null;
		if (newToMe.isSelected())
			userEmail = currentUser.getEmail();
		DefaultTableModel tableModel = entertainment.searchBy(searchTerm, searchBy, userEmail, awardWinner.isSelected(), gameRad.isSelected(), movRad.isSelected());

		memberDataPanel.removeAll();
		dataTable = new JTable(tableModel);
		dataTable.getSelectionModel().addListSelectionListener(this);
		JScrollPane newPane = new JScrollPane(dataTable);
		newPane.setPreferredSize(new Dimension(500, 200));
		memberDataPanel.add(newPane);
		memberDataPanel.add(movieDataPanel);
		memberDataPanel.revalidate();
		memberDataPanel.repaint();
	}

	@Override
	public void windowActivated(WindowEvent e) {
		System.out.println("Window activated...");
	}
	@Override
	public void windowClosed(WindowEvent e) {
		if (mainRegHub.getUserEmail() != null){
			try {
				currentUser = new User(mainRegHub.getUserEmail(), mainRegHub.getUserPass(), mainRegHub.getUserName(), 
						mainRegHub.getUserPhone(), mainRegHub.getUserStreet(), mainRegHub.getUserCity(), 
						mainRegHub.getUserState(), mainRegHub.getUserZip(), false);
			} catch (LoginException e1) {
				System.out.println("Failed to create user...");
				e1.printStackTrace();
			}
			
			boolean isSuccessful = attemptLogin(currentUser.getEmail(), mainRegHub.getUserPass());
			if (isSuccessful)
				doLogin();
			else{
				//TODO: Display some sort of error on login page
				System.out.println("Failed login.");
			}
		}
		else{
			System.out.println("Registration Window Closed...");
		}
	}
	@Override
	public void windowClosing(WindowEvent e) {
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowOpened(WindowEvent e) {
	}

}//end MainGUI class