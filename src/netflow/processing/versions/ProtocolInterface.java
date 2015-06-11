package netflow.processing.versions;

import java.net.DatagramPacket;

import netflow.NetflowEntry;
import netflow.processing.InvalidPacketException;

/**
 * An interface that the different implementations of the netflow protocol will implement
 * @see {@link netflow.processing.versions.ProtocolV5}
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
 */
public interface ProtocolInterface
{	
	/**
	 * Process a single datagram
	 * @param packet the datagram to process
	 * @return an object representing a netflow entry
	 * @throws InvalidPacketException 
	 */
	public NetflowEntry process(DatagramPacket packet) throws InvalidPacketException;
}
