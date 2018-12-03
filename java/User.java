

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{	
			statement = connection.prepareStatement("SELECT * "
					+ "FROM Users U "
					+ "INNER JOIN Address A ON U.aid = A.aid "
					+ "WHERE user_email = ?");

	        statement.clearParameters();
	        statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
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
	        	 subPlan = (int)resultSet.getObject(7);
	        	 
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
	
	public String getPass(){
		Statement statement;
		String pass = null;
		
		try
		{
			connection = connect.connect();
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT U.pass "
					+ "FROM Users U "
					+ "WHERE user_email = '" + email + "'");
			
	         // process query results
			if(resultSet.next()){
				pass = (String)resultSet.getObject(1);				
			}

			resultSet.close();
	        statement.close();
			connect.disconnect(connection);
			return pass;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return pass;
		}
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
	
	/*void rent(Entertainment e)
	 * Allows a user to rent a piece of entertainment
	 * Will throw a RentException if the user cannot rent the
	 * object for some reason
	 */
	public String rent(int eid) throws RentException
	{
		PreparedStatement statement;
		long count;
		int quota=0;
		Entertainment e = null;
		try {
			e = new Entertainment(eid);
		} catch (GetEntertainmentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Could not find Entertainment with ID: " + eid;
		}

		Calendar calendar;
		Date now;
		Timestamp currentTimestamp;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("SELECT total_quota FROM Sub_Plan WHERE level_id = ?");
			statement.clearParameters();
			System.out.println(this.subPlan);
			statement.setInt(1, this.subPlan);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next()){
				quota = resultSet.getInt("total_quota");
				System.out.println("Quote: " + quota);
			}
			else{
				System.out.println("Failed to get quota.");
			}
				
			statement = connection.prepareStatement("SELECT COUNT(*) AS numRented "
					+ "FROM (SELECT * "
					+ "FROM sys.Rent_History R "
					+ "WHERE R.time_returned IS NULL AND R.user_email = ?) R2 "
					+ "GROUP BY R2.user_email");
			
			statement.clearParameters();
			statement.setString(1, this.email);
			
			resultSet = statement.executeQuery();
					
			if(resultSet.next())
			{
				count = resultSet.getLong("numRented");

				if(count < quota)
				{
					calendar = Calendar.getInstance();
					now = calendar.getTime();
					currentTimestamp = new Timestamp(now.getTime());
					
					statement = connection.prepareStatement("INSERT INTO Rent_History VALUES(0,?,?,?,null)");
					
					statement.clearParameters();
					statement.setInt(1, e.getEID());
					statement.setString(2, this.email);
					statement.setTimestamp(3, currentTimestamp);
					
					statement.executeUpdate();
					
					 e.removeOneFromStock();
				}
				else{
					connect.disconnect(connection);
					return "You've reached your maximun amount of rentals.";
				}
					
			}
			else
			{
				calendar = Calendar.getInstance();
				now = calendar.getTime();
				currentTimestamp = new Timestamp(now.getTime());
				
				statement = connection.prepareStatement("INSERT INTO Rent_History VALUES(0,?,?,?,'0000-00-00 00:00:00')");
				
				statement.clearParameters();
				statement.setInt(1, e.getEID());
				statement.setString(2, this.email);
				statement.setTimestamp(3, currentTimestamp);
				
				statement.executeUpdate();
				
				e.removeOneFromStock();
			}
			
			statement.close();
			resultSet.close();
			connect.disconnect(connection);
			return e.title + " was rented.";
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return "Failed to rent entertainment with ID: " + eid;
		}
	}	
	
	public boolean changeName(String name)
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
			return true;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return false;
		}
	}
	
	public boolean changeUserLvl(int userLvl)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET level_id = '" + userLvl + "' WHERE user_email = '" + email + "'");

	        statement.close();
			connect.disconnect(connection);
			return true;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return false;
		}
	}
	
	public boolean changePassword(String pass)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Users SET pass = '" + pass + "' WHERE user_email = '" + email + "'");

	        statement.close();
			connect.disconnect(connection);
			return true;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return false;
		}
	}
	
	public boolean changePhone(String phone)
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
			return true;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);	
			return false;
		}
	}
	
	public boolean changeAddress(String street, String city, String state, int zip)
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
				statement.executeUpdate("UPDATE Users SET aid = " + aid + " WHERE user_email = '" + email + "'");
				
				this.street = street;
				this.city = city;
				this.state = state;
				this.zip = zip;
				
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
				return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return false;
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
	
	static public DefaultTableModel getArrayListOfAllItems()
	{
		Statement statement;
		Connect connect = new Connect();
		Connection connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT U.user_email AS 'Email', U.name AS 'Name', U.phone AS 'Phone', A.street AS 'Street',"
					+ "A.city AS 'City', A.state AS 'State', A.zip AS 'Zip' " + 
										 "FROM Users U " + 
										 "INNER JOIN Address A on U.aid = A.aid");
			
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
	
	public String removeUser()
	{
		Statement statement;
		try
		{
			connection = connect.connect();
			
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Users WHERE user_email ='" + email  + "'");
			
			statement.close();
			connect.disconnect(connection);
			return "Succesfully removed user: " + this.email;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return "Failed to remove user: " + this.email;
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
