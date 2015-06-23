package netflow.io;

import netflow.NetflowCollector;

/**
 * This is a flag that 
 * @author Michael Ripley (<a href="mailto:michael-ripley@utulsa.edu">michael-ripley@utulsa.edu</a>) Jun 23, 2015
 */
public abstract class Exporter
{	
	/**
	 * Get an instance of this type of exporter
	 * @param collector
	 * @return
	 */
	public static Exporter getInstance(NetflowCollector collector);
}
