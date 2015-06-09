package netflow;

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
}
