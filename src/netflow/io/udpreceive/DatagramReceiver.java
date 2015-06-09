package netflow.io.udpreceive;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.NetflowCollector;
import netflow.Stoppable;
import netflow.Util;



/**
 * Listens for incoming datagrams
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 5, 2015
 */
public class DatagramReceiver implements Stoppable
{	
	
	private NetflowCollector collector;
	private DatagramSocket socket;
	private PacketManager packetManager;
	private ConcurrentLinkedQueue<DatagramPacket> outputQueue;
	
	private boolean running;
	private ReceiverThread thread;
	
	/**
	 * Construct a new DatagramReceiver
	 * @param collector The NetflowCollector this DatagramReceiver is a member of
	 * @param portNumber The port to listen on
	 * @throws SocketException if the socket could not be opened (mainly if the port is already bound)
	 */
	public DatagramReceiver(NetflowCollector collector, final int portNumber) throws SocketException
	{
		this.collector = collector;
		socket = new DatagramSocket(portNumber);
		packetManager = collector.getPacketManager();
		outputQueue = collector.getToProcessQueue();
		
		running = true;
		
		thread = new ReceiverThread("ReceiverThread(" + portNumber + ")");
		thread.start();
	}
	
	public void stop()
	{
		running = false;
		socket.close();
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
	 * Thread that "listens" for incoming datagrams
	 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 9, 2015
	 */
	private class ReceiverThread extends Thread
	{
		public ReceiverThread(String name)
		{
			super(name);
			setDaemon(false); //TODO: remove or handle shutdown
		}

		@Override
		public void run()
		{
			DatagramPacket packet = packetManager.get();
			try
			{
				System.out.printf("Listening on port %d\n", socket.getLocalPort());
				while (running)
				{
					// block until packet received
					socket.receive(packet);
					// received packet has now been written into the buffer 'packet'
					
					// send packet to queue
					outputQueue.add(packet);
				}
			}
			catch (SocketException e)
			{
				if (running)
				{
					Util.die(e);
				}
				// TODO: handle sockets closing when they shouldn't
			}
			catch (IOException e)
			{
				Util.die(e);
			}
			
			synchronized(thread)
			{
				// notify threads that are waiting for this thread to stop
				thread.notifyAll();
			}
		}
	}
}
