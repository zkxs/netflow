package netflow.processing;

import java.net.DatagramPacket;

import netflow.processing.versions.*;

/**
 * Process the raw data of a netflow datagram into a Java data structure
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 8, 2015
 */

public class DatagramProcessor
{	
	private final static int MIN_PACKET_LENGTH = 17; // header in protocol v1 (nothing is smaller)
	private final static int MAX_PROTOCOL_VERSION = 9;
	private final static ProtocolInterface[] protocols;
	
	static
	{
		protocols = new ProtocolInterface[MAX_PROTOCOL_VERSION];
		
		protocols[5] = new ProtocolV5();
	}
	
	
	public static void process(DatagramPacket d)
	{
		if (d.getLength() >= MIN_PACKET_LENGTH)
		{
			byte[] buf = d.getData();
		}
		
		
	}
	
	
	
}
