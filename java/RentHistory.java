/*RentHistory.java
 * Created by: Randi Tinney
 * Created On: Nov 2 2018
 * Updated On: Nov 4 2018
 * Description: RentHistory.java creates an object of RentHistory that stores
 * 		all of the information as it appears in the database. This class provides methods
 * 		on returning the information as it appears in the database
 */


import java.sql.Timestamp;
import java.util.regex.Pattern;

public class RentHistory
{
	int eid;
	int rid;
	long count;
	String title;
	Timestamp dateRented;
	Timestamp dateReturned;
	User user;
	String name;
	String street;
	String city;
	String state;
	int zip;
	
	/*Constructor with all of the information except for dateReturned passed
	 * 
	 */
	public RentHistory(int eid, int rid, String title, String email, Timestamp dateRented)
	{
		String a[];
		
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		this.user = new User(email);
		name = this.user.getName();
		this.street = this.user.getStreet();
		this.city = this.user.getCity();
		this.state = this.user.getState();
		this.zip = this.user.getZip();
		this.dateRented = dateRented;
	}
	
	/*Constructor with count passed which is how many times
	 * that item was rented
	 */
	public RentHistory(int eid, String title, int rid, long count)
	{
		this.eid = eid;
		this.title = title;
		this.rid = rid;
		this.count = count;
	}
	
	/*Constructor without any user information passed and no dateReturned
	 * 
	 */
	public RentHistory(int eid, int rid, String title, Timestamp dateRented)
	{
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		this.dateRented = dateRented;
	}
	
	/*Constructor with all information except user information
	 * 
	 */
	public RentHistory(int eid, int rid, String title, Timestamp dateRented, Timestamp dateReturned)
	{
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		this.dateRented = dateRented;
		this.dateReturned = dateReturned;
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
	
	/*String getName()
	 * returns the name of the user who rented the object
	 */
	public String getName()
	{
		return name;
	}
	
	/*String getUserEmail()
	 * returns the email of the user who rented the object
	 */
	public String getUserEmail()
	{
		String email = user.getEmail();
		return email;
	}
	
	/*long getCount()
	 * returns the count of the rented object
	 */
	public long getCount()
	{
		return count;
	}
	
	/*int getRID()
	 * returns the rid of the RentHistory
	 */
	public int getRID()
	{
		return rid;
	}
	
	/*int getEID()
	 * returns the eid of the rented object
	 */
	public int getEID()
	{
		return eid;
	}
	
	/*String getTitle()
	 * returns the title of the rented object
	 */
	public String getTitle()
	{
		return title;
	}
	
	/*Timestamp getDateRented()
	 * returns the date and time the object was rented
	 */
	public Timestamp getDateRented()
	{
		return dateRented;
	}
	
	/*Timestamp getDateReturned
	 * returns the date and time the object was returned
	 */
	public Timestamp getDateReturned()
	{
		return dateReturned;
	}
}
