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
	private short protocolType;
	private int sourcePort;
	private int destinationPort;
	
	/** 
	 * Some packets contain multiple netflow entries.  These are stored as a linked list by the
	 * processing code.  The returned entry is the head of a linked list containing all the entries from a packet.
	 * If any entry contains invalid data, the entire packet is dropped during processing.  This method gets the
	 * next entry in the chain.
	 * @return nextEntry the next Netflow from the packet
	 */
	public NetflowEntry getNextEntry() 
	{
		return nextEntry;
	}
	
	/** 
	 * Gets the version of the packet
	 * @return version the version of the packet
	 */
	
	public int getVersion() 
	{
		return version;
	}

	
	/**
	 * Gets the Source Address of the Netflow
	 * @return sourceAddress the source of the Netflow
	 */
	public InetAddress getSourceAddress() 
	{
		return sourceAddress;
	}
	
	/**
	 * Gets the Destination Address of the Netflow
	 * @return destinationAddress the destination of the Netflow
	 */
	public InetAddress getDestinationAddress() 
	{
		return destinationAddress;
	}
	
	/**
	 * Gets the Protocol Type of the netflow
	 * @return protocolType
	 */
	public short getProtocolType() {
		return protocolType;
	}
	
	/**
	 * gets the source port of the netflow
	 * @return sourcePort the source of the netflow
	 */
	public int getSourcePort() {
		return sourcePort;
	}

	/**
	 * gets the destination port of the netflow
	 * @return destinationPort the destination of the netflow
	 */
	public int getDestinationPort() {
		return destinationPort;
	}

	/**
	 * Set the next netflow 
	 * @param nextEntry
	 */
	public void setNextEntry(NetflowEntry nextEntry) {
		this.nextEntry = nextEntry;
	}

	/**
	 * Constructor for a Netflow
	 * @param vers version of the packet
	 * @param uptime the sytem uptime
	 * @param src the source of the Netflow
	 * @param dest the destination of the Netflow
	 * @param nxt the next Netflow entry
	 */
	public NetflowEntry(int vers, InetAddress src, InetAddress dest, short protType, int srcport, int destport)
	{
		version = vers;
		sourceAddress = src;
		destinationAddress = dest;
		protocolType = protType;
		sourcePort = srcport;
		destinationPort = destport;
	}
	
	//TODO: Michael, Time stuff
}
