package netflow.processing;

import java.net.DatagramPacket;

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
	
	static
	{
		protocols = new ProtocolInterface[MAX_PROTOCOL_VERSION];
		
		protocols[5] = new ProtocolV5();
	}
	
	
	public static void process(DatagramPacket packet)
	{
		if (packet.getLength() >= MIN_PACKET_LENGTH)
		{
			int version = Util.bytesToUnsignedShort(packet.getData(), 0);
			
			System.out.printf("Got a v%d packet from %s [%d]\n", version, packet.getAddress().toString(), packet.getLength());
			System.out.printf("%s\n", netflow.Util.bytesToHex(packet.getData(), packet.getLength()));
			
			if (protocols[version] != null)
			{
				NetflowEntry entry = protocols[version].process(packet);
			}
			else
			{
				//TODO: invalid protocol version
			}
		}
		else
		{
			//TODO: packet too short
		}
		
	}
	
	
	private static int id = 0;
	private NetflowCollector collector;
	private boolean running;
	private ProcessorThread thread;
	
	public DatagramProcessor(NetflowCollector collector)
	{
		this.collector = collector;
		
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
			
			}
			
			synchronized(thread)
			{
				thread.notifyAll();
			}
		}
	}
}
