package application;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class User 
{
	Connect connect;
	
	Connection connection;
	
	String email;
	String name;
	String phone;
	String address;
	
	//old user logging in
	
	public User()
	{
		connect = new Connect();
	}
	
	public User(String email, String name, String phone, String address)
	{
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.address = address;
	}
	
	public User(String email, String pass) throws LoginException//char[] pass)
	{
		connect = new Connect();
		login(email, pass);
	}
	
	//new user creating account
	public User(String email, String password, String name, String phone, String address) throws LoginException
	{
		connect = new Connect();
		if(!createUser(email, password, name, phone, address))
		{
			throw new LoginException("User already exists");
		}
		else
		{
			this.email = email;
			this.name = name;
			this.phone = phone;
			this.address = address;
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
	
	public boolean createUser(String email, String password, String name, String phone, String address)
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
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + 0 + "," + 1 + ")");
					
					this.email = email;
					this.name = name;
					this.phone = phone;
					this.address = address;
					
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
							+ "('" + email + "','" + name + "','" + password + "'," + phone + "," + aid +  "," + 0 + "," + 1 + ")");
					
					this.email = email;
					this.name = name;
					this.phone = phone;
					this.address = address;
					
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
	
	public ArrayList<User> getArrayListOfAllItems()
	{
		ArrayList<User> list = new ArrayList<User>();
		String address;
		Statement statement;
		
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
				
				list.add(new User((String)resultSet.getObject(1), (String)resultSet.getObject(2), (String)resultSet.getObject(4), address));
			}
			
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
}
