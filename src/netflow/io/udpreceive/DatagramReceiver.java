package netflow.io.udpreceive;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
					socket.receive(packet);
					
					//TODO: handle packet
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
				thread.notifyAll();
			}
		}
	}
}
