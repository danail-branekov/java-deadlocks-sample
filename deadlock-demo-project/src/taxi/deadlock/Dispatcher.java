package taxi.deadlock;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This Dispatcher implementation would invoke the alien method Taxi.setLocation
 * while holding the own monitor. This can potentially cause a deadlock
 *
 */
public class Dispatcher {
	private final Set<Taxi> taxis;
	private final Set<Taxi> availableTaxis;

	public Dispatcher(Taxi... taxis) {
		this.taxis = new HashSet<Taxi>(Arrays.asList(taxis));
		this.availableTaxis = new HashSet<Taxi>(Arrays.asList(taxis));
	}

	public synchronized void notifyAvailable(Taxi taxi) {
		availableTaxis.add(taxi);
	}

	public synchronized Image getImage() {
		Image image = new Image();
		taxis.forEach(taxi -> image.drawMarker(taxi.getLocation()));
		return image;
	}
}