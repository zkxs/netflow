package netflow;
import java.net.SocketException;

import netflow.io.udpreceive.DatagramReceiver;


public class NetflowCollector
{	
	public static void main(String[] args)
	{
		try
		{
			DatagramReceiver server = new DatagramReceiver(9010);
		}
		catch (SocketException e)
		{
			Util.die(e);
		}
	}
}
