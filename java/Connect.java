/*Connect.java
 * Created by: Randi Tinney
 * Created On: Oct 24 2018
 * Updated On: Nov 2 2018
 * Description: Connect.java is the main class that connects to the database.
 * 		It has a default constructor and then the user needs to call on the conenct method
 * 		to get the needed Connection object. The disconnect method is then passed the 
 * 		Connection object to ensure the connection to the database is severed
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect
{
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/sys?useLegacyDatetimeCode=false&serverTimezone=America/New_York&zeroDateTimeBehavior=CONVERT_TO_NULL"; 
	//root@127.0.0.1:3306
	String username = "root";
	String password = "password";
	
	//Default constructor
	public Connect()
	{	}
	
	/*Connection connect()
	 * Returns a Connection object when it has connected to the database
	 */
	public Connection connect()
	{
		Connection connection = null;
		System.out.println("Connecting database...");

		try
		{
			Class.forName(JDBC_DRIVER).newInstance();
			connection = DriverManager.getConnection(DATABASE_URL, username, password);// username , password);
			
			return connection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return connection;
		}
	}
	
	/*boolean disconnect(Connection connection)
	 * Disconnects the connection to the database
	 */
	public boolean disconnect(Connection connection)
	{
		try {
			connection.close();
			System.out.println("You've been disconnected!");
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}
}
