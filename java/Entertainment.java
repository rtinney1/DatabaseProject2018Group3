import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Entertainment 
{
	Connect connect;
	
	Connection connection;
	
	int eid;
	String title;
	String releaseDate;
	String genre;
	int numInStock;
	
	int awardsWon;
	int sequalID;
	
	String platform;
	String versionNum;
	
	public Entertainment(int eid) throws GetEntertainmentException
	{
		Statement statement;
		ResultSet resultSet;
		
		connect = new Connect();
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			resultSet = statement.executeQuery("SELECT * FROM Entertainment WHERE eid = " + eid);
				
			if(resultSet.next())
			{
				this.eid = eid;
				title = (String)resultSet.getObject(2);
				releaseDate = (String)resultSet.getObject(3);
				genre = (String)resultSet.getObject(4);
				numInStock = (int)resultSet.getObject(5);
				awardsWon = (int)resultSet.getObject(6);
				sequalID = (int)resultSet.getObject(7);
				platform = (String)resultSet.getObject(8);
				versionNum = (String)resultSet.getObject(9);
				
				resultSet.close();
			    statement.close();
				connect.disconnect(connection);
			}
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			throw new GetEntertainmentException("Error retrieving entertainment");
		}
	}
	
	public Entertainment
			(boolean add, String title, String releaseDate, String genre, 
					int numInStock, int awardsWon, int sequalID, String platform, String versionNum)
						throws AddEntertainmentException, GetEntertainmentException
	{
		connect = new Connect();
		
		if(add)
		{
			if(!add(title, releaseDate, genre, numInStock, awardsWon, sequalID, platform, versionNum))
				throw new AddEntertainmentException("Game couldn't be added");
		}
		else
		{
			if(!get(title, releaseDate, genre, numInStock, awardsWon, sequalID, platform, versionNum))
				throw new GetEntertainmentException("Game does not exist");
		}
	}
	
	public boolean add(String title, String releaseDate, String genre, 
			int numInStock, int awardsWon, int sequalID, String platform, String versionNum)
	{
		Statement statement;

		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * "
					+ "FROM Entertainment E "
					+ "WHERE E.title = '" + title + "' AND E.releaseDate = '" + releaseDate 
					+ "' AND E.genre = '" + genre + "'," + numInStock + "," + awardsWon + "," + sequalID
					+ ",'" + platform + "','" + versionNum + "'");
	         // process query results
	         
	         if(resultSet.next())//movie already exists in list
	         {
				resultSet.close();
			    statement.close();
				connect.disconnect(connection);
	        	return false;
	         }
	         else
	         {
				statement.executeUpdate("INSERT INTO Entertainment VALUES"
						+ "(" + 0 + ",'" + title + "' AND E.releaseDate = '" + releaseDate 
						+ "' AND E.genre = '" + genre + "'," + numInStock + "," + awardsWon + "," + sequalID
						+ ",'" + platform + "','" + versionNum + "')");
					
					
				this.title = title;
				this.releaseDate = releaseDate;
				this.genre = genre;
				this.numInStock = numInStock;
				this.awardsWon = awardsWon;
				this.sequalID = sequalID;
				this.platform = platform;
				this.versionNum = versionNum;
				
				
				resultSet = statement.executeQuery("SELECT eid FROM Entertanment");
				
				if(resultSet.last())
					eid = (int)resultSet.getObject(1);
				
					
				resultSet.close();
			    statement.close();
				connect.disconnect(connection);
		        return true;
	        }//end of else
		}//end try
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean get(int eid)
	{
		Statement statement;
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Entertainment E WHERE e.eid = " + eid);
			
			if(resultSet.next())
			{
				this.eid = eid;
				
				title = (String)resultSet.getObject(2);
				releaseDate = (String)resultSet.getObject(3);
				genre = (String)resultSet.getObject(4);
				numInStock = (int)resultSet.getObject(5);
				awardsWon = (int)resultSet.getObject(6);
				sequalID = (int)resultSet.getObject(7);
				platform = (String)resultSet.getObject(8);
				versionNum = (String)resultSet.getObject(9);
				
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
	        	return true;
			}
			else
			{
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
	        	return false;
			}
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
        	return true;
		}
	}
	
	public boolean get(String title, String releaseDate, String genre, 
			int numInStock, int awardsWon, int sequalID, String platform, String versionNum)
	{
		Statement statement;

		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * "
					+ "FROM Entertainment E "
					+ "WHERE E.title = '" + title + "' AND E.releaseDate = '" + releaseDate 
					+ "' AND E.genre = '" + genre + "'," + numInStock + "," + awardsWon + "," + sequalID
					+ ",'" + platform + "','" + versionNum + "'");
			
			if(resultSet.next())
			{
				eid = (int)resultSet.getObject(1);
				
				this.title = title;
				this.releaseDate = releaseDate;
				this.genre = genre;
				this.numInStock = numInStock;
				this.awardsWon = awardsWon;
				this.sequalID = sequalID;
				
				this.platform = platform;
				this.versionNum = versionNum;
				
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
	        	return true;
			}
			else
			{
				resultSet.close();
		        statement.close();
				connect.disconnect(connection);
	        	return false;
			}
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return false;
		}
	}
	
	public int getEID()
	{
		return eid;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getReleaseDate()
	{
		return releaseDate;
	}
	
	public String getGenre()
	{
		return genre;
	}
	
	public int getNumInStock()
	{
		return numInStock;
	}
	
	public int getAwardsWon()
	{
		return awardsWon;
	}
	
	public int getSequalID()
	{
		return sequalID;
	}
	
	public Entertainment getSequal()
	{
		Entertainment sequal;
		
		sequal = null;
		
		if(sequalID == 0)
		{
			return sequal;
		}
		else
		{
			try
			{
				sequal = new Entertainment(sequalID);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return sequal;
		}
	}
	
	public String getPlatform()
	{
		return platform;
	}
	
	public String getVersion()
	{
		return versionNum;
	}
	
	public void changeTitle(String title)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET title = '" + title + "' WHERE eid = " + eid);
			
			this.title = title;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeReleaseDate(String releaseDate)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET release_date = '" + releaseDate + "' WHERE eid = " + eid);
			
			this.releaseDate = releaseDate;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeGenre(String genre)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET genre = '" + genre + "' WHERE eid = " + eid);
			
			this.genre = genre;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void addOneToStock()
	{
		Statement statement;
		int newNumberInStock;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT num_in_stock FROM Entertainment E WHERE eid = " + eid);
			
			if(resultSet.next())
			{
				newNumberInStock = (int)resultSet.getObject(1);
				
				newNumberInStock++;
				
				statement.executeUpdate("UPDATE Entertainment SET num_in_stock = " + newNumberInStock + " WHERE eid = " + eid);
				
				this.numInStock = newNumberInStock;
			}
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public boolean removeOneFromStock()
	{
		Statement statement;
		int newNumberInStock;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT num_in_stock FROM Entertainment E WHERE eid = " + eid);
			
			if(resultSet.next())
			{
				newNumberInStock = (int)resultSet.getObject(1);
				
				newNumberInStock--;
				
				if(newNumberInStock >= 0)
				{
					statement.executeUpdate("UPDATE Entertainment SET num_in_stock = " + newNumberInStock + " WHERE eid = " + eid);
				
					this.numInStock = newNumberInStock;
			        statement.close();
					connect.disconnect(connection);
					return true;
				}
				else
					return false;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return false;
		}
	}
	
	public void changeNumOfStock(int newStockNum)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET num_in_stock = " + newStockNum + " WHERE eid = " + eid);
			
			this.numInStock = newStockNum;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeAwardsWon(int awardsWon)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET awards_won = " + awardsWon + " WHERE eid = " + eid);
			
			this.awardsWon = awardsWon;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public boolean addSequal(int sequalID)
	{
		try
		{
			Entertainment sequal = new Entertainment(sequalID);//just to see if the sequal exists
			Statement statement;
			
			connection = connect.connect();
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET sequal_id = " + sequalID + " WHERE eid = " + eid);
			
			this.sequalID = sequalID;
			
			statement.close();
			connect.disconnect(connection);
			return true;
		}
		catch(GetEntertainmentException e)
		{
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	public boolean addSequal(String title, String releaseDate, String genre, 
					int numInStock, int awardsWon, int sequalID, String platform, String versionNum)
	{
		try
		{
			Entertainment sequal = new Entertainment(false, title, releaseDate, genre, numInStock,
					awardsWon, sequalID, platform, versionNum);//just to see if the sequal exists
			Statement statement;
			
			connection = connect.connect();
			statement = connection.createStatement();
			
			int id = sequal.getSequalID();
			
			statement.executeUpdate("UPDATE Entertainment SET sequal_id = " + id + " WHERE eid = " + eid);
			
			this.sequalID = sequalID;
			
			statement.close();
			connect.disconnect(connection);
			return true;
		}
		catch(GetEntertainmentException e)
		{
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void removeSequal()
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET sequsl_id = " + 0 + " WHERE eid = " + eid);
			
			this.sequalID = 0;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changePlatform(String platform)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET platform = '" + platform + "' WHERE eid = " + eid);
			
			this.platform = platform;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
	
	public void changeVersionNum(String versionNum)
	{
		Statement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET version = '" + versionNum + "' WHERE eid = " + eid);
			
			this.versionNum = versionNum;
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			
		}
	}
}
