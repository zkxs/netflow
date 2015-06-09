package netflow;

public interface Stoppable
{	
	/**
	 * Stop this object from running as quickly and gracefully as possible. This method will block until this
	 * object is no longer running.
	 */
	public void stop();
}
