
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

public class RentHistory
{
	int eid;
	int rid;
	long count;
	String title;
	Date dateRented;
	Date dateReturned;
	User user;
	String name;
	String address;
	
	public RentHistory(int eid, int rid, String title, String email, Date dateRented)
	{
		String a[];
		
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		System.out.println("INSIDE RENT HISTORY CONSTRUCTOR");
		System.out.println(email);
		this.user = new User(email);
		name = this.user.getName();
		
		a = this.user.getAddress().split(Pattern.quote("$"));
		
		address = "";
		for(int i = 0; i < a.length; i++)
			address = address + a[i] + " ";
		
		System.out.println(name + " " + address);
		this.dateRented = dateRented;
	}
	
	public RentHistory(int eid, String title, int rid, long count)
	{
		this.eid = eid;
		this.title = title;
		this.rid = rid;
		this.count = count;
	}
	
	public RentHistory(int eid, int rid, String title, Date dateRented)
	{
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		this.dateRented = dateRented;
	}
	
	public RentHistory(int eid, int rid, String title, Date dateRented, Date dateReturned)
	{
		this.eid = eid;
		this.rid = rid;
		this.title = title;
		this.dateRented = dateRented;
		this.dateReturned = dateReturned;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUserEmail()
	{
		String email = user.getEmail();
		return email;
	}
	
	public long getCount()
	{
		return count;
	}
	
	public int getRID()
	{
		return rid;
	}
	
	public int getEID()
	{
		return eid;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Date getDateRented()
	{
		return dateRented;
	}
	
	public Date getDateReturned()
	{
		return dateReturned;
	}
}
