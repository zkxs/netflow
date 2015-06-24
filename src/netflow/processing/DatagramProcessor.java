package netflow.processing;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.NetflowCollector;
import netflow.NetflowEntry;
import netflow.Stoppable;
import netflow.Util;
import netflow.processing.versions.*;

/**
 * Process the raw data of a netflow datagram into a Java data structure
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 8, 2015
 */
public class DatagramProcessor implements Stoppable
{	
	private final static int MIN_PACKET_LENGTH = 17; // header in protocol v1 (nothing is smaller)
	private final static int MAX_PROTOCOL_VERSION = 9;
	private final static ProtocolInterface[] protocols;
	
	// static initialization of the static fields
	static
	{
		protocols = new ProtocolInterface[MAX_PROTOCOL_VERSION];
		
		protocols[5 - 1] = new ProtocolV5();
	}
	
	/**
	 * Process a single datagram packet. The results of the processing will be sent to an output queue.
	 * @param packet The packet to process
	 */
	private void process(DatagramPacket packet) throws InvalidPacketException
	{
		if (packet.getLength() >= MIN_PACKET_LENGTH)
		{
			int version = Util.bytesToUnsignedShort(packet.getData(), 0) - 1; // start counting at 0
			
			if (version < MAX_PROTOCOL_VERSION && protocols[version] != null)
			{
				// process the packet
				NetflowEntry entry = protocols[version].process(packet);
				
				// free the DatagramPacket for reuse
				collector.getPacketManager().free(packet);
				
				/* Stick all the entries in the output queue.
				 * A packet can contain multiple entries. These are stored as a linked list.
				 */
				while (entry != null)
				{
					// add a single entry
					outputQueue.add(entry);
					
					// get the next entry in the chain, if it exists
					entry = entry.getNextEntry();
					
					// signal that a new entry has been enqueued
					collector.signalNewEntry();
				}
			}
			else
			{
				throw new InvalidPacketException(String.format("invalid protocol version: %d", version));
			}
		}
		else
		{
			throw new InvalidPacketException(String.format("packet too short: only %d bytes", packet.getLength()));
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
			if (thread.isAlive())
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
					try
					{
						process(packet);
					}
					catch (InvalidPacketException e)
					{
						// TODO Log invalid packets
						e.printStackTrace();
					}
				}
				else // the queue is empty
				{
					synchronized (lock)
					{
						try
						{
							// wait for the signal that a new packet is in the queue
							lock.wait();
						}
						catch (InterruptedException e)
						{
							Util.die(e);
						}
					}
				}
			}
			
			// signal threads waiting on us to stop that we have stopped
			synchronized(thread)
			{
				thread.notifyAll();
			}
		}
	}
}
