package taxi.deadlock_free;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This Dispatcher implementation would use an open call when invoking the alien
 * Taxi.getLocation method thus avoiding the deadlock.<br>
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

	public Image getImage() {
		Set<Taxi> copy;
		synchronized (this) {
			copy = new HashSet<>(taxis);
		}
		Image image = new Image();
		copy.forEach(taxi -> image.drawMarker(taxi.getLocation()));
		return image;
	}
}