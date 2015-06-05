package io.udpreceive;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages the creation of new packets (and their underlying buffers) as needed
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 5, 2015
 */
class PacketManager
{
	final private int bufferSize;
	ConcurrentLinkedQueue<DatagramPacket> queue;
	
	/**
	 * Construct a new PacketManager
	 * @param bufferSize the size (in bytes) of the bufers
	 */
	PacketManager(int bufferSize)
	{
		this.bufferSize = bufferSize;
		queue = new ConcurrentLinkedQueue<>();
	}
	
	/**
	 * Get a datagram packet
	 * @return a packet you may use, and should return later with {@link #free()}
	 */
	DatagramPacket get()
	{
		DatagramPacket packet;
		if ((packet = queue.poll()) == null)
		{
			packet = new DatagramPacket(new byte[bufferSize], bufferSize);
		}
		return packet;
	}
	
	/**
	 * Return a packet to the queue for reuse
	 * @param packet the packet to free
	 */
	void free(DatagramPacket packet)
	{
		queue.add(packet);
	}
}
