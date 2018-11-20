import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

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

class MainGUI extends JFrame implements ActionListener, ListSelectionListener, WindowListener/*,
																		ListSelectionListener,
																		MouseListener*/
{
	Toolkit tk;
	Dimension d;
	Container cp;
	
	JScrollPane scroller;
	JTable tableViewer;
	
	JPanel memberFieldPanel, memberPanel, memberDataPanel, memberTopPanel, searchPanel, radButtonPanel, cBoxPanel;
	JButton search, clear, rentButton, sequalButton, userHistoryButton, adminGetLast24Button, adminGetTop10Button;
	JLabel searchLabel;
	JTextField searchField;
	
	JCheckBox awardWinner, newToMe;
	JRadioButton gameRad, movRad, bothRad;
	ButtonGroup radGroup;
	
	JComboBox<String> comboBox;
	JScrollPane sPane;
	JTable dataTable;
	
	JPanel loginPanel, userPanel, passPanel, adminPanel, fieldPanel, logButtonPanel, srButtonPanel;
	JButton login, register;
	JLabel userLabel, passLabel;
	JTextField userField;
	JPasswordField passField;
	
	User currentUser;
	RegHub mainRegHub;
	//-----------------------------------------------------------------------------
	public MainGUI()
	{
		String[] choices = { "Title","Actor", "Genre","Director","Platform"};
		
		tk = Toolkit.getDefaultToolkit();
		d = tk.getScreenSize();
		
		memberTopPanel = new JPanel(new GridLayout(4, 1));
		memberDataPanel = new JPanel(new BorderLayout());
		//memberPanel = new JPanel(new BorderLayout());
		memberFieldPanel = new JPanel();
		srButtonPanel = new JPanel();
		searchPanel = new JPanel();
		userPanel = new JPanel(new GridLayout(2, 1));
		passPanel = new JPanel(new GridLayout(2, 1));
		fieldPanel = new JPanel(new GridLayout(2, 1));
		loginPanel = new JPanel(new GridLayout(2,1));
		logButtonPanel = new JPanel();
		radButtonPanel = new JPanel();
		cBoxPanel = new JPanel();
		
		radGroup = new ButtonGroup();
				
		login = new JButton("Login");
		login.setActionCommand("LOGIN");
		login.addActionListener(this);
		rootPane.setDefaultButton(login);
		
		register = new JButton("Register");
		register.setActionCommand("REGISTER");
		register.addActionListener(this);
		
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
		
		sequalButton = new JButton("Get Sequals");
		sequalButton.setActionCommand("SEQUALS");
		sequalButton.addActionListener(this);
		sequalButton.setEnabled(false);
		
		userLabel = new JLabel("Username:");
		passLabel = new JLabel("Password:");
		searchLabel = new JLabel("Search: ");
		
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
		
		srButtonPanel.add(rentButton);
		srButtonPanel.add(sequalButton);
		
		sPane = new JScrollPane(dataTable);
		
		memberFieldPanel.add(searchLabel);
		memberFieldPanel.add(searchField);
		memberFieldPanel.add(comboBox);
		
		memberDataPanel.add(sPane, BorderLayout.CENTER);
		 
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
			sequalButton.setEnabled(false);
			rentButton.setEnabled(false);
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
			memberDataPanel.add(newPane, BorderLayout.CENTER);
			memberDataPanel.revalidate();
			memberDataPanel.repaint();
		}
		
		else if (e.getActionCommand().equals("SEQUALS")){
			sequalButton.setEnabled(false);
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
	}//end actionPerformed() method
	
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		System.out.println(dataTable.getValueAt(dataTable.getSelectedRow(), dataTable.getSelectedColumn()).toString());
		SwingUtilities.invokeLater( 
	        new Runnable() {
	            public void run() {
	                sequalButton.setEnabled(true);
	                rentButton.setEnabled(true);
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
		cp.add(memberTopPanel, BorderLayout.NORTH);
		cp.add(memberDataPanel, BorderLayout.CENTER);
		cp.add(srButtonPanel, BorderLayout.SOUTH);
		
		setJMenuBar(newMenuBar());
		
		rootPane.setDefaultButton(search);
		
		setTitle("Movies-R-Us");
		setVisible(true);
	}//end doLogin() method
	
	//-----------------------------------------------------------------------------
	public void doRegister()
	{
		mainRegHub = new RegHub(this);
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

//----------------------------------------------------------------------
	//~~~~~~~~~~~~~~~~~~~~~~~~~~Method to create menu bar~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

			private JMenuBar newMenuBar()
			{
				JMenuBar menuBar;
				JMenu subMenu;

				menuBar = new JMenuBar();
				//menuBar.setBackground(new Color(129, 160, 225));
				//-----------------------------------------------
				subMenu = new JMenu("Admin");

				subMenu.setMnemonic(KeyEvent.VK_A);
				subMenu.setToolTipText("Admin. operations");
				subMenu.add(newItem("Edit User", "EUSER", this, KeyEvent.VK_U, KeyEvent.VK_U, "Access and edit a users information."));
				subMenu.add(newItem("Add Invetory", "INVENTORY", this, KeyEvent.VK_I, KeyEvent.VK_I, "Add a new movie or game to the inventory."));
				subMenu.add(newItem("24 Hour Recap", "ADMIN_GET_24", this, KeyEvent.VK_H, KeyEvent.VK_H, "See movies rented in the last 24 hours."));
				subMenu.add(newItem("Top 10", "ADMIN_GET_TOP_10", this, KeyEvent.VK_T, KeyEvent.VK_T, "See to 10 rented items."));

				menuBar.add(subMenu);
				//------------------------------------------------
				subMenu = new JMenu("My Account");

				subMenu.setMnemonic(KeyEvent.VK_M);
				subMenu.setToolTipText("List specific operations");
				subMenu.add(newItem("Edit My Info", "MYINFO", this, KeyEvent.VK_E, KeyEvent.VK_E, "Allows current user to edit/update their information."));
				subMenu.add(newItem("Rental History", "USER_HISTORY", this, KeyEvent.VK_R, KeyEvent.VK_R, "Retrievs current users rental history."));
				
				menuBar.add(subMenu);
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