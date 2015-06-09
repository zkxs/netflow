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
	/** The size of the buffer in which to store datagrams */
	final static int BUFFER_SIZE = 1 << 12; // 2^12 = 4096
	
	public static void main(String[] args)
	{
		NetflowCollector collector = new NetflowCollector();
		collector.listen(9010);
		collector.process();
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
	
	/**
	 * Begin listening on a port. Failures to bind to the port will crash the entire program.
	 * @param port the port to listen on
	 */
	public void listen(int port)
	{
		listen(port, true);
	}
	
	/**
	 * Begin listening on a port
	 * @param port the port to listen on
	 * @param failHard Should exceptions crash the entire program?
	 */
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
	
	/**
	 * Get this collector's Packet Manager
	 * @return this collector's Packet Manager
	 */
	public PacketManager getPacketManager()
	{
		return packetManager;
	}

	/**
	 * Get this collector's "to process" queue
	 * @return this collector's "to process" queue
	 */
	public ConcurrentLinkedQueue<DatagramPacket> getToProcessQueue()
	{
		return toProcessQueue;
	}

	/**
	 * Get this collector's "to store" queue
	 * @return this collector's "to store" queue
	 */
	public ConcurrentLinkedQueue<NetflowEntry> getToStoreQueue()
	{
		return toStoreQueue;
	}

	/**
	 * Get a lock for use by the server threads.
	 * Currently unused.
	 * @return a lock for use by the server threads
	 */
	@Deprecated
	public Object getServerLock()
	{
		return serverLock;
	}

	/**
	 * Get a lock for use by the processing threads.
	 * @return a lock for use by the processing threads
	 */
	public Object getProcessorLock()
	{
		return processorLock;
	}
}
