package netflow;
import java.net.DatagramPacket;
import java.net.SocketException;
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
	
	// begin constructors
	
	public NetflowCollector()
	{
		packetManager = new PacketManager(BUFFER_SIZE);
		toProcessQueue = new ConcurrentLinkedQueue<>();
		toStoreQueue = new ConcurrentLinkedQueue<>();
	}
	
	
	// begin methods
	
	public void listen(int port)
	{
		DatagramReceiver server = null;
		
		try
		{
			server = new DatagramReceiver(port, packetManager, toProcessQueue);
		}
		catch (SocketException e)
		{
			Util.die(e); //TODO: print error message?
		}
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			Util.die(e);
		}
		
		server.stop();
	}
}
