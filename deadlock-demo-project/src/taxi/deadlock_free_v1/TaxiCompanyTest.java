package taxi.deadlock_free_v1;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import tools.ThreadDeadlockDetector;
import tools.ThreadDeadlockReporter;

/**
 * This test would run a taxi company whose Taxi and Dispatcher use open calls.
 * Therefore deadlock is not expected
 */
public class TaxiCompanyTest {
	@Test
	public void runCompany() throws InterruptedException, ExecutionException {
		final ThreadDeadlockDetector deadlockDetector = new ThreadDeadlockDetector();
		deadlockDetector.addListener(new ThreadDeadlockReporter());

		long startTime = System.currentTimeMillis();
		new TaxiCompany().run();
		System.out.println(String.format("Taxi company is done for %d ms", System.currentTimeMillis() - startTime));
	}
}
