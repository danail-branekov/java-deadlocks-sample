package deadlock_recovery.thread_stop;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

import tools.ThreadDeadlockDetector;
import tools.ThreadDeadlockDetector.Listener;

public class DeadlockRecoveryTest_ThreadStop {
	@Test
	public void testDeadlockRecoveryWithThreadStop() throws InterruptedException {
		final ThreadDeadlockDetector deadlockDetector = new ThreadDeadlockDetector();
		deadlockDetector.addListener(new Listener() {
			final ThreadStoppingDeadlockArbitrator deadlockArbitrator = new ThreadStoppingDeadlockArbitrator();

			@Override
			public void deadlockDetected(Thread[] deadlockedThreads) {
				System.out.println("Deadlock detected, trying to recover");
				try {
					System.out.println("Deadlock recovery result: " + deadlockArbitrator.tryResolveDeadlock());
				} catch (InterruptedException e) {
					System.out.println("Deadlock recovery was interrupted");
				}
			}
		});

		final ReentrantLock lock1 = new ReentrantLock();
		final ReentrantLock lock2 = new ReentrantLock();
		final CountDownLatch latch = new CountDownLatch(2);

		final ExecutorService execService = Executors.newCachedThreadPool();
		execService.submit(new TestTask("Task1", lock1, lock2, 1000, latch));
		execService.submit(new TestTask("Task2", lock2, lock1, 2000, latch));
		execService.shutdown();
		while (!execService.isTerminated()) {
			Thread.sleep(1000);
		}
	}

	private static class TestTask implements Runnable {
		private final String taskName;
		private final ReentrantLock first;
		private final ReentrantLock second;
		private final long sleepTime;
		private final CountDownLatch latch;

		public TestTask(String taskName, ReentrantLock first, ReentrantLock second, long sleepTime, final CountDownLatch latch) {
			this.taskName = taskName;
			this.first = first;
			this.second = second;
			this.sleepTime = sleepTime;
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				latch.countDown();
				latch.await();
				first.lock();
				try {
					System.out.println(String.format("Task [%s] locked first lock", taskName));
					Thread.sleep(sleepTime);
					second.lock();
					try {
						System.out.println(String.format("Task [%s] locked second lock", taskName));
						Thread.sleep(sleepTime);
						System.out.println(String.format("Task [%s] is done", taskName));
					} finally {
						second.unlock();
						System.out.println(String.format("Task [%s] unlocked second lock"));
					}
				} finally {
					first.unlock();
					System.out.println(String.format("Task [%s] unlocked first lock"));
				}
			} catch (InterruptedException e) {
				System.out.println(String.format("Task [%s] has been interrupted", taskName));
			}
		}
	}
}
