package netflow;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.io.udpreceive.DatagramReceiver;
import netflow.io.udpreceive.PacketManager;
import netflow.processing.DatagramProcessor;


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
	private LinkedList<DatagramProcessor> processors;
	private Object serverLock = new Object();
	private Object processorLock = new Object();
	
	// begin constructors
	
	public NetflowCollector()
	{
		packetManager = new PacketManager(BUFFER_SIZE);
		toProcessQueue = new ConcurrentLinkedQueue<>();
		toStoreQueue = new ConcurrentLinkedQueue<>();
		servers = new LinkedList<>();
		processors = new LinkedList<>();
	}
	
	
	// begin methods
	
	public void listen(int port)
	{
		listen(port, true);
		process();
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
	
	/**
	 * Add a new processing thread
	 */
	public void process()
	{
		DatagramProcessor processor = new DatagramProcessor(this);
		processors.add(processor);
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

	public Object getServerLock()
	{
		return serverLock;
	}

	public Object getProcessorLock()
	{
		return processorLock;
	}
}
