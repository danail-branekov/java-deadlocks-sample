package taxi.deadlock_free_v2;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaxiCompany {
	private final Dispatcher dispatcher;
	private final TaxiDriver taxiDriver;
	private final TaxiObserver taxiObserver;

	public TaxiCompany() {
		Taxi taxi = new Taxi();

		this.dispatcher = new Dispatcher(taxi);
		taxi.setDispatcher(this.dispatcher);

		this.taxiObserver = new TaxiObserver(dispatcher);

		final List<Point> route = generateRoute();
		this.taxiDriver = new TaxiDriver(taxi, route);
	}

	private List<Point> generateRoute() {
		final List<Point> route = new ArrayList<Point>();
		for (int i = 0; i < 3; i++) {
			route.add(new Point(i, i));
		}
		return route;
	}

	public void run() throws InterruptedException, ExecutionException {
		ExecutorService exec = Executors.newCachedThreadPool();
		CompletionService<String> results = new ExecutorCompletionService<String>(
				exec);
		results.submit(this.taxiObserver, "Observer is done");
		results.submit(this.taxiDriver, "Taxi driver is done");

		System.out.println(results.take().get());
		exec.shutdown();
	}
}
