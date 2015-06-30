package taxi.deadlock_free_v2;

import java.awt.Point;

/**
 * This is another solution of Taxi implementation. We make the location field
 * volatile in order to make sure that threads accessing the Taxi instance
 * access the non-cached location value. Therefore getLocation method does not
 * need to be synchronized and thus the deadlock is avoided
 */
public class Taxi {
	private static final int SET_LOCATION_DELAY = 1;
	private Point destination;
	private volatile Point location;
	private Dispatcher dispatcher;

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public Point getLocation() {
		return location;
	}

	public synchronized void setLocation(Point location) {
		try {
			Thread.sleep(SET_LOCATION_DELAY);
		} catch (InterruptedException e) {
			return;
		}
		this.location = location;
		if (location.equals(destination))
			dispatcher.notifyAvailable(this);
	}
}