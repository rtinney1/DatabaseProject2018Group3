import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JOptionPane;

class MyInputVerifier extends InputVerifier
{

//=====================================

public boolean verify(JComponent c)
{
	return true;
}//end verify() method

//=====================================

public static boolean nameVerification(JComponent t, JComponent l)
{
	String inString;
	JTextField tf;
	JLabel label;

	label = (JLabel)l;
	tf = (JTextField)t;
	inString = tf.getText().trim();

	if (inString.equals(""))
	{
		label.setForeground(Color.RED);
		JOptionPane.showMessageDialog(null, "Please input a valid name. (Last Name, First Name)", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
		t.requestFocus();

		return false;
	}

	label.setForeground(Color.BLACK);
	return true;

}//end nameVerification() method

//=====================================

	public static boolean passwordVerifier(JComponent ta, JComponent la, JComponent tb, JComponent lb)
	{
		String inString_1, inString_2;
		JPasswordField pw_1, pw_2;
		JLabel label_1, label_2;
		
		label_1 = (JLabel)la;
		label_2 = (JLabel)lb;
		pw_1 = (JPasswordField)ta;
		pw_2 = (JPasswordField)tb;
		inString_1 =new String( pw_1.getPassword());
		inString_2 = new String(pw_2.getPassword());
		
		if (inString_1.equals(""))
		{
			label_1.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please input a valid password.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			
			if (inString_2.equals(""))
			{
				label_2.setForeground(Color.RED);
				JOptionPane.showMessageDialog(null, "Please re-enter your password.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			}
			ta.requestFocus();
			return false;
		}
		
		label_1.setForeground(Color.BLACK);
		
		if (inString_2.equals(""))
		{
			label_2.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please re-enter your password.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			tb.requestFocus();
			
			return false;
		}
		
		if (!inString_1.equals(inString_2))
		{
			label_1.setForeground(Color.RED);
			label_2.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Passwords do not match.\nPlease try again.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			ta.requestFocus();
			
			return false;
		}
		
		label_1.setForeground(Color.BLACK);
		label_2.setForeground(Color.BLACK);
		
		return true;
		
	}//end passwordVerifier()
	
	//=====================================
	
	public static boolean emailVerifier(JComponent t, JComponent l)
	{
		String inString;
		JTextField tf;
		JLabel label;
		
		label = (JLabel)l;
		tf = (JTextField)t;
		inString = tf.getText().trim();

		if (!inString.contains("@") || inString.equals(""))
		{
			label.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please input a valid email.\nProper email form is: name@host.com", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			t.requestFocus();

			return false;
		}

		label.setForeground(Color.BLACK);
		return true;
	}
	
	//=====================================
	
	public static boolean addressVerifier(JComponent af, JComponent al, JComponent cf, JComponent cl, 
																JComponent sf, JComponent sl, JComponent zf, JComponent zl)
	{
		JTextField aField, cField, zField;
		JComboBox sBox;
		JLabel aLabel, cLabel, sLabel, zLabel;
		String inString_1, inString_2, inString_3, inString_4;
		int zipCode;
		
		aField = (JTextField)af;
		cField = (JTextField)cf;
		zField = (JTextField)zf;
		sBox = (JComboBox)sf;
		
		aLabel = (JLabel)al;
		cLabel = (JLabel)cl;
		sLabel = (JLabel)sl;
		zLabel = (JLabel)zl;
		
		inString_1 = aField.getText().trim();
		inString_2 = cField.getText().trim();
		inString_3 = zField.getText().trim();
		inString_4 = (String) sBox.getSelectedItem();
				
		if (inString_1.equals(""))
		{
			aLabel.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please enter a valid street address or PO Box.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			af.requestFocus();
			
			return false;
		}
	
		aLabel.setForeground(Color.BLACK);
		
		if (inString_2.equals(""))
		{
			cLabel.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please enter a valid city", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			cf.requestFocus();
			
			return false;
		}
		
		cLabel.setForeground(Color.BLACK);
		
		if (inString_4.equals("-Choose Your State-"))
		{
			sLabel.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please choose a state.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			sf.requestFocus();
			
			return false;
		}
		
		sLabel.setForeground(Color.BLACK);
		
		if (inString_3.equals(""))
		{
			zLabel.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please enter a valid zip code.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			zf.requestFocus();
			
			return false;
		}
		
		zLabel.setForeground(Color.BLACK);
		
		try
		{
			zipCode = Integer.parseInt(inString_3);
			if (zipCode >99999 || zipCode <10000)
			{
				zLabel.setForeground(Color.RED);
				JOptionPane.showMessageDialog(null, "Please input a valid zip code.", "INVALID INPUT!", JOptionPane.ERROR_MESSAGE);
				zf.requestFocus();
				return false;
			}

		}// end try
		catch(NumberFormatException e)
		{
			zLabel.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please input a valid zip code.", "INVALID INPUT!", JOptionPane.ERROR_MESSAGE);
			zf.requestFocus();
			return false;
			
		}// end catch
		
		aLabel.setForeground(Color.BLACK);
		cLabel.setForeground(Color.BLACK);
		sLabel.setForeground(Color.BLACK);
		zLabel.setForeground(Color.BLACK);
		
		return true;
		
	}//end addressVerifier()
	
	//=====================================
	
	public static boolean phoneVerifier(JComponent pf, JComponent pl)
	{
		String inString;
		JTextField tf;
		JLabel label;
		
		tf = (JTextField)pf;
		label = (JLabel)pl;
		inString = tf.getText().trim();
		
		if (inString.equals(""))
		{
			label.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please input a valid phone number", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			pf.requestFocus();

			return false;
		}

		label.setForeground(Color.BLACK);
		return true;
		
	}//end phoneVerifier()
	
	//=====================================
	
	public static boolean planVerifier(JComponent pf, JComponent pl)
	{
		JComboBox pBox;
		JLabel label;
		String inString;
		
		pBox = (JComboBox)pf;
		
		label = (JLabel)pl;
		inString = (String) pBox.getSelectedItem();
		
		if (inString.equals("-Please Selecta Plan-"))
		{
			label.setForeground(Color.RED);
			JOptionPane.showMessageDialog(null, "Please choose a rental plan.", "INVALID INPUT", JOptionPane.ERROR_MESSAGE);
			pf.requestFocus();
			
			return false;
		}
		
		label.setForeground(Color.BLACK);
		return true;
	
	}//end planVerifier()
//+++++++++++++++++++++++++++++++++++++
	
}//end MyInputVerifier class
