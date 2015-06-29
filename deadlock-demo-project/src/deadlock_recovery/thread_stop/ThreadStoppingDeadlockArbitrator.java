package deadlock_recovery.thread_stop;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class ThreadStoppingDeadlockArbitrator {
	private static final ThreadMXBean tmb = ManagementFactory.getThreadMXBean();

	public boolean tryResolveDeadlock(int attempts, long timeout, TimeUnit unit) throws InterruptedException {
		for (int i = 0; i < attempts; i++) {
			long[] ids = tmb.findDeadlockedThreads();
			if (ids == null) {
				return true;
			}

			Thread t = findThread(ids[i % ids.length]);
			if (t == null) {
				throw new IllegalStateException("Could not find thread");
			}
			System.out.println(String.format("Stopping thread [%s]", t.getName()));
			t.stop();
			unit.sleep(timeout);
		}
		return false;
	}

	public boolean tryResolveDeadlock() throws InterruptedException {
		return tryResolveDeadlock(3, 1, TimeUnit.SECONDS);
	}

	private Thread findThread(long id) {
		for (Thread thread : Thread.getAllStackTraces().keySet()) {
			if (thread.getId() == id)
				return thread;
		}
		return null;
	}
}
