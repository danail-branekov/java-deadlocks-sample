package philosophers.deadlock_free.v1;

import java.util.concurrent.atomic.AtomicLong;

/**
 * "Krasi" means wine in Greek, which is where the philosophers used to live
 * many thousands of years ago. We use the Krasi objects as locks to synchronize
 * on.
 *
 * @author Heinz Kabutz
 */
public class Krasi implements Comparable<Krasi> {
	private final static AtomicLong nextCupNumber = new AtomicLong();
	private final long cupNumber = nextCupNumber.incrementAndGet();

	@Override
	public int compareTo(Krasi o) {
		return Long.compare(cupNumber, o.cupNumber);
	}

	@Override
	public String toString() {
		return "Krasi{cupNumber=" + cupNumber + '}';
	}
}
