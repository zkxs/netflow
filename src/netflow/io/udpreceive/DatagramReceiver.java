package netflow.io.udpreceive;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



/**
 * Listens for incoming datagrams
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 5, 2015
 */
public class DatagramReceiver
{	
	final static int BUFFER_SIZE = 1464;
	
	
	private DatagramSocket socket;
	private PacketManager bufferManager;
	
	public DatagramReceiver(final int portNumber) throws SocketException
	{
		socket = new DatagramSocket(portNumber);
		bufferManager = new PacketManager(BUFFER_SIZE);
		
		(new DatagramThread("name name")).start();
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
			DatagramPacket packet = bufferManager.get();
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
