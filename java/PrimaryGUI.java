import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.*;

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

class MainGUI extends JFrame implements ActionListener/*,
																		ListSelectionListener,
																		MouseListener*/
{
	Toolkit tk;
	Dimension d;
	Container cp;
	
	JScrollPane scroller;
	JTable tableViewer;
	
	
	JPanel memberFieldPanel, memberButtonPanel, memberPanel;
	JButton search, clear;
	JLabel eid, actor, title, genre, director, platform;
	JTextField idField, actorField, titleField, genreField, directorField, platformField;
	
	JPanel loginPanel, userPanel, passPanel, adminPanel, fieldPanel, logButtonPanel;
	JButton login, register;
	JLabel userLabel, passLabel;
	JTextField userField;
	JPasswordField passField;
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
		
		cp = getContentPane();
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.WEST);
		cp.add(Box.createRigidArea(new Dimension(50,0)), BorderLayout.EAST);
		cp.add(Box.createRigidArea(new Dimension(0,25)), BorderLayout.NORTH);
		cp.add(loginPanel, BorderLayout.CENTER);
		
		search = new JButton("Search");
		search.setActionCommand("SEARCH");
		search.addActionListener(this);
		
		clear = new JButton("Clear");
		clear.setActionCommand("CLEAR");
		clear.addActionListener(this);
		
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
		
		 memberFieldPanel = new JPanel(new GridLayout(2, 6));
		 memberButtonPanel = new JPanel();//new GridLayout(1,2));
		 memberPanel = new JPanel(new GridLayout(2,1));
		
		 memberButtonPanel.add(search);
		 memberButtonPanel.add(clear);
		 memberFieldPanel.add(eid);
		 memberFieldPanel.add(actor);
		 memberFieldPanel.add(title);
		 memberFieldPanel.add(genre);
		 memberFieldPanel.add(director);
		 memberFieldPanel.add(platform);
		 memberFieldPanel.add(idField);
		 memberFieldPanel.add(actorField);
		 memberFieldPanel.add(titleField);
		 memberFieldPanel.add(genreField);
		 memberFieldPanel.add(directorField);
		 memberFieldPanel.add(platformField);
		 memberPanel.add(memberFieldPanel);
		 memberPanel.add(memberButtonPanel);
		 
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
			doLogin();
		}//end logon conditional
	}//end actionPerformed() method
	//-----------------------------------------------------------------------------
	public void doLogin()
	{
		setVisible(false);
		setSize((d.width/3)+300, (d.height/3)+250);
		setLocationRelativeTo(null);
	   cp.removeAll();
	   //cp.add(Box.createRigidArea(new Dimension(30,0)), BorderLayout.WEST);
	   //cp.add(Box.createRigidArea(new Dimension(30,0)), BorderLayout.EAST);
	   cp.add(memberPanel, BorderLayout.NORTH);
	   //cp.add(client.secondaryButtonPanel, BorderLayout.SOUTH);
	   setTitle("Movies-R-Us");
	   setVisible(true);
	}//end doLogin() method
	//-----------------------------------------------------------------------------
	public void doRegister()
	{
		
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
}//end MainGUI class