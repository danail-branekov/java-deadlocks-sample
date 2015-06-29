package tools;

import tools.ThreadDeadlockDetector.Listener;

public class ThreadDeadlockReporter implements Listener {

	@Override
	public void deadlockDetected(Thread[] deadlockedThreads) {
		System.out.println("Deadlock detected. Here are the deadlocked threads:");
		for (Thread t : deadlockedThreads) {
			System.out.println(t.getName());
		}
	}

}
