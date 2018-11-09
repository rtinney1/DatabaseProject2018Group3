/*SubPlan.java
 * Created by: Randi Tinney
 * Created On: Nov 4 2018
 * Updated On: Nov 4 2018
 * Description: SubPlan.java creates a SubPlan object which indicates
 * 		what level the user is at. It has methods that will return the
 * 		level number, the max quota, and a list of all available subplans
 * 		in the database
 */

package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SubPlan
{
	int level;
	int quota;
	
	//Default constructor
	public SubPlan()
	{
		
	}
	
	//Constructor when both level and quota are known
	public SubPlan(int level, int quota)
	{
		this.level = level;
		this.quota = quota;
	}
	
	/*int getLevel()
	 * returns the level of the SubPlan
	 */
	public int getLevel()
	{
		return level;
	}
	
	/*int getQuota()
	 * returns the max quota of the SubPlan
	 */
	public int getQuota()
	{
		return quota;
	}
	
	/*ArrayList<SubPlan> getSubPlans()
	 * Accesses the database to get an ArrayList of all
	 * available SubPlans
	 */
	static public ArrayList<SubPlan> getSubPlans()
	{
		ArrayList<SubPlan> list = new ArrayList<SubPlan>();
		
		Connect connect = new Connect();
		Connection connection = connect.connect();
		Statement statement;
		
		try
		{
			statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Sub_Plan");
			
			while(resultSet.next())
				list.add(new SubPlan(resultSet.getInt("level_id"), resultSet.getInt("total_quota")));
			
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
}