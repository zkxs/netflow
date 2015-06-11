package netflow.processing;

/**
 * Thrown when a packet is invalid and could not be parsed
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 11, 2015
 */
public class InvalidPacketException extends Exception
{
	public InvalidPacketException()
	{
		super("Invalid Packet");
	}

	public InvalidPacketException(String message)
	{
		super(message);
	}	
}
