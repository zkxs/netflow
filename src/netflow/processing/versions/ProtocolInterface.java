package netflow.processing.versions;

import java.net.DatagramPacket;

import netflow.NetflowEntry;

public interface ProtocolInterface
{	
	public NetflowEntry process(DatagramPacket packet);
}
