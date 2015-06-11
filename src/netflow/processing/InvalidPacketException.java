package netflow.processing;

/**
 * Thrown when a packet is invalid and could not be parsed
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 11, 2015
 */
public class InvalidPacketException extends Exception
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new InvalidPacketException with no message.
	 */
	public InvalidPacketException()
	{
		super();
	}

	/**
	 * Constructs a new InvalidPacketException with the specified detail message
	 * @param message the detail message
	 */
	public InvalidPacketException(String message)
	{
		super(message);
	}	
}
