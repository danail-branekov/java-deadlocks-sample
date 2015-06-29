package semaphores.deadlock;

import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import tools.ThreadDeadlockDetector;
import tools.ThreadDeadlockReporter;

/**
 * This test runs two tasks which acquire {@link Semaphore} in inverted order
 * thus causing a deadlock. As {@link ThreadMXBean} is unable to detect such
 * deadlocks, the deadlock will not be reported
 */
public class SemaphoresDeadlockTest {
	@Test
	public void testSemaphoresDeadlock() throws InterruptedException {
		final ThreadDeadlockDetector deadlockDetector = new ThreadDeadlockDetector();
		deadlockDetector.addListener(new ThreadDeadlockReporter());

		final Semaphore s1 = new Semaphore(1);
		final Semaphore s2 = new Semaphore(1);
		final CountDownLatch latch = new CountDownLatch(1);

		final ExecutorService execService = Executors.newCachedThreadPool();
		execService.submit(new TestTask("Task1", s1, s2, 1000, latch));
		execService.submit(new TestTask("Task2", s2, s1, 2000, latch));
		latch.countDown();
		execService.awaitTermination(1, TimeUnit.DAYS);
	}

	private static class TestTask implements Runnable {
		private final String taskName;
		private final Semaphore first;
		private final Semaphore second;
		private final long sleepTime;
		private final CountDownLatch latch;

		public TestTask(String taskName, Semaphore first, Semaphore second, long sleepTime, final CountDownLatch latch) {
			this.taskName = taskName;
			this.first = first;
			this.second = second;
			this.sleepTime = sleepTime;
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				latch.await();
				first.acquire();
				try {
					System.out.println(String.format("Task [%s] acquired first semaphore", taskName));
					Thread.sleep(sleepTime);
					second.acquire();
					try {
						System.out.println(String.format("Task [%s] acquired second semaphore", taskName));
						Thread.sleep(sleepTime);
						System.out.println(String.format("Task [%s] is done", taskName));
					} finally {
						second.release();
					}
				} finally {
					first.release();
				}
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
