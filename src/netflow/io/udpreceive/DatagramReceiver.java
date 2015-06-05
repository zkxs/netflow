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
	
	public DatagramReceiver(final int portNumber, PacketManager packetManager,
			ConcurrentLinkedQueue<DatagramPacket> outputQueue) throws SocketException
	{
		socket = new DatagramSocket(portNumber);
		this.packetManager = packetManager;
		this.outputQueue = outputQueue;
		
		(new DatagramThread("DatagramThread(" + portNumber + ")")).start();
		(new Thread("derp") {
			public void run() {
				try {
					DatagramSocket s = new DatagramSocket();
					byte[] buf = {99};
					DatagramPacket p = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), portNumber);
					while (true) {
						s.send(p);
						Thread.sleep(60000);
					}
				}
				catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
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
				while (true)
				{
					socket.receive(packet);
					//TODO: handle packet
					
					byte[] buf = packet.getData();
					if (buf[0] == 99)
					{
						System.out.printf("Got a test packet\n");
					}
					else
					{							
						System.out.printf("Got a REAL packet from %s [%d]\n", packet.getAddress().toString(), packet.getLength());
						System.out.printf("%s\n", netflow.Util.bytesToHex(buf, packet.getLength()));
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
