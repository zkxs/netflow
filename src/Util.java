/**
 * Various utilities
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 5, 2015
 */
public final class Util
{	
	/**
	 * Do not allow construction
	 */
	private Util(){}
	
	/**
	 * Called when an unrecoverable error has occured and the program should shut down
	 * @param e The exception that caused the program to fail
	 */
	final static void die(Exception e)
	{
		e.printStackTrace();
		//TODO: release resources? perhaps via shutdown hooks?
		System.exit(1);
	}
}
