/*Entertainment.java
 * Created by: Randi Tinney
 * Created On: Oct 24 2018
 * Updated On: Nov 7 2018
 * Description: Entertainment.java is the main class for all types of entertainment within the Movies-R-Us database.
 * 		It provides methods for getting entertainment, lcreating entertainment, getting the entertainment's data,
 * 		changing the entertainment's data, getting the entertainment's awards won, 
 * 		getting a list of all sequels for the entertainment, getting the CastMembers of the Entertainment, deleting
 * 		an award, deleting a castmember, searching through the entertainment with predefined specifications, and getting
 * 		a list of all entertainment within the databse
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
	
	int sequalID;
	
	String platform;
	String versionNum;
	
	//Default constructor that sets all values to empty and 0
	public Entertainment()
	{
		connect = new Connect();
		eid=0;
		title = "";
		type = "";
		releaseDate = "";
		genre = "";
		numInStock = 0;
		sequalID = 0;
		platform = "";
		versionNum = "";
	}
	
	/*Constructor with all information known and we just need to store the
	 * data
	 */
	public Entertainment(int eid, String title, String releaseDate, String genre, 
					int numInStock,int sequalID, String platform, String versionNum)
	{
		connect = new Connect();
		this.eid = eid;
		this.title = title;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.numInStock = numInStock;
		this.sequalID = sequalID;
		this.platform = platform;
		this.versionNum = versionNum;
		
		if(platform.equals("DVD") || platform.equals("BlueRay"))
			type = "Movie";
		else
			type = "Game";
	}
	
	/*Constructor that accesses the database to get the information of
	 * an entertainment with the passed eid. Throws GetEntertainmentException
	 * if the entertainment does not exist
	 */
	public Entertainment(int eid) throws GetEntertainmentException
	{
		Statement statement;
		ResultSet resultSet;
		
		connect = new Connect();
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			resultSet = statement.executeQuery("SELECT * FROM Entertainment E WHERE E.eid = " + eid);
				
			if(resultSet.next())
			{
				this.eid = eid;
				title = (String)resultSet.getObject(2);
				releaseDate = (String)resultSet.getObject(3);
				genre = (String)resultSet.getObject(4);
				numInStock = (int)resultSet.getObject(5);
				sequalID = (int)resultSet.getObject(6);
				platform = (String)resultSet.getObject(7);
				versionNum = (String)resultSet.getObject(8);
				
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
	
	/*Constructor that either adds or gets entertainment based on all of its information
	 * Will throw either AddEntertainmentException or GetEntertainmentException depending on
	 * if you are adding or getting
	 */
//	public Entertainment
//			(boolean add, String title, String releaseDate, String genre, 
//					int numInStock, Entertainment sequal, String platform, String versionNum)
//						throws AddEntertainmentException, GetEntertainmentException
//	{
//		connect = new Connect();
//		
//		if(add)
//		{
//			if(!add(title, releaseDate, genre, numInStock, sequal, platform, versionNum))
//				throw new AddEntertainmentException("Entertainment couldn't be added");
//		}
//		else
//		{
//			if(!get(title, releaseDate, genre, numInStock, sequal, platform, versionNum))
//				throw new GetEntertainmentException("Entertainment does not exist");
//		}
//	}
	
	/*boolean add(String title, String releaseDate, String genre, 
	 *		int numInStock, Entertainment sequal, String platform, String versionNum)
	 *Adds the entertainment to the database based on all of its information 
	 */
	public String add(String title, String releaseDate, String genre, 
			int numInStock, int sequalId, String platform, String versionNum)
	{
		PreparedStatement statement;
		connection = connect.connect();
		try
		{	
			statement = connection.prepareStatement("SELECT * "
					+ "FROM Entertainment E "
					+ "WHERE E.title = ? AND E.release_date = ? AND E.genre = ? AND num_in_stock = ? AND "
					+ "sequal_id = ? AND platform = ? AND version = ?");
			
			statement.clearParameters();
			statement.setString(1, title);
			statement.setString(2, releaseDate);
			statement.setString(3, genre);
			statement.setInt(4, numInStock);
			statement.setInt(5, sequalId);
			statement.setString(6, platform);
			statement.setString(7, versionNum);
			
			ResultSet resultSet = statement.executeQuery();
	         // process query results
	         
	         if(resultSet.next())//movie already exists in list
	         {
				resultSet.close();
			    statement.close();
				connect.disconnect(connection);
	        	return "[Error]: Could not add '" + title + "', as it already exists.";
	         }
	         else
	         {
	        	 statement = connection.prepareStatement("INSERT INTO Entertainment VALUES (0,?,?,?,?,?,?,?)");
	        	 
	        	 statement.clearParameters();
	 			statement.setString(1, title);
	 			statement.setString(2, releaseDate);
	 			statement.setString(3, genre);
	 			statement.setInt(4, numInStock);
	 			statement.setInt(5, sequalId);
	 			statement.setString(6, platform);
	 			statement.setString(7, versionNum);
	 			
				statement.executeUpdate();
					
					
				this.title = title;
				this.releaseDate = releaseDate;
				this.genre = genre;
				this.numInStock = numInStock;
				this.sequalID = sequalId;
				this.platform = platform;
				this.versionNum = versionNum;
				
				if(platform.equals("DVD") || platform.equals("BlueRay"))
					type = "Movie";
				else
					type = "Game";
				
				resultSet = statement.executeQuery("SELECT eid FROM Entertainment");
				if(resultSet.last())
					eid = (int)resultSet.getObject(1);
				
				resultSet.close();
			    statement.close();
				connect.disconnect(connection);
		        return "Succesfully added '" + title + "'.";
	        }//end of else
		}//end try
		catch(Exception e)
		{
			connect.disconnect(connection);
			e.printStackTrace();
			return "[Error] Failed to add '" + title + "'.";
		}
	}
	
	/*boolean get(int eid)
	 * Gets the entertainment from the database based on its eid
	 */
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
				sequalID = (int)resultSet.getObject(6);
				platform = (String)resultSet.getObject(7);
				versionNum = (String)resultSet.getObject(8);
				
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
	
	/*boolean get(String title, String releaseDate, String genre, 
	 *		int numInStock, Entertainment sequal, String platform, String versionNum)
	 * Gets the entertainment from the database based on all of its information
	 */
	public boolean get(String title, String releaseDate, String genre, 
			int numInStock, Entertainment sequal, String platform, String versionNum)
	{
		PreparedStatement statement;
		int sid;
		connection = connect.connect();
		try
		{
			
			if(sequal.getEID() == 0)
				sid = 0;
			else
				sid = sequal.getEID();
			
			statement = connection.prepareStatement("SELECT * "
					+ "FROM Entertainment E "
					+ "WHERE E.title = ? AND E.releaseDate = ? AND E.genre =? AND num_in_stock = ? AND "
					+ "sequal_id = ? AND platform =? AND version = ?");
			
			statement.clearParameters();
			statement.setString(1, title);
			statement.setString(2, releaseDate);
			statement.setString(3, genre);
			statement.setInt(4, numInStock);
			statement.setInt(5, sid);
			statement.setString(6, platform);
			statement.setString(7, versionNum);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				eid = resultSet.getInt("eid");
				
				this.title = title;
				this.releaseDate = releaseDate;
				this.genre = genre;
				this.numInStock = numInStock;
				this.sequalID = sequal.getEID();
				
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
	
	/*int getEID()
	 * returns the eid of the Entertainment
	 */
	public int getEID()
	{
		return eid;
	}
	
	/*String getTitle()
	 * returns the title of the Entertainment
	 */
	public String getTitle()
	{
		return title;
	}
	
	/*String getReleaseDate()
	 * returns the release date of the Entertainment
	 */
	public String getReleaseDate()
	{
		return releaseDate;
	}
	
	/*String getGenre()
	 * returns the genre of the Entertainment
	 */
	public String getGenre()
	{
		return genre;
	}
	
	/*int getNumInStock()
	 * returns the number of the Entertainment in stock
	 */
	public int getNumInStock()
	{
		return numInStock;
	}
	
	/*ArrayList<Award> getAwardsWon
	 * Accesses the database to return an ArrayList of
	 * the awards the entertainment has won
	 */
	public ArrayList<Award> getAwardsWon()
	{
		ArrayList<Award> list = new ArrayList<Award>();
		Statement statement;
		ResultSet resultSet = null;
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			resultSet = statement.executeQuery("SELECT * FROM Awards A "
					+ "INNER JOIN Won W ON A.awardID = W.awardID "
					+ "WHERE W.eid = " + eid);
			
			while(resultSet.next())
				list.add(new Award(resultSet.getInt("awardID"), resultSet.getString("title")));
			
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
	
	/*int getSequalID()
	 * returns the sequel id of the entertainment
	 */
	public int getSequalID()
	{
		return sequalID;
	}
	
	/*String getType()
	 * returns the type of the entertainment
	 */
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
					+ "num_in_stock AS 'Stock', "
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
	
	/*Entertainment getSequal()
	 * returns the sequal of the entertainment
	 */
	public Entertainment getSequal()
	{
		Entertainment e = null;
		
		try
		{
			e = new Entertainment(sequalID);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return e;
	}
	
	/*ArrayList<Entertainment> getSequalList()
	 * Returns an ArrayList of Entertainment objects of
	 * all sequals of the entertainment
	 */
	public ArrayList<Entertainment> getSequalList()
	{
		ArrayList<Entertainment> list = new ArrayList<Entertainment>();
		Entertainment sequal;
		boolean sequalExists = true;
		int id = getSequalID();
		//int id = this.eid;
		
		Statement statement;
		connection = connect.connect();
		
		if(id == 0)
		{
			return list;
		}
		else
		{
			try
			{
				statement = connection.createStatement();
				
				while(sequalExists)
				{
					sequal = new Entertainment(id);
					list.add(sequal);
					
					ResultSet resultSet = statement.executeQuery("SELECT eid FROM Entertainment E "
							+ "WHERE E.eid = " + sequal.getSequalID());
					
					if(resultSet.next())
						id = (int)resultSet.getObject(1);
					else
						sequalExists = false;
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
	}
	
	/*String getPlatform()
	 * returns the platform of the entertainment
	 */
	public String getPlatform()
	{
		return platform;
	}
	
	/*String getVersion()
	 * returns the version number of the entertainment
	 */
	public String getVersion()
	{
		return versionNum;
	}
	
	/*void changeTitle(String title)
	 * Accesses the database to change the title of the
	 * entertainment
	 */
	public String changeTitle(String title)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET title = ? WHERE eid = " + eid);
			statement.clearParameters();
			statement.setString(1, title);
			
			statement.executeUpdate();
			
			this.title = title;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated title.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to updated title.";
			
		}
	}
	
	/*void changeReleaseDate(String releaseDate)
	 * Accesses the database to change the release date of
	 * the entertainment
	 */
	public String changeReleaseDate(String releaseDate)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET release_date = ? WHERE eid = " + eid);
			statement.clearParameters();
			statement.setString(1, releaseDate);
			
			statement.executeUpdate();
			
			this.releaseDate = releaseDate;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated release date.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to updated release date.";
		}
	}
	
	/*void changeGenre(String genre)
	 * Accesses the database to change the genre of the
	 * entertainment
	 */
	public String changeGenre(String genre)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET genre = ? WHERE eid = " + eid);
			statement.clearParameters();
			statement.setString(1, genre);
			
			statement.executeUpdate();
			
			this.genre = genre;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated genre.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to updated genre.";
		}
	}
	
	/*void addOneToStock()
	 * Accesses the database to add one to the total number
	 * of entertainment in stock
	 */
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
				newNumberInStock = resultSet.getInt("num_in_stock");
				
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
	
	/*boolean removeOneFromStock()
	 * Accesses the database to remove one from the total
	 * number of entertainment in stock
	 */
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
				newNumberInStock = resultSet.getInt("num_in_stock");
				
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
	
	/*void changeNumOfStock(int newStockNum)
	 * Accesses the database to change the total number
	 * of entertainment in stock
	 */
	public String changeNumOfStock(int newStockNum)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET num_in_stock = ? WHERE eid = " + this.eid);
			statement.clearParameters();
			statement.setInt(1, newStockNum);
			
			statement.executeUpdate();
			
			this.numInStock = newStockNum;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated stock.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to update the stock.";
		}
	}
	
	/*void addAward(ArrayList<Award> awardWon)
	 * An ArrayList of Award objects is passed and then
	 * added to the database with the entertainment
	 */
	public void addAward(ArrayList<Award> awardWon)
	{
		PreparedStatement statement;
		connection = connect.connect();
		try
		{
			System.out.println("Inside addAward");
			statement = connection.prepareStatement("INSERT INTO Won VALUES (" + eid + ",?)");
			
			for(int i = 0; i < awardWon.size(); i++)
			{
				System.out.println("Adding " + awardWon.get(i).getName());
				statement.clearParameters();
				statement.setInt(1, awardWon.get(i).getAwardID());
				statement.executeUpdate();
			}
			
	        statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*void addSequal(Entertainment sequal)
	 * Accesses the database to add a sequal to the entertainment
	 */
	public void addSequal(Entertainment sequal)
	{
		Statement statement;
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET sequal_id = " + sequal.getEID() + " WHERE eid = " + eid);
			
			this.sequalID = sequal.getEID();
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*boolean addSequal(intt sequalID)
	 * Accesses the database to add a sequal to the entertainment
	 */
	public String addSequal(int sequalID)
	{
		try
		{
			//Entertainment sequal = new Entertainment(sequalID);//just to see if the sequal exists
			Statement statement;
			
			connection = connect.connect();
			statement = connection.createStatement();
			
			statement.executeUpdate("UPDATE Entertainment SET sequal_id = " + sequalID + " WHERE eid = " + eid);
			
			this.sequalID = sequalID;
			
			statement.close();
			connect.disconnect(connection);
			return "Successfully added sequel.";
		}
		catch(Exception e)
		{
			return "Failed to add sequel.";
		}
		
	}
	
	/*boolean addSequal(String title, String releaseDate, String genre, 
					int numInStock, Entertainment sequalID, String platform, String versionNum)
	 * Accesses the database to add a sequal to the entertainment
	 */
//	public boolean addSequal(String title, String releaseDate, String genre, 
//					int numInStock, Entertainment sequalID, String platform, String versionNum)
//	{
//		try
//		{
//			Entertainment sequal = new Entertainment(false, title, releaseDate, genre, numInStock,
//					sequalID, platform, versionNum);//just to see if the sequal exists
//			Statement statement;
//			
//			connection = connect.connect();
//			statement = connection.createStatement();
//			
//			int id = sequal.getSequalID();
//			
//			statement.executeUpdate("UPDATE Entertainment SET sequal_id = " + id + " WHERE eid = " + eid);
//			
//			this.sequalID = id;
//			
//			statement.close();
//			connect.disconnect(connection);
//			return true;
//		}
//		catch(GetEntertainmentException e)
//		{
//			return false;
//		}
//		catch(Exception e)
//		{
//			return false;
//		}
//	}
	
	/*void removeSequal()
	 *Accesses the database to remove the sequal from the entertainment
	 */
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
	
	/*void changePlatform(String platform)
	 * Accesses the database to change the platform of the entertainment
	 */
	public String changePlatform(String platform)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET platform = ? WHERE eid = " + eid);
			statement.clearParameters();
			statement.setString(1, platform);
			
			statement.executeUpdate();
			
			this.platform = platform;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated platform.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to updated platform.";
		}
	}
	
	/*void changeVersionNum(String versionNum)
	 * Accesses the database to change the versionNum of the entertainment
	 */
	public String changeVersionNum(String versionNum)
	{
		PreparedStatement statement;
		
		connection = connect.connect();
		try
		{
			statement = connection.prepareStatement("UPDATE Entertainment SET version = ? WHERE eid = " + eid);
			statement.clearParameters();
			statement.setString(1, versionNum);
			
			statement.executeUpdate();
			
			this.versionNum = versionNum;
			
	        statement.close();
			connect.disconnect(connection);
			return "Successfully updated version.";
		}
		catch(Exception e)
		{
			connect.disconnect(connection);
			return "Failed to updated version.";
		}
	}
	
	/*ArrayList<Entertainment> searchBy(String searchTerm, String searchBy, String userEmail)
	 * Accesses the database to search all entertainments listed for a specific searchTerm under
	 * a specific clause. Creates an ArrayList of Entertainment objects as the result. Passes
	 * the userEmail for some of the searches
	 */
	public DefaultTableModel searchBy(String searchTerm, String searchBy, String userEmail, boolean awardWinners, boolean gamesOnly, boolean moviesOnly)
	{
		Statement statement;
		ResultSet resultSet = null;
		
		connection = connect.connect();
		
		try
		{
			statement = connection.createStatement();
			
			String query = "SELECT eid AS 'ID', title AS 'Title', "
					+ "release_date AS 'Release Date', genre AS 'Genre', "
					+ "num_in_stock AS 'Stock', "
					+ "sequal_id AS 'Sequel ID', platform AS 'Platform', version AS 'Version' ";
			
			if(searchBy.equals("ACTOR"))
			{
				System.out.println(searchTerm);
				query = query + "FROM worked_in W "
						+ "NATURAL JOIN Entertainment E "
						+ "NATURAL JOIN Cast_Member C "
						+ "WHERE C.name LIKE '" + searchTerm + "' ";
			}
			else if(searchBy.equals("DIRECTOR"))
			{
				System.out.println(searchTerm);
				query = query + "FROM worked_in W "
						+ "NATURAL JOIN Entertainment E "
						+ "NATURAL JOIN Cast_Member C "
						+ "WHERE C.name LIKE '" + searchTerm + "' AND C.is_director = 1 ";
			}
			else if (searchBy.equals("TITLE") || searchBy.equals("PLATFORM") || searchBy.equals("GENRE")){
				
				if (userEmail != null)
					query = query + "FROM entertainment E WHERE E.eid NOT IN ( "
							+ "SELECT eid "
							+ "FROM entertainment E natural join rent_history R "
							+ "WHERE R.user_email = '" + userEmail + "') ";
				else
					query = query + "FROM Entertainment E WHERE " + searchBy.toLowerCase() + " LIKE '" + searchTerm + "' ";
			}
			
			if (awardWinners)
				query = query + "AND E.eid IN ( "
					+ "SELECT DISTINCT eid "
					+ "FROM won) ";
			
			if (gamesOnly)
				query = query + "AND E.platform <> 'DVD' AND E.platform <> 'BlueRay' ";
			else if (moviesOnly)
				query = query + "AND E.platform = 'DVD' OR E.platform = 'BlueRay' ";
			
			resultSet = statement.executeQuery(query);
			
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
	
	/*ArrayList<Entertainment> getArrayListOfAllItems()
	 * Accesses the database and gets a list of all entertainment objects
	 */
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
				list.add(new Entertainment(resultSet.getInt("eid"), resultSet.getString("title"), 
						resultSet.getString("release_date"), resultSet.getString("genre"), resultSet.getInt("num_in_stock"),  
						resultSet.getInt("sequal_id"), resultSet.getString("platform"), resultSet.getString("version")));
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
	
	/*void removeEntertainment()
	 * Accesses the database and removes the entertainment
	 */
	public String removeEntertainment()
	{
		Statement statement;
		
		try
		{
			connection = connect.connect();
			
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Entertainment WHERE eid =" + eid);
			
			statement.close();
			connect.disconnect(connection);
			return "Successfuly removed: " + this.title;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return "Failed to remove: " + this.title;
		}
	}
	
	/*ArrayList<CastMember> getCastMembers()
	 * Accesses the database and gets a list of all castmembers
	 * that worked on the entertainment
	 */
	public ArrayList<CastMember> getCastMembers()
	{
		ArrayList<CastMember> list = new ArrayList<CastMember>();
		connection = connect.connect();
		Statement statement;
		String street;
		String city;
		String state;
		int zip;
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Cast_Member C " + 
					"INNER JOIN Address A ON C.aid = A.aid " + 
					"INNER JOIN Worked_In W ON C.cid = W.cid WHERE W.eid = " + eid);
			
			while(resultSet.next())
			{
				street = resultSet.getString("street");
				city = resultSet.getString("city");
				state = resultSet.getString("state");
				zip = resultSet.getInt("zip");
				
				list.add(new CastMember(resultSet.getInt("cid"), resultSet.getString("name"), street, city, state, zip,
							resultSet.getBoolean("is_director")));
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
	
	/*void addCastMembers(ArrayList<CastMember> cml)
	 * Gets passed an ArrayList of CastMember objects.
	 * Then accesses the database to add each CastMember
	 * to the entertainment under Worked_In
	 */
	public String addCastMembers(ArrayList<CastMember> cml)
	{
		connection = connect.connect();
		Statement statement;
		int cid;
		
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Worked_In WHERE eid = " + eid);
			
			for(int i = 0; i < cml.size(); i++)
			{
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Worked_In W WHERE W.cid = " + cml.get(i).getCID() + " AND W.eid = " + eid);
				if (!resultSet.next()){
					System.out.println("Adding cast to worked in.");
					cid = cml.get(i).getCID();
					statement.executeUpdate("INSERT INTO Worked_In VALUES(" + cid + "," + eid + ")");
				}
				resultSet.close();
			}
			
			statement.close();
			connect.disconnect(connection);
			return "Successfully added cast members.";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
			return "Failed to add cast members.";
		}
	}
	
	/*void deleteAward(Award e)
	 * Deletes an award from the database so it is no longer
	 * connected to the entertainment
	 */
	public void deleteAward(Award a)
	{
		connection = connect.connect();
		Statement statement;
		
		try
		{
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Won WHERE eid = " + eid + " AND awardID = " + a.getAwardID());
			
			statement.close();
			connect.disconnect(connection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			connect.disconnect(connection);
		}
	}
	
	/*void deleteCastMember(CastMember cm)
	 * Deletes a castmember from the database so it is no
	 * longer connected to the entertainment
	 */
	public void deleteCastMember(CastMember cm)
	{
		connection = connect.connect();
		Statement statement;
		int cid = cm.getCID();
		
		try
		{
			System.out.println("INSIDE DELETE CASTMEMBER with " + cm.getName() + " CID " + cm.getCID() + " EID " + eid);
			statement = connection.createStatement();
			
			statement.executeUpdate("DELETE FROM Worked_In WHERE cid = " + cid + " AND eid =" + eid);
			
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
		return this.title;
	}
}