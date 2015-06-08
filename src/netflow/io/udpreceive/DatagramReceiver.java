package netflow.io.udpreceive;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;



/**
 * Listens for incoming datagrams
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 5, 2015
 */
public class DatagramReceiver
{	
	
	
	private DatagramSocket socket;
	private PacketManager packetManager;
	private ConcurrentLinkedQueue<DatagramPacket> outputQueue;
	
	private boolean running;
	
	public DatagramReceiver(final int portNumber, PacketManager packetManager,
			ConcurrentLinkedQueue<DatagramPacket> outputQueue) throws SocketException
	{
		socket = new DatagramSocket(portNumber);
		this.packetManager = packetManager;
		this.outputQueue = outputQueue;
		
		running = true;
		
		(new DatagramThread("DatagramThread(" + portNumber + ")")).start();
	}
	
	public void stop()
	{
		running = false;
		socket.close();
	}
	
	private class DatagramThread extends Thread
	{

		public DatagramThread(String name)
		{
			super(name);
			setDaemon(false); //TODO: remove
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
					
					byte[] buf = packet.getData();
					
					System.out.printf("Got a packet from %s [%d]\n", packet.getAddress().toString(), packet.getLength());
					System.out.printf("%s\n", netflow.Util.bytesToHex(buf, packet.getLength()));
					
					//TODO: handle packet
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
