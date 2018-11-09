import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

public class Entertainment 
{
	Connect connect;
	
	Connection connection;
	
	int eid;
	String type;
	String title;
	String releaseDate;
	String genre;
	int numInStock;
	
	String awardsWon;
	int sequalID;
	
	String platform;
	String versionNum;
	
	public Entertainment()
	{
		connect = new Connect();
	}
	
	public Entertainment(int eid, String title, String releaseDate, String genre, 
					int numInStock, String awardsWon, int sequalID, String platform, String versionNum)
	{
		connect = new Connect();
		this.eid = eid;
		this.title = title;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.numInStock = numInStock;
		this.awardsWon = awardsWon;
		this.sequalID = sequalID;
		this.platform = platform;
		this.versionNum = versionNum;
		
		if(platform.equals("DVD") || platform.equals("BlueRay"))
			type = "Movie";
		else
			type = "Game";
	}
	
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
				awardsWon = (String)resultSet.getObject(6);
				sequalID = (int)resultSet.getObject(7);
				platform = (String)resultSet.getObject(8);
				versionNum = (String)resultSet.getObject(9);
				
				if(platform.equals("DVD") || platform.equals("BlueRay"))
					type = "Movie";
				else
					type = "Game";
				
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
					int numInStock, String awardsWon, int sequalID, String platform, String versionNum)
						throws AddEntertainmentException, GetEntertainmentException
	{
		connect = new Connect();
		
		if(add)
		{
			if(!add(title, releaseDate, genre, numInStock, awardsWon, sequalID, platform, versionNum))
				throw new AddEntertainmentException("Entertainment couldn't be added");
		}
		else
		{
			if(!get(title, releaseDate, genre, numInStock, awardsWon, sequalID, platform, versionNum))
				throw new GetEntertainmentException("Entertainment does not exist");
		}
	}
	
	public boolean add(String title, String releaseDate, String genre, 
			int numInStock, String awardsWon, int sequalID, String platform, String versionNum)
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
				
				if(platform.equals("DVD") || platform.equals("BlueRay"))
					type = "Movie";
				else
					type = "Game";
				
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
				awardsWon = (String)resultSet.getObject(6);
				sequalID = (int)resultSet.getObject(7);
				platform = (String)resultSet.getObject(8);
				versionNum = (String)resultSet.getObject(9);
				
				if(platform.equals("DVD") || platform.equals("BlueRay"))
					type = "Movie";
				else
					type = "Game";
				
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
			int numInStock, String awardsWon, int sequalID, String platform, String versionNum)
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
				
				if(platform.equals("DVD") || platform.equals("BlueRay"))
					type = "Movie";
				else
					type = "Game";
				
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
	
	public ArrayList<String> getAwardsWon()
	{
		String[] awards = awardsWon.split(Pattern.quote("$"));
		ArrayList<String> retString = new ArrayList<String>();
		Statement statement;
		ResultSet resultSet = null;
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			for(int i = 0; i < awards.length; i++)
			{
				resultSet = statement.executeQuery("SELECT title FROM Awards WHERE awID =" + Integer.parseInt(awards[i]));
				
				while(resultSet.next())
				{
					retString.add((String)resultSet.getObject(1));
				}
			}
			
			resultSet.close();
			statement.close();
			connect.disconnect(connection);
			return retString;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return retString;
		}
	}
	
	public int getSequalID()
	{
		return sequalID;
	}
	
	public String getType()
	{
		return type;
	}
	
	/*
	 * Input: entertainment ID (for which the sequels are desired)
	 * Output: a DefaultTableModel containing all the Entertainment rows
	 * that are sequels to the given EID
	 */
	public DefaultTableModel findAllSequalsForEid(int eid){

		if(eid == 0){
			return null;
		}
		else
		{
			Statement statement;
			connection = connect.connect();
			
			// Building an ArrayList of Sequal Ids Branched from the given Movie/Game EID
			ArrayList<Integer> sequalIdList = new ArrayList<Integer>();
			Entertainment temp;
			try {
				temp = new Entertainment(eid);
				while (temp.getSequalID() != 0){
					sequalIdList.add(temp.getSequalID());
					temp = new Entertainment(temp.getSequalID());
				}
			} catch (GetEntertainmentException e1) {
				System.out.println("ERROR: Failed to generate a list of sequals for a given EID.");
				e1.printStackTrace();
				return null;
			}
			
			
			if (sequalIdList.size() > 0){
				try
				{
					statement = connection.createStatement();
					
					// Building the select query to include all sequel id checks
					String query = "SELECT eid AS 'ID', title AS 'Title', "
					+ "release_date AS 'Release Date', genre AS 'Genre', "
					+ "num_in_stock AS 'Stock', awards_won AS 'Awards Won', "
					+ "sequal_id AS 'Sequel ID', platform AS 'Platform', version AS 'Version' "
					+ "FROM Entertainment E WHERE ";
					int count = 0;
					
					if (sequalIdList.size() > 1)
						while (count < sequalIdList.size()-1) {
							query = query + "E.eid = " + sequalIdList.get(count) + " OR ";
							count++;
						}
					
					query = query + "E.eid = " + sequalIdList.get(count);
					
					ResultSet resultSet = statement.executeQuery(query);
					
					DefaultTableModel tableModel = TableModelUtil.buildTableModel(resultSet);
					
					statement.close();
					resultSet.close();
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
		
		return null;
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
	
	public void changeAwardsWon(String awardWon)
	{
		Statement statement;
		connection = connect.connect();
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Awards WHERE title ='" + awardWon + "'");
			
			if(resultSet.next())
			{
				awardsWon = awardsWon + "$" + (int)resultSet.getObject(1);
				
				statement.executeUpdate("UPDATE Entertainment SET awards_won = " + awardsWon + " WHERE eid = " + eid);
			}
			else
			{
				statement.executeUpdate("INSERT INTO Awards VALUES (" + 0 + "'" + awardWon + "'");

				resultSet = statement.executeQuery("SELECT * FROM Awards");
				
				resultSet.last();
				
				awardsWon = awardsWon + "$" + (int)resultSet.getObject(1);
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
					int numInStock, String awardsWon, int sequalID, String platform, String versionNum)
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
	
	public DefaultTableModel searchBy(String searchTerm, String searchBy, String userEmail, boolean awardWinners)
	{
		ArrayList<Entertainment> list = new ArrayList<Entertainment>();
		Statement statement;
		ResultSet resultSet = null;
		
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			if(searchTerm.equals("ALL")){
				resultSet = statement.executeQuery("SELECT * FROM Entertainment");
			}
				//list = getArrayListOfAllItems();
			else if(searchTerm.equals("MOVIES_ONLY"))
			{
				resultSet = statement.executeQuery("SELECT * FROM Entertainment E WHERE platform ='DVD' OR platform = 'BlueRay'");
			}
			else if(searchTerm.equals("GAMES_ONLY"))
			{
				resultSet = statement.executeQuery("SELECT * FROM Entertainment E WHERE platform <>'DVD' AND platform <> 'BlueRay'");
			}
			else if(searchTerm.equals("AWARDS_MOVIES"))
			{
				resultSet = statement.executeQuery("SELECT * FROM Entertainment E "
						+ "WHERE awards_won > 0 AND platform ='DVD' OR platform = 'BlueRay'");
			}
			else if(searchTerm.equals("AWARDS_GAMES"))
			{
				resultSet = statement.executeQuery("SELECT * FROM Entertainment E "
						+ "WHERE awards_won > 0 AND platform <>'DVD' AND platform <> 'BlueRay'");
			}
			else if(searchTerm.equals("MOVIE_NO_CHECK"))
			{
				resultSet = statement.executeQuery("SELECT * FROM rent_history R "
						+ "INNER JOIN Entertainment E ON R.eid = E.eid "
						+ "INNER JOIN Users U ON R.user_email = U.user_email "
						+ "WHERE R.user_email = '" + userEmail + "' AND E.platform = 'DVD' OR E.platform = 'BlueRay'");
			}
			else if(searchTerm.equals("GAME_NO_CHECK"))
			{
				resultSet = statement.executeQuery("SELECT * FROM rent_history R "
						+ "INNER JOIN Entertainment E ON R.eid = E.eid "
						+ "INNER JOIN Users U ON R.user_email = U.user_email "
						+ "WHERE R.user_email = '" + userEmail + "' AND E.platform <> 'DVD' AND E.platform <> 'BlueRay'");
			}
			else if(searchBy.equals("ACTOR"))
			{
				System.out.println(searchTerm);
				resultSet = statement.executeQuery("SELECT "
						+ "E.eid, E.title, E.release_date, E.genre, E.num_in_stock, E.awards_won, E.sequal_id, E.platform, E.version "
						+ "FROM worked_in W "
						+ "INNER JOIN Entertainment E ON W.eid = E.eid "
						+ "INNER JOIN Cast_Member C ON W.cid = C.cid "
						+ "WHERE C.name LIKE '" + searchTerm + "'");
			}
			else if(searchBy.equals("DIRECTOR"))
			{
				System.out.println(searchTerm);
				resultSet = statement.executeQuery("SELECT "
						+ "E.eid, E.title, E.release_date, E.genre, E.num_in_stock, E.awards_won, E.sequal_id, E.platform, E.version "
						+ "FROM worked_in W "
						+ "INNER JOIN Entertainment E ON W.eid = E.eid "
						+ "INNER JOIN Cast_Member C ON W.cid = C.cid "
						+ "WHERE C.name LIKE '" + searchTerm + "' AND C.is_director = 1");
				
//				while(resultSet.next())
//				{
//					list.add(new Entertainment((int)resultSet.getObject(3), (String)resultSet.getObject(4), (String)resultSet.getObject(5), 
//							(String)resultSet.getObject(6), (int)resultSet.getObject(7), (String)resultSet.getObject(8), 
//							(int)resultSet.getObject(9), (String)resultSet.getObject(10), (String)resultSet.getObject(11)));
//				}
			}

			
			else if (searchBy.equals("TITLE") || searchBy.equals("PLATFORM") || searchBy.equals("GENRE")){
				if (awardWinners)
					resultSet = statement.executeQuery("SELECT eid AS 'ID', title AS 'Title', "
							+ "release_date AS 'Release Date', genre AS 'Genre', "
							+ "num_in_stock AS 'Stock', awards_won AS 'Awards Won', "
							+ "sequal_id AS 'Sequel ID', platform AS 'Platform', version AS 'Version' "
							+ "FROM Entertainment E WHERE " + searchBy.toLowerCase() + " LIKE '" + searchTerm + "' AND E.awards_won > 0");
				else
					resultSet = statement.executeQuery("SELECT eid AS 'ID', title AS 'Title', "
							+ "release_date AS 'Release Date', genre AS 'Genre', "
							+ "num_in_stock AS 'Stock', awards_won AS 'Awards Won', "
							+ "sequal_id AS 'Sequel ID', platform AS 'Platform', version AS 'Version' "
							+ "FROM Entertainment E WHERE " + searchBy.toLowerCase() + " LIKE '" + searchTerm + "'");
			}
			
			DefaultTableModel tableModel = TableModelUtil.buildTableModel(resultSet);
			
			statement.close();
			resultSet.close();
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
	
	public ArrayList<Entertainment> getArrayListOfAllItems()
	{
		Statement statement;
		ArrayList<Entertainment> list = new ArrayList<Entertainment>();
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Entertainment");
	         
			while ( resultSet.next() ) 
	         {
				list.add(new Entertainment((int)resultSet.getObject(1), (String)resultSet.getObject(2), (String)resultSet.getObject(3), 
						(String)resultSet.getObject(4), (int)resultSet.getObject(5), (String)resultSet.getObject(6), 
						(int)resultSet.getObject(7), (String)resultSet.getObject(8), (String)resultSet.getObject(9)));
	         } 
			
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
	
	public void removeEntertainment()
	{
		Statement statement;
		try
		{
			connection = connect.connect();
			
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Entertainment WHERE eid =" + eid);
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	@Override
	public String toString()
	{
		String outputString;
		
		outputString = "" + eid + " " + type + " " + title;
		
		return outputString;
	}
}
