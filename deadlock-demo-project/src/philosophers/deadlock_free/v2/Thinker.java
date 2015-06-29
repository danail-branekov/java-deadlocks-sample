package philosophers.deadlock_free.v2;

import java.util.concurrent.Callable;

/**
 * Our philosopher always first locks left, then right. If all of the thinkers
 * sit in a circle and their threads call "drink()" at the same time, then they
 * will end up with a deadlock.
 * <p>
 * Instead of locking first on left and then on right, change the code to lock
 * on Krasi with bigger hash code first. In case the hash code of both Krasis is
 * equal, lock the tie lock first. This will avoid the deadlock, as we will
 * always lock in the same order.
 *
 * @author Heinz Kabutz
 */
public class Thinker implements Callable<ThinkerStatus> {
	private static final Object TIE_LOCK = new Object();

	private final int id;
	private final Krasi left, right;
	private int drinks = 0;

	public Thinker(int id, Krasi right, Krasi left) {
		this.id = id;
		this.left = left;
		this.right = right;
	}

	@Override
	public ThinkerStatus call() throws Exception {
		for (int i = 0; i < 1000; i++) {
			drink();
			think();
		}
		return drinks == 1000 ? ThinkerStatus.HAPPY_THINKER : ThinkerStatus.UNHAPPY_THINKER;
	}

	@SuppressWarnings("boxing")
	public void drink() {
		long leftHashCode = System.identityHashCode(left);
		long rightHashCode = System.identityHashCode(right);
		if (leftHashCode > rightHashCode) {
			synchronized (left) {
				synchronized (right) {
					drinking();
				}
			}
		} else if (leftHashCode < rightHashCode) {
			synchronized (right) {
				synchronized (left) {
					drinking();
				}
			}

		} else {
			synchronized (TIE_LOCK) {
				synchronized (right) {
					synchronized (left) {
						drinking();
					}
				}
			}
		}
	}

	private void drinking() {
		if (!Thread.holdsLock(left) || !Thread.holdsLock(right)) {
			throw new IllegalMonitorStateException("Not holding both locks");
		}
		System.out.printf("(%d) Drinking%n", id);
		drinks++;
	}

	@SuppressWarnings("boxing")
	public void think() {
		System.out.printf("(%d) Thinking%n", id);
	}
}
