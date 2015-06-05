package netflow;
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
	final public static void die(Exception e)
	{
		e.printStackTrace();
		//TODO: release resources? perhaps via shutdown hooks?
		System.exit(1);
	}
	
	final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	final public static String bytesToHex(byte[] bytes, int length)
	{
		length = Math.min(bytes.length, length);
		char[] hexChars = new char[length * 2];
		for (int j = 0; j < length; j++)
		{
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
