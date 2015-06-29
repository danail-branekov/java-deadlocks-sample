package philosophers.deadlock_free.v2;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * At the symposium, we create a bunch of thinkers and place cups of wine
 * between them. We then run them in a cached thread pool.
 *
 * @author Heinz Kabutz
 */
public class Symposium {
	private final Krasi[] cups;
	private final Thinker[] thinkers;

	public Symposium(int delegates) {
		cups = new Krasi[delegates];
		thinkers = new Thinker[delegates];
		for (int i = 0; i < cups.length; i++) {
			cups[i] = new Krasi();
		}
		for (int i = 0; i < delegates; i++) {
			Krasi right = cups[i];
			Krasi left = cups[(i + 1) % delegates];
			thinkers[i] = new Thinker(i, left, right);
		}
	}

	public ThinkerStatus run() throws InterruptedException {
		// do this after we created the symposium, so that we do not
		// let the reference to the Symposium escape.
		ExecutorService exec = Executors.newCachedThreadPool();
		CompletionService<ThinkerStatus> results = new ExecutorCompletionService<>(exec);
		for (Thinker thinker : thinkers) {
			results.submit(thinker);
		}
		ThinkerStatus result = ThinkerStatus.HAPPY_THINKER;
		System.out.println("Waiting for results");
		for (@SuppressWarnings("unused")
		Thinker thinker : thinkers) {
			try {
				ThinkerStatus status = results.take().get();
				System.out.println(status);
				if (status == ThinkerStatus.UNHAPPY_THINKER) {
					result = ThinkerStatus.UNHAPPY_THINKER;
				}
			} catch (ExecutionException e) {
				e.getCause().printStackTrace();
			}
		}
		exec.shutdown();
		return result;
	}
}