import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class UserManagementDialog extends JDialog implements ListSelectionListener, ActionListener, WindowListener {
	
	JPanel mainPanel, buttonPanel;
	JButton addUserButton, removeUserButton;
	
	JScrollPane sPane;
	JTable dataTable;
	
	RegHub mainHub;
	
	public UserManagementDialog() {
		
		DefaultTableModel tableModel = User.getArrayListOfAllItems();
		dataTable = new JTable(tableModel);
		dataTable.getSelectionModel().addListSelectionListener(this);
		sPane = new JScrollPane(dataTable);
		
		removeUserButton = new JButton("Remove User");
		removeUserButton.addActionListener(this);
		removeUserButton.setActionCommand("REMOVE");
		removeUserButton.setEnabled(false);
		
		addUserButton = new JButton("Add New User");
		addUserButton.addActionListener(this);
		addUserButton.setActionCommand("ADD");
		
		mainPanel = new JPanel();
		mainPanel.add(sPane);
		
		buttonPanel = new JPanel();
		buttonPanel.add(removeUserButton);
		buttonPanel.add(addUserButton);
		
		add(mainPanel);
		add(buttonPanel, BorderLayout.SOUTH);
		setSize(550, 415);
		setTitle("[Admin] Manage Users");
		add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		removeUserButton.setEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("REMOVE")){
				
				String userEmail = (String) dataTable.getValueAt(dataTable.getSelectedRow(), 0);
				User user = new User(userEmail);
				String statusMsg = user.removeUser();
				JOptionPane.showMessageDialog(this, statusMsg);
				
				DefaultTableModel tableModel = User.getArrayListOfAllItems();
				
				mainPanel.removeAll();
				dataTable = new JTable(tableModel);
				dataTable.getSelectionModel().addListSelectionListener(this);
				JScrollPane newPane = new JScrollPane(dataTable);
				newPane.setPreferredSize(new Dimension(500, 200));
				mainPanel.add(newPane, BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
		}
		
		else if (e.getActionCommand().equals("ADD")){
			mainHub = new RegHub(true);
			mainHub.addWindowListener(this);
		}
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		User currentUser;
		if (mainHub.getUserEmail() != null){
			try {
				currentUser = new User(mainHub.getUserEmail(), mainHub.getUserPass(), mainHub.getUserName(), 
						mainHub.getUserPhone(), mainHub.getUserStreet(), mainHub.getUserCity(), 
						mainHub.getUserState(), mainHub.getUserZip(), mainHub.getAdminStatus());
				
				DefaultTableModel tableModel = User.getArrayListOfAllItems();
				
				mainPanel.removeAll();
				dataTable = new JTable(tableModel);
				dataTable.getSelectionModel().addListSelectionListener(this);
				JScrollPane newPane = new JScrollPane(dataTable);
				newPane.setPreferredSize(new Dimension(500, 200));
				mainPanel.add(newPane, BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			} catch (LoginException e1) {
				System.out.println("Failed to create user...");
				e1.printStackTrace();
			}
		}
		else{
			System.out.println("Registration Window Closed...");
		}
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
