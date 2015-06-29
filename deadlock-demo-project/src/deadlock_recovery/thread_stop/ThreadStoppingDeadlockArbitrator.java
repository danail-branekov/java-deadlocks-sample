package deadlock_recovery.thread_stop;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
			doStopThread(t);
			unit.sleep(timeout);
		}
		return false;
	}

	private void doStopThread(Thread t) {
		// Invoking Thread.stop() with Oracle JDK8 throws
		// UnsupportedOperationException. Therefore we are invoking
		// Thread.stop0() via reflection :)
		//
		// t.stop(new DeadlockVictimError(t));
		try {
			Method stopMethod = Thread.class.getDeclaredMethod("stop0", Object.class);
			stopMethod.setAccessible(true);
			stopMethod.invoke(t, new DeadlockVictimError(t));
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		}
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
