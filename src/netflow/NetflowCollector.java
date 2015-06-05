package netflow;
import java.net.SocketException;

import netflow.io.udpreceive.DatagramReceiver;
import netflow.io.udpreceive.PacketManager;


public class NetflowCollector
{	
	final static int BUFFER_SIZE = 1464;
	
	public static void main(String[] args)
	{
		NetflowCollector collector = new NetflowCollector();
		collector.listen(9010);
	}
	
	// begin fields
	
	private PacketManager packetManager;
	
	
	// begin constructors
	
	public NetflowCollector()
	{
		packetManager = new PacketManager(BUFFER_SIZE);
		
		
	}
	
	
	// begin methods
	
	public void listen(int port)
	{
		try
		{
			DatagramReceiver server = new DatagramReceiver(port, packetManager);
		}
		catch (SocketException e)
		{
			Util.die(e); //TODO: print error message?
		}
	}
}
