package taxi.deadlock;

import java.awt.Point;

/**
 * This Taxi implementation would invoked the synchronized
 * Dispatcher.notifyAvailable() method while holding the own lock. This can
 * potentially cause a deadlock
 */
public class Taxi {
	private static final int SET_LOCATION_DELAY = 1;
	private Point location, destination;
	private Dispatcher dispatcher;

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public synchronized Point getLocation() {
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