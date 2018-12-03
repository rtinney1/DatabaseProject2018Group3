/*Award.java
 * Created by: Randi Tinney
 * Created On: Nov 5 2018
 * Updated On: Nov 7 2018
 * Description: Award.java is a class that creates Award objects and stores
 * the name and awardID. This class provides methods for getting the information
 * and changing the information within the database of each award. Also allows
 * the front end to change the name of the database before the award is added to 
 * the database
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Award
{
	Connect connect;
	Connection connection;
	String name;
	int awardID;
	
	/*Constructor with id and name
	 */
	public Award(int id, String name)
	{
		awardID = id;
		this.name = name;
	}
	
	/*Constructor with name
	 */
	public Award(String name)
	{
		connect = new Connect();
		this.name = name;
	}
	
	public Award() {
		// TODO Auto-generated constructor stub
	}

	/*String getName()
	 * Returns the name of the award
	 */
	public String getName()
	{
		return name;
	}
	
	/*int getAwardID()
	 * Returns the awardID of the award
	 */
	public int getAwardID()
	{
		return awardID;
	}
	
	/*void changeNameBeforeAddtoDB(String n)
	 * Changes the name of the award before the award
	 * is added to the database
	 */
	public void changeNameBeforeAddtoDB(String n)
	{
		name = n;
	}
	
	/*void changeName(String n)
	 * Accesses the database to change the name of the award
	 */
	public void changeName(String n)
	{
		connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("UPDATE Awards SET title = ? WHERE awardID =?");
			statement.clearParameters();
			statement.setString(1, n);
			statement.setInt(2, this.awardID);
			
			statement.executeUpdate();
			
			name = n;
			
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
	 * Adds the award to the database
	 */
	public void addToDB()
	{
		connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("SELECT * FROM Awards WHERE title = ?");
			statement.clearParameters();
			statement.setString(1, name);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				awardID = resultSet.getInt("awardID");
			}
			else
			{
				statement = connection.prepareStatement("INSERT INTO Awards VALUES (0, ?)");
				statement.clearParameters();
				statement.setString(1, name);
				statement.executeUpdate();
				resultSet = statement.executeQuery("SELECT * FROM Awards");
				resultSet.last();
				awardID = resultSet.getInt("awardID");
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
	
	public static ArrayList<Award> getAllAwards(){
		ArrayList<Award> awardArray = new ArrayList<>();
		Connect connect = new Connect();
		Connection connection = connect.connect();
		PreparedStatement statement;
		
		try
		{
			statement = connection.prepareStatement("SELECT * FROM Awards");
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next())
			{
				int awardId = resultSet.getInt("awardID");
				String name = resultSet.getString("title");
				Award tempAward = new Award();
				tempAward.awardID = awardId;
				tempAward.name = name;
				awardArray.add(tempAward);
			}
			
			return awardArray;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return null;
		}
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}