package taxi.deadlock_free;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

public class TaxiDriver implements Runnable {
	private final Taxi taxi;
	private final List<Point> route;

	public TaxiDriver(Taxi taxi, List<Point> route) {
		this.taxi = taxi;
		this.route = route;
	}

	@Override
	public void run() {
		this.taxi.setDestination(route.get(route.size() - 1));
		final Iterator<Point> routeIterator = route.iterator();
		while (!Thread.currentThread().isInterrupted()
				&& routeIterator.hasNext()) {
			Point nextLocation = routeIterator.next();
			System.out.println("Taxi driver is setting taxi location to "
					+ nextLocation);
			this.taxi.setLocation(nextLocation);
		}
	}
}
