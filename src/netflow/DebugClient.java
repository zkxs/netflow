package netflow;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DebugClient
{	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		InetAddress addr = InetAddress.getLoopbackAddress();
		int port = 9010;
		
		final DatagramSocket socket = new DatagramSocket();
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			@Override
			public void run(){
				socket.close();
			}
		}));
		
		
		byte[] buf = new byte[17];
		buf[1] = 5;
		System.arraycopy(new byte[]{(byte)0xDE, (byte)0xAD, (byte)0xBE, (byte)0xEF}, 0, buf, 13, 4);
		DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, port);
		
		
		System.out.println("running...");
		
		for (int i = 1; i <= 7; i++)
		{
			buf[3] = (byte)i;
			socket.send(packet);
			Thread.sleep(1000);
		}
	}
}
