package philosophers.deadlock_free.v1;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests whether the Symposium ends in a deadlock. This test should finish
 * quickly as our philosophers take the cups in the proper order.
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