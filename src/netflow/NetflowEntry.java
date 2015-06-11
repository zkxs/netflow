package netflow;

import java.net.InetAddress;

/**
 * Stores the data for a single netflow entry. Some datagrams may contain multiple entries,
 * so a linked list structure using {@link #nextEntry} is created.
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
 */
public class NetflowEntry
{	
	/** The next netflow entry from a single datagram, or <code>null</code> if there are no more */
	private NetflowEntry nextEntry;
	
	private int version;
	//private int count;
	
	private InetAddress sourceAddress;
	private InetAddress destinationAddress;
	
	/** Gets the next Netflow
	 * 
	 * @return nextEntry the next Netflow from the packet
	 */
	public NetflowEntry getNextEntry() 
	{
		return nextEntry;
	}
	
	/** Gets the version of the packet
	 * 
	 * @return version the version of the packet
	 */
	
	public int getVersion() 
	{
		return version;
	}

	
	/** Gets the Source Address of the Netflow
	 * 
	 * @return sourceAddress the source of the Netflow
	 */
	public InetAddress getSourceAddress() 
	{
		return sourceAddress;
	}
	
	/** Gets the Destination Address of the Netflow
	 * 
	 * @return destinationAddress the destination of the Netflow
	 */
	public InetAddress getDestinationAddress() 
	{
		return destinationAddress;
	}
	
	/** Constructor for a Netflow
	 * 
	 * @param vers version of the packet
	 * @param uptime the sytem uptime
	 * @param src the source of the Netflow
	 * @param dest the destination of the Netflow
	 * @param nxt the next Netflow entry
	 */
	public NetflowEntry(int vers, InetAddress src, InetAddress dest, NetflowEntry nxt)
	{
		version = vers;
		sourceAddress = src;
		destinationAddress = dest;
		nextEntry = nxt;
	}
	//TODO: Bailey, add more fields here, and maybe a constructor
	//TODO: MIchael, Time stuff
}
