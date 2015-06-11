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
	private int count;
	private long sys_uptime;
	
	private InetAddress sourceAddress;
	private InetAddress destinationAddress;
	
	
	public NetflowEntry getNextEntry() {
		return nextEntry;
	}
	public int getVersion() {
		return version;
	}
	public int getCount() {
		return count;
	}
	public long getSys_uptime() {
		return sys_uptime;
	}
	public InetAddress getSourceAddress() {
		return sourceAddress;
	}
	public InetAddress getDestinationAddress() {
		return destinationAddress;
	}
	
	//TODO: Bailey, add more fields here, and maybe a constructor
}
