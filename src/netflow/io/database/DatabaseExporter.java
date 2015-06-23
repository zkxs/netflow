package netflow.io.database;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;

import netflow.NetflowCollector;
import netflow.NetflowEntry;
import netflow.Stoppable;
import netflow.Util;
import netflow.io.Exporter;

public class DatabaseExporter implements Exporter, Stoppable
{
	private static int id = 0;
	private NetflowCollector collector;
	private boolean running;
	private ExporterThread thread;
	private ConcurrentLinkedQueue<NetflowEntry> inputQueue;
	private Object lock;
	
	public DatabaseExporter(NetflowCollector collector)
	{
		this.collector = collector;
		inputQueue = collector.getToStoreQueue();
		lock = collector.getProcessorLock();
		
		running = true;
		
		thread = new ExporterThread("ExporterThread(" + id++ + ")");
		thread.start();
	}
	
	public Exporter getInstance(NetflowCollector collector)
	{
		return new DatabaseExporter(collector);
	}
	
	private void export(NetflowEntry netflow)
	{
		
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
	
	/**
	 * Thread that processes NetflowEntries
	 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 23, 2015
	 */
	private class ExporterThread extends Thread
	{
		public ExporterThread(String name)
		{
			super(name);
			setDaemon(false); //TODO: remove or handle shutdown
		}
		
		@Override
		public void run()
		{
			while (running)
			{
				NetflowEntry entry;
				if ((entry = inputQueue.poll()) != null)
				{
					try
					{
						// output entry to database
						export(entry);
					}
					catch (Exception e)
					{
						// TODO reconnect to database
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
		}
	}
}
