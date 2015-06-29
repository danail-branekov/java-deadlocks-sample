package philosophers.deadlock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests whether the Symposium ends in a deadlock. You might need to run it a
 * few times on your machine before the deadlock surfaces.
 *
 * @author Heinz Kabutz
 */
public class SymposiumTest {
	@Test
	public void runSymposium() throws InterruptedException {
		Symposium symposium = new Symposium(5);
		ThinkerStatus status = symposium.run();
		assertEquals(ThinkerStatus.HAPPY_THINKER, status);
	}
}