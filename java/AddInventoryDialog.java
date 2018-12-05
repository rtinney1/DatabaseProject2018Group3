import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AddInventoryDialog extends JDialog implements ActionListener{
	
	Entertainment currentEntertainment;
	JPanel buttonPanel, mainPanel, superPanel, dataPanel;
	JButton addButton, closeButton, castButton, removeCastButton, awardButton, removeAwardButton;
	JLabel titleLabel, releaseDateLabel, genreLabel, stockLabel, sequalLabel, 
	platformLabel, versionLabel, castLabel, directorLabel, awardLabel, castListLabel, awardListLabel; 
	JTextField titleField, stockField, sequalIdField, versionField;
	JComboBox<String> monthBox, dayBox, yearBox, genreBox, platformBox, castComboBox, awardComboBox;
	ArrayList<CastMember> castList;
	ArrayList<Award> awardList;
	String previousCastList;
	String previousAwardList;
	
	public AddInventoryDialog() {
		setup(null);
	}
	
	public AddInventoryDialog(Entertainment e){
		this.currentEntertainment = e;
		setup(e);
	}
	
	private void setup(Entertainment e){
		previousCastList = null;
		previousAwardList = null;
		String[] platformChoices = {"DVD", "Blu-ray", "PS3", "PS4", "Xbox 360", "Xbox One", "PC"};
		String[] genreChoices = {"Horror", "Cartoon", "Comedy", "Thriller", "Action", "Drama", "Documentary"};
		String[] monthChoices = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		Vector<String> dayChoices = new Vector<>();
		for (int i = 1; i < 32; i++) {
			dayChoices.add(""+i);
		}
		Vector<String> yearChoices = new Vector<>();
		for (int i = 1900; i < 2019; i++) {
			yearChoices.add(""+i);
		}
		
		
		if (e != null){
			castList = e.getCastMembers();
			awardList = e.getAwardsWon();
		}
		else{
			castList = new ArrayList<>();
			awardList = new ArrayList<>();
		}
		
		castLabel = new JLabel("Add Cast Members");
		awardLabel = new JLabel("Add Awards");
		awardListLabel = new JLabel("Awards: ");
		castListLabel = new JLabel("Cast: ");
		
		for (int i = 0; i < castList.size(); i++) {
			castListLabel.setText(castListLabel.getText() + " " + castList.get(i).getName());
		}
		for (int i = 0; i < awardList.size(); i++) {
			awardListLabel.setText(awardListLabel.getText() + " " + awardList.get(i).getName());
		}
		
		previousCastList = castListLabel.getText();
		previousAwardList = awardListLabel.getText();
		
		awardButton = new JButton("Add");
		awardButton.addActionListener(this);
		awardButton.setActionCommand("AWARD");
		castButton = new JButton("Add");
		castButton.addActionListener(this);
		castButton.setActionCommand("CAST");
		
		removeCastButton = new JButton("Remove");
		removeCastButton.addActionListener(this);
		removeCastButton.setActionCommand("REMOVE_CAST");
		removeAwardButton = new JButton("Remove");
		removeAwardButton.addActionListener(this);
		removeAwardButton.setActionCommand("REMOVE_AWARD");
		
		ArrayList<CastMember> castArray = CastMember.getAllCastMembers();
		ArrayList<Award> awardArray = Award.getAllAwards();
		
		castComboBox = new JComboBox<>();
		for (int i = 0; i < castArray.size(); i++) {
			castComboBox.addItem(castArray.get(i).cid + " : " + castArray.get(i).getName());
		}
		
		awardComboBox = new JComboBox<>();
		for (int i = 0; i < awardArray.size(); i++) {
			awardComboBox.addItem(awardArray.get(i).getAwardID() + " : " + awardArray.get(i).getName());
		}

		titleLabel = new JLabel("Title");
		releaseDateLabel = new JLabel("Release Date");
		genreLabel = new JLabel("Genre");
		stockLabel = new JLabel("Stock");
		sequalLabel = new JLabel("Sequel ID");
		platformLabel = new JLabel("Platform");
		versionLabel = new JLabel("Version");

		titleField = new JTextField();
		stockField = new JTextField();
		sequalIdField = new JTextField();
		versionField = new JTextField();
		
		monthBox = new JComboBox<>(monthChoices);
		yearBox = new JComboBox<>(yearChoices);
		dayBox = new JComboBox<>(dayChoices);
		genreBox = new JComboBox<>(genreChoices);
		platformBox = new JComboBox<>(platformChoices);
		
		String label;
		if (e != null)
			label = "Save";
		else
			label = "Add";
		
		addButton = new JButton(label);
		addButton.addActionListener(this);
		addButton.setActionCommand(label.toUpperCase());
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		closeButton.setActionCommand("CLOSE");
		
		if (e != null){
			titleField.setText(e.getTitle());
			stockField.setText(String.valueOf(e.getNumInStock()));
			sequalIdField.setText(String.valueOf(e.getSequalID()));
			versionField.setText(e.getVersion());
			monthBox.setSelectedItem(e.getReleaseDate().split(" ")[0].trim());
			dayBox.setSelectedItem(e.getReleaseDate().split(" ")[1].trim());
			yearBox.setSelectedItem(e.getReleaseDate().split(" ")[2].trim());
			genreBox.setSelectedItem(e.getGenre());
			platformBox.setSelectedItem(e.getPlatform());
		}
		
		dataPanel = new JPanel(new GridLayout(2, 1));
		superPanel = new JPanel();
		mainPanel = new JPanel();
		GroupLayout layout = new GroupLayout(mainPanel);
		mainPanel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup horizGroup = layout.createSequentialGroup();
		horizGroup.addGroup(layout.createParallelGroup().addComponent(titleLabel).addComponent(releaseDateLabel).addComponent(genreLabel).addComponent(stockLabel)
														.addComponent(sequalLabel).addComponent(platformLabel).addComponent(versionLabel).addComponent(castLabel).addComponent(awardLabel));
		horizGroup.addGroup(layout.createParallelGroup().addComponent(titleField).addComponent(monthBox).addComponent(genreBox).addComponent(stockField)
														.addComponent(sequalIdField).addComponent(platformBox).addComponent(versionField).addComponent(castComboBox).addComponent(awardComboBox));
		horizGroup.addGroup(layout.createParallelGroup().addComponent(dayBox).addComponent(castButton).addComponent(awardButton));
		horizGroup.addGroup(layout.createParallelGroup().addComponent(yearBox).addComponent(removeCastButton).addComponent(removeAwardButton));
		
		layout.setHorizontalGroup(horizGroup);

		GroupLayout.SequentialGroup vertGroup = layout.createSequentialGroup();
		vertGroup.addGroup(layout.createParallelGroup().addComponent(titleLabel).addComponent(titleField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(releaseDateLabel).addComponent(monthBox).addComponent(dayBox).addComponent(yearBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(genreLabel).addComponent(genreBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(stockLabel).addComponent(stockField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(sequalLabel).addComponent(sequalIdField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(platformLabel).addComponent(platformBox));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(versionLabel).addComponent(versionField));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(castLabel).addComponent(castComboBox).addComponent(castButton).addComponent(removeCastButton));
		vertGroup.addGroup(layout.createParallelGroup().addComponent(awardLabel).addComponent(awardComboBox).addComponent(awardButton).addComponent(removeAwardButton));
//		vertGroup.addGroup(layout.createParallelGroup().addComponent(castListLabel));
		layout.setVerticalGroup(vertGroup);
		
		dataPanel.add(castListLabel);
		dataPanel.add(awardListLabel);
		
		superPanel.add(mainPanel);
		superPanel.add(dataPanel, BorderLayout.SOUTH);
		
		buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(closeButton);
		
		add(superPanel);
		add(buttonPanel, BorderLayout.SOUTH);
		setSize(550, 415);
		if (e == null)
			setTitle("[Admin] Add Inventory");
		else
			setTitle("[Admin] Update Inventory");
		add(buttonPanel, BorderLayout.SOUTH);
		setModal(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("CLOSE")){
			dispose();
		}
		
		else if (e.getActionCommand().equals("ADD")){
			Entertainment entertainment = new Entertainment();
			int sequalId = 0;
			if (!sequalIdField.getText().trim().equals(""))
				sequalId = Integer.parseInt(sequalIdField.getText().trim());
			String releaseDate = monthBox.getSelectedItem().toString() + " " + dayBox.getSelectedItem().toString() + " " + yearBox.getSelectedItem().toString();
			String resultMessage = entertainment.add(titleField.getText().trim(), releaseDate, genreBox.getSelectedItem().toString(), Integer.parseInt(stockField.getText().trim()),
					sequalId, platformBox.getSelectedItem().toString(), versionField.getText().trim());
			entertainment.addCastMembers(castList);
			JOptionPane.showMessageDialog(this, resultMessage);
		}
		
		else if (e.getActionCommand().equals("SAVE")){
			try {
				Entertainment entertainment = new Entertainment(this.currentEntertainment.eid);
				String result = "";
				if (!String.valueOf(currentEntertainment.getNumInStock()).equals(stockField.getText().trim())){
					result += entertainment.changeNumOfStock(Integer.parseInt(stockField.getText())) + " \n";
				}
				if (!titleField.getText().trim().equals(currentEntertainment.getTitle())){
					result += entertainment.changeTitle(titleField.getText().trim()) + " \n";
				}
				if (!monthBox.getSelectedItem().equals(currentEntertainment.getReleaseDate().split(" ")[0]) ||
						!dayBox.getSelectedItem().equals(currentEntertainment.getReleaseDate().split(" ")[1]) ||
						!yearBox.getSelectedItem().equals(currentEntertainment.getReleaseDate().split(" ")[2])){
					result += entertainment.changeReleaseDate(monthBox.getSelectedItem().toString() + " " + dayBox.getSelectedItem().toString() + " "
							+ yearBox.getSelectedItem().toString()) + " \n";
				}
				if (!genreBox.getSelectedItem().equals(currentEntertainment.getGenre())){
					result += entertainment.changeGenre(genreBox.getSelectedItem().toString()) + " \n";
				}
				if (!platformBox.getSelectedItem().equals(currentEntertainment.getPlatform())){
					result += entertainment.changePlatform(platformBox.getSelectedItem().toString()) + " \n";
				}
				if (!versionField.getText().trim().equals(currentEntertainment.getVersion())){
					result += entertainment.changeVersionNum(versionField.getText().trim()) + " \n";
				}
				if (!sequalIdField.getText().trim().equals(String.valueOf(currentEntertainment.getSequalID()))){
					result += entertainment.addSequal(Integer.parseInt(sequalIdField.getText().trim())) + " \n";
				}
				if (!previousCastList.equals(castListLabel.getText())){
					result += entertainment.addCastMembers(castList) + " \n";
				}
				if (!previousAwardList.equals(awardListLabel.getText())){
					result += entertainment.addAward(awardList) + " \n";
				}
					
				previousCastList = castListLabel.getText();
				previousAwardList = awardListLabel.getText();
				
				if (result.isEmpty())
					result = "No Changes Made.";

				JOptionPane.showMessageDialog(this, result);
			} catch (GetEntertainmentException e1) {
				e1.printStackTrace();
			}
		}
		
		else if (e.getActionCommand().equals("CAST")){
			String temp = (String)castComboBox.getSelectedItem();
			int castId = Integer.parseInt(temp.split(":")[0].trim());
			System.out.println("Cast ID: " + castId);
			CastMember tempCast = new CastMember(castId);
			
			boolean isAlreadyAdded = false;
			for (int i = 0; i < castList.size(); i++) {
				if (castList.get(i).getCID() == tempCast.getCID()){
					isAlreadyAdded = true;
					break;
				}
			}
			
			if (isAlreadyAdded){
				JOptionPane.showMessageDialog(this, "[Error]: Cast member is already added.");
			}
			
			else{
				castListLabel.setText(castListLabel.getText() + "\n" + tempCast.name);
				castList.add(tempCast);
			}
		}
		
		else if (e.getActionCommand().equals("REMOVE_CAST")){
			System.out.println("Remove cast.");
			String temp = (String)castComboBox.getSelectedItem();
			int castId = Integer.parseInt(temp.split(":")[0].trim());
			for (int i = 0; i < castList.size(); i++) {
				System.out.println(castId + " : " + castList.get(i).getCID());
				if (castId == castList.get(i).getCID()){
					String[] split = castListLabel.getText().split(castList.get(i).getName());
					if (split.length > 1)
						castListLabel.setText(split[0] + split[1]);
					else
						castListLabel.setText(split[0]);
					castList.remove(i);
					break;
				}
			}
		}
		
		else if (e.getActionCommand().equals("AWARD")){
			String temp = (String)awardComboBox.getSelectedItem();
			int awardId = Integer.parseInt(temp.split(":")[0].trim());
			String awardTitle = temp.split(":")[1].trim();
			Award tempAward = new Award(awardId, awardTitle);
			
			boolean isAlreadyAdded = false;
			for (int i = 0; i < awardList.size(); i++) {
				if (awardList.get(i).getAwardID() == tempAward.getAwardID()){
					isAlreadyAdded = true;
					break;
				}
			}
			
			if (isAlreadyAdded){
				JOptionPane.showMessageDialog(this, "[Error]: Award is already added.");
			}
			
			else{
				awardListLabel.setText(awardListLabel.getText() + "\n" + tempAward.getName());
				awardList.add(tempAward);
			}
		}
		
		else if (e.getActionCommand().equals("REMOVE_AWARD")){
			String temp = (String)awardComboBox.getSelectedItem();
			int awardId = Integer.parseInt(temp.split(":")[0].trim());
			for (int i = 0; i < awardList.size(); i++) {
				System.out.println(awardId + " : " + awardList.get(i).getAwardID());
				if (awardId == awardList.get(i).getAwardID()){
					String[] split = awardListLabel.getText().split(awardList.get(i).getName());
					if (split.length > 1)
						awardListLabel.setText(split[0] + split[1]);
					else
						awardListLabel.setText(split[0]);
					awardList.remove(i);
					break;
				}
			}
		}
	}

}
