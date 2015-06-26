package deadlock_recovery.thread_stop;

public class DeadlockVictimError extends Error {
	private final Thread victim;

	public DeadlockVictimError(Thread victim) {
		super("Deadlock victim: " + victim);
		this.victim = victim;
	}

	public Thread getVictim() {
		return victim;
	}
}