package netflow.processing.versions;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import netflow.NetflowEntry;
import netflow.Util;
import netflow.processing.InvalidPacketException;

/**
 * V5 of the netflow protocol
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
 */
public final class ProtocolV5 implements ProtocolInterface
{
	private final static int HEADER_LENGTH = 24;
	private final static int FLOW_LENGTH = 48;
	

	@Override
	public final NetflowEntry process(DatagramPacket packet) throws InvalidPacketException
	{
		
		byte[] data = packet.getData();
		
		int count = Util.bytesToUnsignedShort(data, 2);
		NetflowEntry head = null;
		NetflowEntry prevflow = null;
		NetflowEntry netflw = null;
		if(packet.getLength() != HEADER_LENGTH + FLOW_LENGTH * count)
		{
			throw new InvalidPacketException("Length of packet is incorrect");
		}

		System.out.printf("Got a v5 packet from %s [bytes=%d, count=%d]\n    %s\n",
				packet.getAddress().toString(), // person who sent us the packet
				packet.getLength(), // length of packet
				count, // number of flows
				Util.bytesToHex(data, packet.getLength())); // the data as a string
		
		// buffer to temporarily store IP address bytes in
		byte[] addrBytes = new byte[4];
		
		int offset = -1;
		// for each packet
		for (int i = 0; i < count; i++)
		{
			// offset of the first byte in this flow. Flows are 48 bytes long.
			offset = i * FLOW_LENGTH + HEADER_LENGTH;
			
			// get the source address
			InetAddress sourceAddr = null;
			System.arraycopy(data, offset + 0, addrBytes, 0, 4);
			try
			{
				sourceAddr = InetAddress.getByAddress(addrBytes);
			}
			catch (UnknownHostException e)
			{
				throw new InvalidPacketException("souce address is invalid");
			}
			
			// get the destination address
			InetAddress destAddr = null;
			System.arraycopy(data, offset + 4, addrBytes, 0, 4);
			try
			{
				destAddr = InetAddress.getByAddress(addrBytes);
			}
			catch (UnknownHostException e)
			{
				throw new InvalidPacketException("destination address is invalid");
			}
			
			short protocolType = Util.bytesToUnsignedByte(data, offset + 38);
			int sourcePort = Util.bytesToUnsignedShort(data, offset + 32);
			int destinationPort = Util.bytesToUnsignedShort(data, offset + 34);
			
			System.out.printf(    "%s --> %s\n", sourceAddr.toString(), destAddr.toString());
			
			prevflow = netflw;
			netflw = new NetflowEntry(5, sourceAddr, destAddr, protocolType, sourcePort, destinationPort);
			
			if (prevflow != null) // if not the first entry
			{
				prevflow.setNextEntry(netflw);
			}
			else // first entry
			{
				head = netflw;
			}
		}
		
		return head;
	}	
	
}
