import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
//import javax.swing.event.*;
//import java.lang.*;
import java.util.ArrayList;

public class RegHub extends JDialog implements WindowListener,
																			   ActionListener
{
	User currentUser;
	JLabel nameLabel, emailLabel,  addressLabel_1, addressLabel_2, cityLabel, stateLabel, zipLabel, phoneLabel, planLabel, passwordLabel, passConfLabel;
	JButton saveButton;
	JPasswordField passField, passConfField;
	JTextField nameField, emailField, addressField_1, addressField_2, cityField, zipField, phoneField;
	JComboBox<String> planBox, stateBox;
	
	GroupLayout layout;
	
	JButton register, cancel;
	
	JPanel buttonPanel;
	
	MainGUI parentGUI;
	
	String userName = null;
	String userEmail = null;
	String userStreet = null;
	String userCity = null;
	String userState = null;
	int userZip;
	String userPhone = null;
	String userPass = null;
	int userPlan;
	
	ArrayList<String> stateList;
	//====================RegHub() constructor===================================================
	public RegHub()
	{
		Container pane = new Container();
		String[] plans = {"-Please Selecta Plan-", "Tri-Star Plan (max 3 rentals at a time)", "Columbia Plan (max 5 rentals at a time)",
									 "Paramount Plan (max 7 rentals at a time)", "Universal Plan (max 9 rentals at a time)"};
		String[] states = {"-Choose Your State-", "AL", "AK", "AZ", "AR", "CA",
				 "CO", "CT", "DE", "FL", "GA",
				 "HI", "ID", "IL", "IN", "IA",
				 "KS", "KY", "LA", "ME", "MD",
				 "MA", "MI", "MN", "MS", "MO",
				 "MT", "NE", "NV", "NH", "NJ",
				 "NM", "NY", "NC", "ND", "OH",
				 "OK", "OR", "PA", "RI", "SC",
				 "SD", "TN", "TX", "UT", "VT",
				 "VA", "WA", "WV", "WI", "WY"};
		
		
		nameLabel = new JLabel("Name: ");
		emailLabel = new JLabel("User E-mail: ");
		addressLabel_1 = new JLabel("User Mailing Address: ");
		addressLabel_2 = new JLabel("");
		cityLabel = new JLabel("City: ");
		stateLabel = new JLabel("State: ");
		zipLabel = new JLabel("Zip code: ");
		phoneLabel = new JLabel("User Phone Numer: ");
		planLabel = new JLabel("Choose a rental plan: ");
		passwordLabel = new JLabel("Choose a Password: ");
		passConfLabel = new JLabel("Confirm Password: ");
		
		nameField = new JTextField();
		nameField.requestFocus();
		nameField.setMaximumSize(new Dimension(300, 25));
		emailField = new JTextField();
		emailField.setMaximumSize(new Dimension(300, 25));
		addressField_1 = new JTextField();
		addressField_1.setMaximumSize(new Dimension(300, 25));
		addressField_2 = new JTextField();
		addressField_2.setMaximumSize(new Dimension(300, 25));
		cityField = new JTextField();
		cityField.setMaximumSize(new Dimension(300, 25));
		zipField = new JTextField();
		zipField.setMaximumSize(new Dimension(100, 25));
		phoneField = new JTextField();
		phoneField.setMaximumSize(new Dimension(250, 25));
		
		passField = new JPasswordField();
		passField.setMaximumSize(new Dimension(300, 25));
		passConfField = new JPasswordField();
		passConfField.setMaximumSize(new Dimension(300, 25));
		
		planBox = new JComboBox<String>(plans);
		planBox.setSelectedItem(plans[0]);
		planBox.setMaximumSize(new Dimension(200, 25));
		
		stateBox = new JComboBox<String>(states);
		stateBox.setSelectedItem(states[0]);
		stateBox.setMaximumSize(new Dimension(150, 25));
		
		layout = new GroupLayout(pane);
		pane.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup horizGroup = layout.createSequentialGroup();
		horizGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(emailLabel).addComponent(phoneLabel).addComponent(addressLabel_1)
														.addComponent(addressLabel_2).addComponent(cityLabel).addComponent(stateLabel).addComponent(zipLabel)
														.addComponent(planLabel).addComponent(passwordLabel).addComponent(passConfLabel));
		horizGroup.addGroup(layout.createParallelGroup().addComponent(nameField).addComponent(emailField).addComponent(phoneField).addComponent(addressField_1)
														.addComponent(addressField_2).addComponent(cityField).addComponent(stateBox).addComponent(zipField).addComponent(planBox)
														.addComponent(passField).addComponent(passConfField));
		layout.setHorizontalGroup(horizGroup);

		GroupLayout.SequentialGroup vertGroup = layout.createSequentialGroup();
		vertGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(nameField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(emailLabel).addComponent(emailField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(phoneLabel).addComponent(phoneField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(addressLabel_1).addComponent(addressField_1));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(addressLabel_2).addComponent(addressField_2));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(cityLabel).addComponent(cityField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(stateLabel).addComponent(stateBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(zipLabel).addComponent(zipField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(planLabel).addComponent(planBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(passwordLabel).addComponent(passField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(passConfLabel).addComponent(passConfField));
		layout.setVerticalGroup(vertGroup);
		
		buttonPanel = new JPanel();
		buttonPanel.add(Box.createRigidArea(new Dimension(285, 40)));
		
		register = new JButton("Register");
		register.setActionCommand("REGISTER");
		rootPane.setDefaultButton(register);
		register.addActionListener(this);
		buttonPanel.add(register);
		cancel = new JButton("Cancel");
		cancel.setActionCommand("CANCEL");
		cancel.addActionListener(this);
		buttonPanel.add(cancel);
		
		add(pane, BorderLayout.NORTH);
		setSize(550, 415);

		setTitle("Movies-R-Us New User Registration Hub");
		add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public RegHub(User user){
		currentUser = user;
		
		//this.parentGUI = parentGUI;
		Container pane = new Container();
		String[] plans = {"-Please Selecta Plan-", "Tri-Star Plan (max 3 rentals at a time)", "Columbia Plan (max 5 rentals at a time)",
									 "Paramount Plan (max 7 rentals at a time)", "Universal Plan (max 9 rentals at a time)"};
		String[] states = {"-Choose Your State-", "AL", "AK", "AZ", "AR", "CA",
									 "CO", "CT", "DE", "FL", "GA",
									 "HI", "ID", "IL", "IN", "IA",
									 "KS", "KY", "LA", "ME", "MD",
									 "MA", "MI", "MN", "MS", "MO",
									 "MT", "NE", "NV", "NH", "NJ",
									 "NM", "NY", "NC", "ND", "OH",
									 "OK", "OR", "PA", "RI", "SC",
									 "SD", "TN", "TX", "UT", "VT",
									 "VA", "WA", "WV", "WI", "WY"};
		
		stateList = new ArrayList<String>();
		for (int i = 0; i < states.length; i++) {
			stateList.add(states[i]);
		}
		
		nameLabel = new JLabel("Name: ");
		emailLabel = new JLabel("User E-mail: ");
		addressLabel_1 = new JLabel("User Mailing Address: ");
		addressLabel_2 = new JLabel("");
		cityLabel = new JLabel("City: ");
		stateLabel = new JLabel("State: ");
		zipLabel = new JLabel("Zip code: ");
		phoneLabel = new JLabel("User Phone Numer: ");
		planLabel = new JLabel("Choose a rental plan: ");
		passwordLabel = new JLabel("Choose a Password: ");
		passConfLabel = new JLabel("Confirm Password: ");
		
		nameField = new JTextField(currentUser.getName());
		nameField.requestFocus();
		nameField.setMaximumSize(new Dimension(300, 25));
		emailField = new JTextField(currentUser.getEmail());
		emailField.setMaximumSize(new Dimension(300, 25));
		emailField.setEnabled(false);
		addressField_1 = new JTextField(currentUser.getStreet());
		addressField_1.setMaximumSize(new Dimension(300, 25));
		addressField_2 = new JTextField();
		addressField_2.setMaximumSize(new Dimension(300, 25));
		cityField = new JTextField(currentUser.getCity());
		cityField.setMaximumSize(new Dimension(300, 25));
		zipField = new JTextField(String.valueOf(currentUser.getZip()));
		zipField.setMaximumSize(new Dimension(100, 25));
		phoneField = new JTextField(currentUser.getPhone());
		phoneField.setMaximumSize(new Dimension(250, 25));
		
		String pass = currentUser.getPass();
		passField = new JPasswordField(pass);
		passField.setMaximumSize(new Dimension(300, 25));
		passConfField = new JPasswordField(pass);
		passConfField.setMaximumSize(new Dimension(300, 25));
		
		planBox = new JComboBox<String>(plans);
		planBox.setSelectedItem(plans[currentUser.getUserLvl()]);
		planBox.setMaximumSize(new Dimension(200, 25));
		
		stateBox = new JComboBox<String>(states);
		if (currentUser.getState() != null)
			stateBox.setSelectedItem(states[stateList.indexOf(currentUser.getState())]);
		else
			stateBox.setSelectedItem(states[0]);
		stateBox.setMaximumSize(new Dimension(150, 25));
		
		layout = new GroupLayout(pane);
		pane.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup horizGroup = layout.createSequentialGroup();
		horizGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(emailLabel).addComponent(phoneLabel).addComponent(addressLabel_1)
														.addComponent(addressLabel_2).addComponent(cityLabel).addComponent(stateLabel).addComponent(zipLabel)
														.addComponent(planLabel).addComponent(passwordLabel).addComponent(passConfLabel));
		horizGroup.addGroup(layout.createParallelGroup().addComponent(nameField).addComponent(emailField).addComponent(phoneField).addComponent(addressField_1)
														.addComponent(addressField_2).addComponent(cityField).addComponent(stateBox).addComponent(zipField).addComponent(planBox)
														.addComponent(passField).addComponent(passConfField));
		layout.setHorizontalGroup(horizGroup);

		GroupLayout.SequentialGroup vertGroup = layout.createSequentialGroup();
		vertGroup.addGroup(layout.createParallelGroup().addComponent(nameLabel).addComponent(nameField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(emailLabel).addComponent(emailField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(phoneLabel).addComponent(phoneField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(addressLabel_1).addComponent(addressField_1));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(addressLabel_2).addComponent(addressField_2));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(cityLabel).addComponent(cityField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(stateLabel).addComponent(stateBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(zipLabel).addComponent(zipField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(planLabel).addComponent(planBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(passwordLabel).addComponent(passField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(passConfLabel).addComponent(passConfField));
		layout.setVerticalGroup(vertGroup);
		
		buttonPanel = new JPanel();
		buttonPanel.add(Box.createRigidArea(new Dimension(285, 40)));
		
		saveButton = new JButton("Save");
		saveButton.setActionCommand("SAVE");
		rootPane.setDefaultButton(saveButton);
		saveButton.addActionListener(this);
		buttonPanel.add(saveButton);
		cancel = new JButton("Close");
		cancel.setActionCommand("CANCEL");
		cancel.addActionListener(this);
		buttonPanel.add(cancel);
		
		add(pane, BorderLayout.NORTH);
		setSize(550, 415);

		setTitle("Change Member Info");
		add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//====================actionPerformed(Actionevent e)============================================
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("CANCEL"))
		{
			dispose();
		}
		else if(e.getActionCommand().equals("REGISTER"))
		{
			if (verifyInfo()){
				this.userEmail = emailField.getText().trim();
				this.userName = nameField.getText().trim();
				this.userPass = String.valueOf(passField.getPassword());
				this.userStreet = addressField_1.getText().trim();
				this.userCity = cityField.getText().trim();
				this.userState = stateBox.getSelectedItem().toString();
				this.userZip = Integer.parseInt(zipField.getText().trim());
				this.userPhone = phoneField.getText().trim();
				this.userPlan = planBox.getSelectedIndex();
				
				dispose();
			}	
		}
		else if (e.getActionCommand().equals("SAVE")){
			String resultMessage = "";
			
			if (verifyInfo()){
				if (!nameField.getText().trim().equals(currentUser.getName())){
					if (!currentUser.changeName(nameField.getText().trim())){
						resultMessage += "Failed to change name. ";
					}
					else{
						resultMessage += "Successfully changed name. ";
					}
				}
				
				if (!(new String(passField.getPassword()).equals(currentUser.getPass()))){
					if (!currentUser.changePassword(new String(passField.getPassword()))){
						resultMessage += "Failed to change password | ";
					}
					else{
						resultMessage += "Successfully changed password. ";
					}
				}
				
				if (!phoneField.getText().trim().equals(currentUser.getPhone())){
					if (!currentUser.changePhone(phoneField.getText().trim())){
						resultMessage += "Failed to change phone number. ";
					}
					else{
						resultMessage += "Successfuly changed phone number. ";
					}
				}
				
				if (!addressField_1.getText().trim().equals(currentUser.getStreet()) 
						|| !cityField.getText().trim().equals(currentUser.getCity()) 
						|| !stateBox.getSelectedItem().equals(currentUser.getState())
						|| !zipField.getText().trim().equals(String.valueOf(currentUser.getZip()))){
					if (!currentUser.changeAddress(addressField_1.getText().trim(), cityField.getText().trim(), stateBox.getSelectedItem().toString(), Integer.parseInt(zipField.getText().trim()))){
						resultMessage += "Failed to change address. ";
					}
					else{
						resultMessage += "Successfully changed address. ";
					}
				}
				
				if (planBox.getSelectedIndex() != currentUser.getUserLvl()){
					if (!currentUser.changeUserLvl(planBox.getSelectedIndex())){
						resultMessage += "Failed to change member plan. ";
					}
					else{
						resultMessage += "Successfully changed member plan. ";
					}
				}
				
				JOptionPane.showMessageDialog(this, resultMessage);
			}
		}
	}
	//================(WindowEvent e)===========================================
	//====================windowActivated(WindowEvent e)===========================================

		public void windowActivated(WindowEvent e)
		{}

	//====================windowClosed(WindowEvent e)==============================================

		public void windowClosed(WindowEvent e)
		{}

	//====================windowClosing(WindowEvent e)=============================================

	public void windowClosing(WindowEvent e)
		{
			/*try
			{
				int response = JOptionPane.showConfirmDialog(null, "Would you like to save this record before exiting?", "UNSAVED CHANGES", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (response == JOptionPane.YES_OPTION)
				{
					//doSubmit(selectedIndex);
				}

				else*/
					dispose();
			/*}
			catch(ParseException pe)
			{
				System.out.println("ParseException thrown in edit dialog.");
			}*/
		}//end windowclosing() methoid

	//====================windowDeactivated(WindowEvent e)=========================================

		public void windowDeactivated(WindowEvent e)
		{}

	//====================windowDeiconified(WindowEvent e)=========================================

		public void windowDeiconified(WindowEvent e)
		{}

	//====================windowIconified(WindowEvent e)===========================================

		public void windowIconified(WindowEvent e)
		{}

	//====================windowOpened(WindowEvent e)==============================================

		public void windowOpened(WindowEvent e)
		{}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		public String getUserName() {
			return userName;
		}
		public String getUserEmail() {
			return userEmail;
		}
		public String getUserStreet() {
			return userStreet;
		}
		public String getUserCity() {
			return userCity;
		}
		public String getUserState() {
			return userState;
		}
		public int getUserZip() {
			return userZip;
		}
		public String getUserPhone() {
			return userPhone;
		}
		public String getUserPass() {
			return userPass;
		}
		public int getUserPlan() {
			return userPlan;
		}
		
		public boolean verifyInfo(){
			if (!MyInputVerifier.nameVerification(nameField, nameLabel)){
				System.out.println("Name Not Valid.");
				return false;
			}
			else if (!MyInputVerifier.emailVerifier(emailField, emailLabel)){
				System.out.println("Email Not Valid");
				return false;
			}
			else if (!MyInputVerifier.phoneVerifier(phoneField, phoneLabel)){
				System.out.println("Phone Not Valid");
				return false;
			}
			else if (!MyInputVerifier.addressVerifier(addressField_1, addressLabel_1, cityField, cityLabel, stateBox, stateLabel, zipField, zipLabel)){
				System.out.println("Address Not Valid.");
				return false;
			}
			else if (!MyInputVerifier.planVerifier(planBox, planLabel)){
				System.out.println("Plan Not Valid.");
				return false;
			}
			else if (!MyInputVerifier.passwordVerifier(passField, passwordLabel, passConfField, passConfLabel)){
				System.out.println("Password Not Valid.");
				return false;
			}
			else{
				return true;
			}
		}
		
}// end RegHub class