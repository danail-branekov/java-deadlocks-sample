package taxi.deadlock;

import java.util.concurrent.ExecutionException;

import org.junit.Test;

import tools.ThreadDeadlockDetector;
import tools.ThreadDeadlockReporter;

/**
 * This test would run a TexiCompany which uses taxis which do not use open
 * calls. As a result we expect a deadlock which is reported by the
 * {@link ThreadDeadlockDetector}
 *
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
