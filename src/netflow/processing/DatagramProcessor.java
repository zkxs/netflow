package netflow.processing;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.NetflowCollector;
import netflow.NetflowEntry;
import netflow.Util;
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
	
	// static initialization of the static fields
	static
	{
		protocols = new ProtocolInterface[MAX_PROTOCOL_VERSION];
		
		protocols[5] = new ProtocolV5();
	}
	
	/**
	 * Process a single datagram packet. The results of the processing will be sent to an output queue.
	 * @param packet The packet to process
	 */
	public void process(DatagramPacket packet)
	{
		if (packet.getLength() >= MIN_PACKET_LENGTH)
		{
			int version = Util.bytesToUnsignedShort(packet.getData(), 0) - 1; // start counting at 0
			
			System.out.printf("Got a v%d packet from %s [%d]\n", version, packet.getAddress().toString(), packet.getLength());
			System.out.printf("%s\n", netflow.Util.bytesToHex(packet.getData(), packet.getLength()));
			
			if (version < MAX_PROTOCOL_VERSION && protocols[version] != null)
			{
				NetflowEntry entry = protocols[version].process(packet);
				
				/* TODO: stick the entry (entries???) in the output queue
				 * A packet can contain multiple entries. I think I'll store these as a linked list by adding
				 * a NetflowEntry field within NetflowEntry.
				 */
				
				// free the DatagramPacket for reuse
				collector.getPacketManager().free(packet);
			}
			else
			{
				//TODO: invalid protocol version
				System.err.println("invalid protocol version");
			}
		}
		else
		{
			//TODO: packet too short
			System.err.println("packet too short");
		}
		
	}
	
	
	private static int id = 0;
	private NetflowCollector collector;
	private boolean running;
	private ProcessorThread thread;
	private ConcurrentLinkedQueue<DatagramPacket> inputQueue;
	private ConcurrentLinkedQueue<NetflowEntry> outputQueue;
	private Object lock;
	
	/**
	 * Construct a new DatagramProcessor
	 * @param collector The NetflowCollector this DatagramReceiver is a member of
	 */
	public DatagramProcessor(NetflowCollector collector)
	{
		this.collector = collector;
		inputQueue = collector.getToProcessQueue();
		outputQueue = collector.getToStoreQueue();
		lock = collector.getProcessorLock();
		
		running = true;
		
		thread = new ProcessorThread("ProcessorThread(" + id++ + ")");
		thread.start();
	}
	
	public void stop()
	{
		running = false;
		
		synchronized(thread)
		{
			try
			{
				thread.wait();
			}
			catch (InterruptedException e)
			{
				Util.die(e);
			}
		}
	}
	
	/**
	 * Thread that processes DatagramPackets
	 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
	 */
	private class ProcessorThread extends Thread
	{
		public ProcessorThread(String name)
		{
			super(name);
			setDaemon(false); //TODO: remove or handle shutdown
		}
		
		@Override
		public void run()
		{
			while (running)
			{
				DatagramPacket packet;
				if ((packet = inputQueue.poll()) != null)
				{
					// 
				}
				else
				{
					synchronized (lock)
					{
						try
						{
							lock.wait();
						}
						catch (InterruptedException e)
						{
							Util.die(e);
						}
					}
				}
			}
			
			synchronized(thread)
			{
				thread.notifyAll();
			}
		}
	}
}
