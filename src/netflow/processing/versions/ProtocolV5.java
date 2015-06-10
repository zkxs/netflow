package netflow.processing.versions;

import java.net.DatagramPacket;

import netflow.NetflowEntry;

/**
 * V5 of the netflow protocol
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
 */
public final class ProtocolV5 implements ProtocolInterface
{

	@Override
	public final NetflowEntry process(DatagramPacket packet)
	{
		// TODO Bailey, make this all work
		
		System.out.printf("Got a v5 packet from %s [%d]\n    %s\n",
				packet.getAddress().toString(),
				packet.getLength(),
				netflow.Util.bytesToHex(packet.getData(), packet.getLength())); // the data as a string
		
		
		
		return null;
	}	
	
}
