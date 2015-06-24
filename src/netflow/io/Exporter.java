package netflow.io;

import netflow.NetflowCollector;

public abstract class Exporter
{	
	private NetflowCollector collector;
	
	/**
	 * Constructor for Exporters.
	 * @param collector
	 */
	public Exporter(NetflowCollector collector)
	{
		this.collector = collector;
	}
	
	/**
	 * Get the netflow collector for this Exporter
	 * @return
	 */
	protected NetflowCollector getNetflowCollector()
	{
		return collector;
	}
}
