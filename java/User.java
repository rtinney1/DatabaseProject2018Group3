

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
	String street;
	String city;
	String state;
	int zip;
	int subPlan;
	
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
	
	public User(String email, String name, String phone, String street, String city, String state, int zip, boolean admin)
	{
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.admin = admin;
	}
	
	public User(String email, String pass) throws LoginException//char[] pass)
	{
		connect = new Connect();
		login(email, pass);
	}
	
	//new user creating account
	public User(String email, String password, String name, String phone, String street, String city, String state, int zip, boolean admin) throws LoginException
	{
		connect = new Connect();
		if(createUser(email, password, name, phone, street, city, state, zip, admin) == null)
		{
			System.out.println("Failed to login");
			throw new LoginException("User already exists");
		}
		else
		{
			this.email = email;
			this.name = name;
			this.phone = phone;
			this.street = street;
			this.city = city;
			this.state = state;
			this.zip = zip;
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
	        	 street = (String)resultSet.getObject(11);
	        	 city = (String)resultSet.getObject(10);
	        	 state = (String)resultSet.getObject(9);
	        	 zip = (int)resultSet.getObject(12);
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
	        	 street = (String)resultSet.getObject(11);
	        	 city = (String)resultSet.getObject(10);
	        	 state = (String)resultSet.getObject(9);
	        	 zip = (int)resultSet.getObject(12);
	        	 admin = (boolean)resultSet.getObject(6);
	        	 
	        	 System.out.println("" + this.email + " " + name + " " + phone + " " + street + " " + city + " " + state + " " + zip);
	        	 
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
	
	public User createUser(String email, String password, String name, String phone, String street, String city, String state, int zip, boolean a)
	{
		Statement statement;
		int aid = 0;

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
				System.out.println("User already exists");
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
				return null;
			}
			else
			{	
				statement = connection.createStatement();
				resultSet = statement.executeQuery("SELECT aid FROM Address "
						+ "WHERE state = '" + state + "' AND city = '" + city + "' AND street = '" + street + "' AND zip = " + zip);
				
				if(resultSet.next())
				{
					aid = (int)resultSet.getObject(1);
					
					System.out.println("Address ID: " + aid);
					
					statement.executeUpdate("INSERT INTO Users VALUES"
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + a + "," + 1 + ")");
					
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
					
					System.out.println("New Address ID: " + aid);
					statement.executeUpdate("INSERT INTO Users VALUES"
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + a + "," + 1 + ")");
					
					resultSet.close();
			        statement.close();
					connect.disconnect(connection);
				}
				
				this.street = street;
				this.city = city;
				this.state = state;
				this.zip = zip;
				this.email = email;
				this.name = name;
				this.phone = phone;
				this.admin = a;
				
				return this;
			}
		}
		catch(Exception e)
		{
			System.out.println("Create user catch all...");
			connect.disconnect(connection);
			e.printStackTrace();
			return null;
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
	
	public String getStreet(){
		return street;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getState(){
		return state;
	}
	
	public int getZip(){
		return zip;
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
		System.out.println("email: " + email + " name: "+ name + " phone: " + phone + " street: " + street + " city: " + city + " state: " + state + " zip: " + zip);
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
	
	public void changeAddress(String street, String city, String state, int zip)
	{
		int aid = 0;
		
		Statement statement;
		
		connection = connect.connect();
		
		try
		{
			System.out.println(street + " " + city + " " + state + " " + zip);
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT aid FROM Address "
					+ "WHERE state = '" + state + "' AND city = '" + city + "' AND street = '" + street + "' AND zip = " + zip);
			
			if(resultSet.next())
			{
				aid = (int)resultSet.getObject(1);
				
				statement.executeUpdate("UPDATE Users SET aid = " + aid + " WHERE user_email = '" + email + "'");
				this.street = street;
				this.city = city;
				this.state = state;
				this.zip = zip;
				
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
				
				this.street = street;
				this.city = city;
				this.state = state;
				this.zip = zip;
				
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
				street = (String)resultSet.getObject(11);
				city = (String)resultSet.getObject(10);
				state = (String)resultSet.getObject(9);
				zip = (int)resultSet.getObject(12);
				
				if((int)resultSet.getObject(6) == 0)
					a = false;
				else
					a = true;
				
				list.add(new User((String)resultSet.getObject(1), (String)resultSet.getObject(2), (String)resultSet.getObject(4), street, city, state, zip, a));
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
			
			ResultSet resultSet = statement.executeQuery("SELECT E.eid AS 'ID', E.title AS 'Title', E.genre AS 'Genre', "
					+ "E.platform AS 'Platform', R.time_rented AS 'Rent Date', R.time_returned AS 'Return Date' FROM rent_history R "
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
		Statement statement;
		Calendar calendar;
		Date yesterday;
		Timestamp oneDayAgo;
		
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

		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT T.eid AS 'ID', T.title AS 'Title', T.count AS 'Num Rentals' "
					+ "FROM (SELECT E.eid, E.title, COUNT(R.eid) AS 'count' "
					+ "FROM rent_history R NATURAL JOIN entertainment E "
					+ "WHERE R.time_rented > '" + lastMonth + "' AND R.time_rented < '" + startThisMonth + "' "
					+ "GROUP BY E.eid) T "
					+ "ORDER BY T.count DESC LIMIT 10");
					
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
