package taxi.deadlock_free;

import java.awt.Point;

/**
 * This Taxi implementation uses an open call in its setLocation method - the
 * method is not synchronized, i.e. own lock is not held while being executed.
 * The own lock is held for a short period while updating the location field is
 * performed and is then released prior invoking the alien dispatcher method<br>
 * Further on, the location field is volatile which guarantees that all threads
 * accessing the Taxi object would "see" the correct taxi location rather than a
 * cached value. Thus we no longer need method getLocation to be synchronized
 */
public class Taxi {
	private static final int SET_LOCATION_DELAY = 1;
	private volatile Point location;
	private Point destination;
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

	public void setLocation(Point location) {
		try {
			Thread.sleep(SET_LOCATION_DELAY);
		} catch (InterruptedException e) {
			return;
		}

		boolean reachedDestination;
		synchronized (this) {
			this.location = location;
			reachedDestination = location.equals(destination);
		}
		if (reachedDestination) {
			dispatcher.notifyAvailable(this);
		}
	}
}