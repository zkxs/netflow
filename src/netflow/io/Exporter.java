package netflow.io;

import netflow.NetflowEntry;

public interface Exporter
{	
	public void Export(NetflowEntry netflow);
}
