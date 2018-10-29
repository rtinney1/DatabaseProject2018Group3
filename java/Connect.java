import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect
{
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/dbclasstest";
	//root@127.0.0.1:3306
	String username = "root";
	String password = "toor";
	
	
	public Connect()
	{	}
	
	public Connection connect()
	{
		Connection connection = null;
		System.out.println("Connecting database...");

		try
		{
			Class.forName(JDBC_DRIVER).newInstance();
			System.out.println("successful 1");
			connection = DriverManager.getConnection(DATABASE_URL, username, password);// username , password);
			System.out.println("successful 2");

			System.out.println("Database connected!");
			
			return connection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return connection;
		}
	}
	
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