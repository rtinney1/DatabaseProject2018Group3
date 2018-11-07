/*RentException.java
 * Created by: Randi Tinney
 * Created On: Nov 4 2018
 * Updated On: Nov 4 2018
 * Description: RentException.java is used when renting an entertainment
 * 		object
 */

package application;


public class RentException extends Exception 
{
	  public RentException(String message)
	  {
	    super(message);
	  }
}
