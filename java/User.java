

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

public class User 
{
	Connect connect;
	
	Connection connection;
	
	String email;
	String name;
	String phone;
	String address;
	
	boolean admin;
	
	//old user logging in
	
	public User()
	{
		connect = new Connect();
	}
	
	public User(String email)
	{
		connect = new Connect();
		
		get(email);
	}
	
	public User(String email, String name, String phone, String address, boolean admin)
	{
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.admin = admin;
	}
	
	public User(String email, String pass) throws LoginException//char[] pass)
	{
		connect = new Connect();
		login(email, pass);
	}
	
	//new user creating account
	public User(String email, String password, String name, String phone, String address, boolean admin) throws LoginException
	{
		connect = new Connect();
		if(!createUser(email, password, name, phone, address, admin))
		{
			throw new LoginException("User already exists");
		}
		else
		{
			this.email = email;
			this.name = name;
			this.phone = phone;
			this.address = address;
			this.admin = admin;
		}
	}
	
	public void get(String email)
	{
		Statement statement;
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * "
					+ "FROM Users U "
					+ "INNER JOIN Address A ON U.aid = A.aid "
					+ "WHERE user_email = '" + email + "'");
					
			if(resultSet.next())
	         {
				System.out.println("GETTING USER INFO");
	        	 this.email = (String)resultSet.getObject(1);
	        	 name = (String)resultSet.getObject(2);
	        	 phone = (String)resultSet.getObject(4);
	        	 address = (String)resultSet.getObject(11) + "$" + (String)resultSet.getObject(10) + "$" + (String)resultSet.getObject(9) + "$" + (int)resultSet.getObject(12);
	        
	        	 admin = (boolean)resultSet.getObject(6);
	        	 
		         resultSet.close();
		         statement.close();
		         connect.disconnect(connection);
	        	 //System.out.println(resultSet.getObject(1));
	         }
	         else
	         {
	        	 System.out.println("Error");
	        	 resultSet.close();
		         statement.close();
		         connect.disconnect(connection);
	         }
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
		}
	}
	public void login(String email, String pass) throws LoginException
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * "
					+ "FROM Users U "
					+ "INNER JOIN Address A on U.aid = A.aid "
					+ "WHERE user_email = '" + email + "' AND pass = '" + pass +"'");
	         // process query results
	         
	         if(resultSet.next())
	         {
	        	 this.email = (String)resultSet.getObject(1);
	        	 name = (String)resultSet.getObject(2);
	        	 phone = (String)resultSet.getObject(4);
	        	 address = (String)resultSet.getObject(11) + "$" + (String)resultSet.getObject(10) + "$" + (String)resultSet.getObject(9) + "$" + (int)resultSet.getObject(12);
	        
	        	 admin = (boolean)resultSet.getObject(6);
	        	 
	        	 System.out.println("" + this.email + " " + name + " " + phone + " " + address);
		         resultSet.close();
		         statement.close();
		         System.out.println("HELLO");
		         connect.disconnect(connection);
	        	 //System.out.println(resultSet.getObject(1));
	         }
	         else
	         {
	        	 //System.out.println("Incorrect username/password");
	        	 resultSet.close();
		         statement.close();
		         connect.disconnect(connection);
		         throw new LoginException("Invalid username/password");
	         }
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			throw new LoginException("Invalid username/password");
		}
	}
	
	public boolean createUser(String email, String password, String name, String phone, String address, boolean a)
	{
		Statement statement;
		String[] addressSplit;
		String street;
		String city;
		String state;
		int aid = 0;
		int zip;
		
		
		try
		{
			connection = connect.connect();
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * "
					+ "FROM Users U "
					+ "WHERE user_email = '" + email + "'");
	         // process query results
			if(resultSet.next())
			{
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
				return false;
			}
			else
			{
				
				addressSplit = address.split(Pattern.quote("$"));
				
				street = addressSplit[0];
				city = addressSplit[1];
				state = addressSplit[2];
				zip = Integer.parseInt(addressSplit[3]);
				
				statement = connection.createStatement();
				
				resultSet = statement.executeQuery("SELECT aid FROM Address "
						+ "WHERE state = '" + state + "' AND city = '" + city + "' AND street = '" + street + "' AND zip = " + zip);
				
				if(resultSet.next())
				{
					aid = (int)resultSet.getObject(1);
					
					System.out.println("" + aid);
					
					statement.executeUpdate("INSERT INTO Users VALUES"
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + a + "," + 1 + ")");
					
					this.email = email;
					this.name = name;
					this.phone = phone;
					this.address = address;
					this.admin = a;
					
					resultSet.close();
			        statement.close();
					connect.disconnect(connection);
					return true;
				}
				else
				{
					statement.executeUpdate("INSERT INTO Address VALUES"
							+ "(" + 0 + ",'" + state + "','" + city + "','" + street + "'," + zip + ")");
					
					resultSet = statement.executeQuery("SELECT aid FROM Address");
					
					resultSet.last();
					
					aid = (int)resultSet.getObject(1);
					
					System.out.println("" + aid);
					statement.executeUpdate("INSERT INTO Users VALUES"
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + a + "," + 1 + ")");
					
					this.email = email;
					this.name = name;
					this.phone = phone;
					this.address = address;
					this.admin = a;
					
					resultSet.close();
			        statement.close();
					connect.disconnect(connection);
					return true;
				}
			}
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return false;
		}
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public boolean getAdminLvl()
	{
		return admin;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public int getUserLvl()
	{
		Statement statement;
		int userLvl = -1;
		
		try
		{
			connection = connect.connect();
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT U.level_id "
					+ "FROM Users U "
					+ "WHERE user_email = '" + email + "'");
	         // process query results
			if(resultSet.next())
			{
				userLvl = (int)resultSet.getObject(1);
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
				return userLvl;
			}
			else
			{
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
				return userLvl;
			}
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return userLvl;
		}
	}
	
	public void printUserInfo()
	{
		System.out.println("email: " + email + " name: "+ name + " phone: " + phone + " address: " + address);
	}
	
	public void changeName(String name)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET name = '" + name + "' WHERE user_email = '" + email + "'");
			
			this.name = name;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeUserLvl(int userLvl)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET level_id = '" + userLvl + "' WHERE user_email = '" + email + "'");

	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changePassword(String pass)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET pass = '" + pass + "' WHERE user_email = '" + email + "'");

	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changePhone(String phone)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET phone = " + phone + " WHERE user_email = '" + email + "'");
			
			this.phone = phone;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeAddress(String address)
	{
		String[] addressSplit;
		String street;
		String city;
		String state;
		int aid = 0;
		int zip;
		
		Statement statement;
		
		connection = connect.connect();
		
		try
		{
			addressSplit = address.split(Pattern.quote("$"));
			
			street = addressSplit[0];
			city = addressSplit[1];
			state = addressSplit[2];
			zip = Integer.parseInt(addressSplit[3]);
			
			System.out.println(street + " " + city + " " + state + " " + zip);
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT aid FROM Address "
					+ "WHERE state = '" + state + "' AND city = '" + city + "' AND street = '" + street + "' AND zip = " + zip);
			
			if(resultSet.next())
			{
				aid = (int)resultSet.getObject(1);
				
				statement.executeUpdate("UPDATE Users SET aid = " + aid + " WHERE user_email = '" + email + "'");
				this.address = address;
				
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
			}
			else
			{
				statement.executeUpdate("INSERT INTO Address VALUES"
						+ "(" + 0 + ",'" + state + "','" + city + "','" + street + "'," + zip + ")");
				
				resultSet = statement.executeQuery("SELECT aid FROM Address");
				
				resultSet.last();
				
				aid = (int)resultSet.getObject(1);
				
				System.out.println("" + aid);
				statement.executeUpdate("UPDATE Users SET aid = " + aid + "WHERE user_email = '" + email + "'");
				
				this.address = address;
				
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	public void changeAdminLvl(boolean a)
	{
		Statement statement;
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET is_admin = '" + a + "' WHERE user_email = '" + email + "'");

	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	public ArrayList<User> getArrayListOfAllItems()
	{
		ArrayList<User> list = new ArrayList<User>();
		String address;
		Statement statement;
		boolean a;
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT *" + 
										 "FROM Users U " + 
										 "INNER JOIN Address A on U.aid = A.aid");
			while(resultSet.next())
			{
				address = (String)resultSet.getObject(11) + "$" + (String)resultSet.getObject(10) + "$" + 
						(String)resultSet.getObject(9) + "$" + (String)resultSet.getObject(12);
				
				if((int)resultSet.getObject(6) == 0)
					a = false;
				else
					a = true;
				
				list.add(new User((String)resultSet.getObject(1), (String)resultSet.getObject(2), (String)resultSet.getObject(4), address, a));
			}
			
			resultSet.close();
			statement.close();
			connect.disconnect(connection);
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return list;
		}
	}
	
	public void removeUser()
	{
		Statement statement;
		try
		{
			connection = connect.connect();
			
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Users WHERE user_email ='" + email  + "'");
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	public DefaultTableModel getRentHistory()
	{
		Statement statement;
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT E.eid, E.title, E.genre, E.platform, R.time_rented, R.time_returned FROM rent_history R "
					+ "INNER JOIN Entertainment E ON R.eid = E.eid "
					+ "INNER JOIN Users U ON R.user_email = U.user_email "
					+ "WHERE R.user_email = '" + email + "'");
			
			DefaultTableModel tableModel = TableModelUtil.buildTableModel(resultSet);
			
			resultSet.close();
			statement.close();
			connect.disconnect(connection);
			return tableModel;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return null;
		}
	}
	
	public void returnEntertainment(RentHistory x)
	{
		Statement statement;
		connection = connect.connect();
		Calendar calendar;
		Date now;
		Timestamp currentTimestamp;
		
		try
		{
			Entertainment e = new Entertainment(x.getEID());
			
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM rent_history R "
					+ "WHERE R.rid = " + x.getRID());
			
			if(resultSet.next())
			{
				calendar = Calendar.getInstance();
				now = calendar.getTime();
				currentTimestamp = new Timestamp(now.getTime());
				
				statement.executeUpdate("UPDATE rent_history SET time_returned = '" + currentTimestamp + 
						"' WHERE rid = " + x.getRID());
				
				e.addOneToStock();
			}
			
			statement.close();
			resultSet.close();
			connect.disconnect(connection);
		}
		catch(GetEntertainmentException ge)
		{
			ge.printStackTrace();
			connect.disconnect(connection);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	public DefaultTableModel adminGetLast24Hours()
	{
		ArrayList<RentHistory> list = new ArrayList<RentHistory>();
		Statement statement;
		Calendar calendar;
		Date yesterday;
		Timestamp oneDayAgo;
//		Timestamp rentDate;
		
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		yesterday = calendar.getTime();
		oneDayAgo = new Timestamp(yesterday.getTime());
		
		System.out.println(yesterday.toString() + " $$$ " + oneDayAgo.toString());
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT R.rid AS 'Rent ID', E.title AS 'Title', U.user_email AS 'Member', A.street AS 'Street', "
					+ "A.city AS 'City', A.state AS 'State', A.zip AS 'Zip Code', R.time_rented AS 'Rent Date', R.time_returned AS 'Return Date' "
					+ "FROM rent_history R NATURAL JOIN entertainment E NATURAL JOIN users U NATURAL JOIN address A "
					+ "WHERE R.time_rented > '" + oneDayAgo + "'");
			
//			while(resultSet.next())
//			{
//				rentDate = (Timestamp)resultSet.getObject(4);
//				
//				System.out.println("INSIDE GETTING LAST 24");
//				
//				System.out.println((String)resultSet.getObject(3));
//				
//				list.add(new RentHistory((int)resultSet.getObject(2), (int)resultSet.getObject(1), (String)resultSet.getObject(7),
//						(String)resultSet.getObject(3), rentDate));
//			}
			DefaultTableModel tableModel = TableModelUtil.buildTableModel(resultSet);
			resultSet.close();
			statement.close();
			connect.disconnect(connection);
			return tableModel;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return null;
		}
	}
	
	public DefaultTableModel adminGetTop10LastMonth()
	{
		ArrayList<RentHistory> list = new ArrayList<RentHistory>();
		Statement statement;
		Calendar calendar1;
		Calendar calendar2;
		Date firstDayLastMonth;
		Date firstDayThisMonth;
		Timestamp lastMonth;
		Timestamp startThisMonth;
		
		calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		firstDayLastMonth = calendar1.getTime();
		lastMonth = new Timestamp(firstDayLastMonth.getTime());
		
		calendar2 = Calendar.getInstance();
		calendar2.set(Calendar.DAY_OF_MONTH, 1);
		firstDayThisMonth = calendar2.getTime();
		startThisMonth = new Timestamp(firstDayThisMonth.getTime());
		
		System.out.println("LastMonth: " + lastMonth.toString() + " ThisMonth: " + startThisMonth.toString());

		long lowestCount = 999999999;
		int pos;
		int i;
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT T.eid AS 'ID', T.title AS 'Title', T.count AS 'Num Rentals' "
					+ "FROM (SELECT E.eid, E.title, R.rid, COUNT(R.eid) AS 'count' "
					+ "FROM rent_history R NATURAL JOIN entertainment E "
					+ "WHERE R.time_rented > '" + lastMonth + "' AND R.time_rented < '" + startThisMonth + "' "
					+ "GROUP BY E.title) T "
					+ "ORDER BY T.count DESC LIMIT 5");
			
//			while(resultSet.next())
//			{				
//				list.add(new RentHistory((int)resultSet.getObject(1), (String)resultSet.getObject(2), 
//						(int)resultSet.getObject(3), (long)resultSet.getObject(4)));
//			}
//			
//			while(list.size() > 10)
//			{
//				for(i = 0; i < list.size(); i++)
//				{
//					if(list.get(i).getCount() < lowestCount)
//					{
//						lowestCount = list.get(i).getCount();
//						pos = i;
//					}
//				}
//					
//				list.remove(i);
//			}
//			
			DefaultTableModel tableModel = TableModelUtil.buildTableModel(resultSet);
			resultSet.close();
			statement.close();
			connect.disconnect(connection);
			return tableModel;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return null;
		}
	}
}
