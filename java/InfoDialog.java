/*InfoDialogt.java
 * Created by: Randi Tinney
 * Created On: Dec 3 2018
 * Description: InfoDialog.java provides a JDialog that shows all of the information of the chosen
 * 			media.
 */

package application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class InfoDialog extends JDialog implements ActionListener
{
	DefaultListModel<String> dlm;
	JList<String>            list;
	JScrollPane      		 scrollpane;
	Entertainment    		 e;
	
	public InfoDialog(JFrame frame, int eid)
	{
		super(frame, "Info", ModalityType.APPLICATION_MODAL);
		
		try
		{
			e = new Entertainment(eid);
			
			dlm = new DefaultListModel<String>();
			populate();
			list = new JList<String>(dlm);
			scrollpane = new JScrollPane(list);
			
			this.add(scrollpane, BorderLayout.CENTER);

			this.setSize(400, 300);
			this.setVisible(true);
		}
		catch(GetEntertainmentException e)
		{
			System.out.println("error");
		}
	}
	
	/*populate()
	 Populates the JList with all of the information of the chosen form of entertainment
	 */
	private void populate()
	{
		ArrayList<CastMember> cal;
		ArrayList<Award> aal;
		ArrayList<Entertainment> eal;
		
		dlm.addElement("Title: ");
		dlm.addElement(e.getTitle());
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Type: ");
		dlm.addElement(e.getType());
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Genre:");
		dlm.addElement(e.getGenre());
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Release Date:");
		dlm.addElement(e.getReleaseDate());
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Version Number:");
		dlm.addElement(e.getVersion());
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Platform:");
		dlm.addElement(e.getPlatform());
		
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Cast Members:");
		cal = e.getCastMembers();
		
		for(int i = 0; i < cal.size(); i++)
		{
			if(cal.get(i).getDirector())
				dlm.addElement("Producer: " + cal.get(i).getName() + " " + cal.get(i).getAddress());
			else
				dlm.addElement(cal.get(i).getName() + " " + cal.get(i).getAddress());
		}
		
		aal = e.getAwardsWon();
		
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("Awards Won:");

		for(int i = 0; i< aal.size(); i++)
			dlm.addElement(aal.get(i).getName());
		 
		eal = e.getSequalList();
		
		dlm.addElement("");
		dlm.addElement("");
		dlm.addElement("List of Sequels:");
		
		for(int i = 0; i < eal.size(); i++)
			dlm.addElement(eal.get(i).getTitle());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
