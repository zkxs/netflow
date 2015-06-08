package netflow;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.io.udpreceive.DatagramReceiver;
import netflow.io.udpreceive.PacketManager;


public class NetflowCollector
{	
	final static int BUFFER_SIZE = 1 << 12; // 2^12 = 4096
	
	public static void main(String[] args)
	{
		NetflowCollector collector = new NetflowCollector();
		collector.listen(9010);
	}
	
	// begin fields
	
	private PacketManager packetManager;
	private ConcurrentLinkedQueue<DatagramPacket> toProcessQueue;
	private ConcurrentLinkedQueue<NetflowEntry> toStoreQueue;
	private LinkedList<DatagramReceiver> servers;
	
	// begin constructors
	
	public NetflowCollector()
	{
		packetManager = new PacketManager(BUFFER_SIZE);
		toProcessQueue = new ConcurrentLinkedQueue<>();
		toStoreQueue = new ConcurrentLinkedQueue<>();
		servers = new LinkedList<>();
	}
	
	
	// begin methods
	
	public void listen(int port)
	{
		listen(port, true);
	}
	
	public void listen(int port, boolean failHard)
	{
		try
		{
			DatagramReceiver server = new DatagramReceiver(this, port);
			servers.add(server);
		}
		catch (SocketException e)
		{
			if (failHard)
			{
				Util.die(e);
			}
			else
			{
				e.printStackTrace(); //TODO: print error message differently?
			}
		}
	}


	public PacketManager getPacketManager()
	{
		return packetManager;
	}


	public ConcurrentLinkedQueue<DatagramPacket> getToProcessQueue()
	{
		return toProcessQueue;
	}


	public ConcurrentLinkedQueue<NetflowEntry> getToStoreQueue()
	{
		return toStoreQueue;
	}
	
	
}
