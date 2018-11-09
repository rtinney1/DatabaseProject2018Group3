/*CastMember.java
 * Created by: Randi Tinney
 * Created On: Nov 4 2018
 * Updated On: Nov 7 2018
 * Description: CastMember.java creates an object of CastMember that stores
 * 		all of the information as it appears in the database. This class provides methods
 * 		on returning the information, changing all information before inserting into the
 * 		database, changing all information after inserting into the database, and inserting
 * 		the CastMember object into the database
 */

package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Pattern;

public class CastMember
{
	Connection connection;
	Connect connect;
	String name;
	String address;
	String addressToAddToDB;
	boolean director;
	int eid;
	int cid;
	
	/*Constructor that accesses the database with the passed
	 * cid and creates the CastMember from the result
	 */
	public CastMember(int cid)
	{
		connect = new Connect();
		connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("SELECT * FROM Cast_Members "
					+ "INNER JOIN Address A ON C.aid = A.aid "
					+ "INNER JOIN Worked_In W ON C.cid = W.cid WHERE cid = ?");
			statement.clearParameters();
			statement.setInt(1, cid);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				this.cid = cid;
				this.name = resultSet.getString("name");
				this.director = resultSet.getBoolean("is_director");
				this.address = resultSet.getString("street") + " " + resultSet.getString("city") + " "
						+ resultSet.getString("state") + " " + resultSet.getInt("zip");

				this.addressToAddToDB = resultSet.getString("street") + "$" + resultSet.getString("city") + "$"
						+ resultSet.getString("state") + "$" + resultSet.getInt("zip");
				
				this.eid = resultSet.getInt("eid");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*Constructor with all information except the cid passed
	 */
	public CastMember(String name, String street, String city, String state, int zip, boolean director)
	{
		connect= new Connect();
		
		this.name = name;
		this.address = street + " " + city + " " + state + " " + zip;
		this.addressToAddToDB = street + "$" + city + "$" + state + "$" + zip;
		this.director = director;
	}
	
	/*Constructor with all information
	 */
	public CastMember(int cid, String name, String street, String city, String state, int zip, boolean director)
	{
		connect= new Connect();
		
		this.cid = cid;
		this.name = name;
		this.address = street + " " + city + " " + state + " " + zip;
		this.addressToAddToDB = street + "$" + city + "$" + state + "$" + zip;
		this.director = director;
	}
	
	/*String getName()
	 * Returns the name of the CastMember
	 */
	public String getName()
	{
		return name;
	}
	
	/*String getAddress()
	 * Returns the address of the CastMember
	 */
	public String getAddress()
	{
		return address;
	}
	
	/*String getAddressDelim
	 * Returns the delimited address of the CastMember
	 */
	public String getAddressDelim()
	{
		return addressToAddToDB;
	}
		
	/*boolean getDirector()
	 * Returns whether the CastMember is a director or not
	 */
	public boolean getDirector()
	{
		return director;
	}
	
	/*int getCID()
	 * Returns the cid of the CastMember
	 */
	public int getCID()
	{
		return cid;
	}
	
	/*void changeDirectorBeforeAdd(boolean d)
	 * Changes whether the CastMember is a director or not
	 * before it is added to the database
	 */
	public void changeDirectorBeforeAdd(boolean d)
	{
		director = d;
	}
	
	/*void changeDirector(boolean d)
	 * Accesses the database to change whether the CastMember
	 * is a director or not
	 */
	public void changeDirector(boolean d)
	{
		connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("UPDATE Cast_Members SET director = ? WHERE cid = ?");
			
			statement.clearParameters();
			statement.setBoolean(1, d);
			statement.setInt(1, this.cid);
			
			statement.executeUpdate();
			
			director = d;
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*void changeAddressBeforeAdd(String street, String city, String state, int zip)
	 * Changes the address before the CastMember is added to the Database
	 */
	public void changeAddressBeforeAdd(String street, String city, String state, int zip)
	{
		address = street + " " + city + " " + state + " " + zip;
		addressToAddToDB = street + "$" + city + "$" + state + "$" + zip;
	}
	
	/*void changeAddress(String street, String city, String state, int zip)
	 * Accesses the database to change the address of the CastMember
	 */
	public void changeAddress(String street, String city, String state, int zip)
	{
		connection = connect.connect();
		PreparedStatement statement;
		int aid;
		
		try
		{
			statement = connection.prepareStatement("SELECT aid FROM Address "
					+ "WHERE state = ? AND city = ? AND street = ? AND zip = ?");
			
			statement.clearParameters();
			statement.setString(1, state);
			statement.setString(2, city);
			statement.setString(3, street);
			statement.setInt(4, zip);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				aid = resultSet.getInt("aid");
				
				statement.executeUpdate("UPDATE Cast_Members SET aid = " + aid + " WHERE cid =" + cid);
				
				resultSet.close();
				statement.close();
				connect.disconnect(connection);
			}
			else
			{
				statement = connection.prepareStatement("INSERT INTO Address VALUES (0,?,?,?,?)");
				statement.clearParameters();
				statement.setString(1, state);
				statement.setString(2, city);
				statement.setString(3, street);
				statement.setInt(3, zip);
				
				statement.executeUpdate();
				
				resultSet = statement.executeQuery("SELECT aid FROM Address");
				
				resultSet.last();
				
				aid = resultSet.getInt("aid");
				
				statement.executeUpdate("UPDATE Cast_Members SET aid = " + aid + " WHERE cid =" + cid);
				
				resultSet.close();
				statement.close();
				connect.disconnect(connection);
			}
			
			address = street + " " + city + " " + state + " " + zip;
			addressToAddToDB = street + "$" + city + "$" + state + "$" + zip;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}

	/*void changeNameBeforeAdd(String nn)
	 * Changes the name before the CastMember is added to the Database
	 */
	public void changeNameBeforeAdd(String nn)
	{
		name = nn;
	}
	
	/*void changeName(String nn)
	 * Accesses the database to change the name of the CastMember
	 */
	public void changeName(String nn)
	{
		connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("UPDATE Cast_Members SET name = ? WHERE cid = ?");
			
			statement.clearParameters();
			statement.setString(1, nn);
			statement.setInt(2, this.cid);
			
			statement.executeUpdate();
			
			name = nn;
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*void addToDB()
	 * Adds the CastMember to the database with the current information stored
	 */
	public void addToDB()
	{
		PreparedStatement statement;
		int aid;
		String[] address = addressToAddToDB.split(Pattern.quote("$"));
		String state = address[2];
		String city = address[1];
		String street = address[0];
		int zip = Integer.parseInt(address[3]);
		
		try
		{
			connection = connect.connect();
			
			statement = connection.prepareStatement("SELECT aid FROM Address "
					+ "WHERE state = ? AND city = ? AND street = ? AND zip = ?");
			statement.clearParameters();
			statement.setString(1, state);
			statement.setString(2, city);
			statement.setString(3, street);
			statement.setInt(4, zip);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				aid = resultSet.getInt("aid");
				
				statement = connection.prepareStatement("SELECT * FROM Cast_Member WHERE name = ? AND is_director = ?");
				statement.clearParameters();
				statement.setString(1, this.name);
				statement.setBoolean(2, this.director);
				resultSet = statement.executeQuery();
				
				if(resultSet.next())
				{
					cid = (int)resultSet.getObject(1);
					
					statement.executeUpdate("UPDATE Cast_Member SET aid =" + aid + " WHERE cid = " + cid);
				}
				else
				{
					statement = connection.prepareStatement("INSERT INTO Cast_Member VALUES (0,?,?,?)");
					statement.clearParameters();
					statement.setString(1, this.name);
					statement.setInt(2, aid);
					statement.setBoolean(3, this.director);
					
					statement.executeUpdate();
					
					resultSet = statement.executeQuery("SELECT * FROM Cast_Member");
					
					resultSet.last();
					
					cid = resultSet.getInt("cid");
				}
			}
			else
			{
				statement = connection.prepareStatement("INSERT INTO Address VALUES (0,?,?,?,?)");
				
				statement.clearParameters();
				statement.setString(1, state);
				statement.setString(2, city);
				statement.setString(3, street);
				statement.setInt(4, zip);
				
				statement.executeUpdate();
				
				resultSet = statement.executeQuery("SELECT aid FROM Address");
				
				resultSet.last();
				
				aid = resultSet.getInt("aid");
				
				statement = connection.prepareStatement("SELECT * FROM Cast_Member WHERE name = ? AND is_director = ?");
				statement.clearParameters();
				statement.setString(1, this.name);
				statement.setBoolean(2, this.director);
				resultSet = statement.executeQuery();
				
				if(resultSet.next())
				{
					cid = resultSet.getInt("cid");
					
					statement.executeUpdate("UPDATE Cast_Member SET aid =" + aid + " WHERE cid = " + cid);
				}
				else
				{
					statement = connection.prepareStatement("INSERT INTO Cast_Member VALUES (0,?,?,?)");
					statement.clearParameters();
					statement.setString(1, this.name);
					statement.setInt(2, aid);
					statement.setBoolean(3, this.director);
					
					statement.executeUpdate();
					
					resultSet = statement.executeQuery("SELECT * FROM Cast_Member");
					
					resultSet.last();
					
					cid = resultSet.getInt("cid");
				}
			}

			resultSet.close();
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