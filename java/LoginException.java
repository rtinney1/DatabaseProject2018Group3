/*LoginException.java
 * Created by: Randi Tinney
 * Created On: Oct 24 2018
 * Updated On: Oct 24 2018
 * Description: LoginException.java is to be used when logging a user
 * 		in or registering a new user
 */



public class LoginException extends Exception 
{
	  public LoginException(String message)
	  {
	    super(message);
	  }
}
